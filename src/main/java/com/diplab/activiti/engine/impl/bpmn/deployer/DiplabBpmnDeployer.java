package com.diplab.activiti.engine.impl.bpmn.deployer;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.delegate.event.impl.ActivitiEventBuilder;
import org.activiti.engine.impl.bpmn.deployer.BpmnDeployer;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.cmd.CancelJobsCmd;
import org.activiti.engine.impl.cmd.DeploymentSettings;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.db.DbSqlSession;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.DeploymentEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntityManager;
import org.activiti.engine.impl.persistence.entity.ResourceEntity;
import org.activiti.engine.impl.persistence.entity.TimerEntity;
import org.activiti.engine.impl.util.IoUtil;
import org.activiti.engine.runtime.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.diplab.activiti.Constant;
import com.diplab.activiti.engine.impl.jobexecutor.SmokeDeclarationImpl;
import com.diplab.activiti.engine.impl.jobexecutor.SmokeStartEventJobHandler;
import com.diplab.activiti.engine.impl.jobexecutor.TemperatureDeclarationImpl;
import com.diplab.activiti.engine.impl.jobexecutor.TemperatureStartEventJobHandler;
import com.diplab.activiti.engine.impl.persistence.entity.SmokeEntity;
import com.diplab.activiti.engine.impl.persistence.entity.TemperatureEntity;

public class DiplabBpmnDeployer extends BpmnDeployer {

	private static final Logger log = LoggerFactory
			.getLogger(DiplabBpmnDeployer.class);

	@Override
	public void deploy(DeploymentEntity deployment,
			Map<String, Object> deploymentSettings) {
		log.debug("Processing deployment {}", deployment.getName());

		List<ProcessDefinitionEntity> processDefinitions = new ArrayList<ProcessDefinitionEntity>();
		Map<String, ResourceEntity> resources = deployment.getResources();

		final ProcessEngineConfigurationImpl processEngineConfiguration = Context
				.getProcessEngineConfiguration();
		for (String resourceName : resources.keySet()) {

			log.info("Processing resource {}", resourceName);
			if (isBpmnResource(resourceName)) {
				ResourceEntity resource = resources.get(resourceName);
				byte[] bytes = resource.getBytes();
				ByteArrayInputStream inputStream = new ByteArrayInputStream(
						bytes);

				BpmnParse bpmnParse = bpmnParser.createParse()
						.sourceInputStream(inputStream).deployment(deployment)
						.name(resourceName);

				if (deploymentSettings != null) {

					// Schema validation if needed
					if (deploymentSettings
							.containsKey(DeploymentSettings.IS_BPMN20_XSD_VALIDATION_ENABLED)) {
						bpmnParse
								.setValidateSchema((Boolean) deploymentSettings
										.get(DeploymentSettings.IS_BPMN20_XSD_VALIDATION_ENABLED));
					}

					// Process validation if needed
					if (deploymentSettings
							.containsKey(DeploymentSettings.IS_PROCESS_VALIDATION_ENABLED)) {
						bpmnParse
								.setValidateProcess((Boolean) deploymentSettings
										.get(DeploymentSettings.IS_PROCESS_VALIDATION_ENABLED));
					}

				} else {
					// On redeploy, we assume it is validated at the first
					// deploy
					bpmnParse.setValidateSchema(false);
					bpmnParse.setValidateProcess(false);
				}

				bpmnParse.execute();

				for (ProcessDefinitionEntity processDefinition : bpmnParse
						.getProcessDefinitions()) {
					processDefinition.setResourceName(resourceName);

					if (deployment.getTenantId() != null) {
						processDefinition.setTenantId(deployment.getTenantId()); // process
																					// definition
																					// inherits
																					// the
																					// tenant
																					// id
					}

					String diagramResourceName = getDiagramResourceForProcess(
							resourceName, processDefinition.getKey(), resources);

					// Only generate the resource when deployment is new to
					// prevent modification of deployment resources
					// after the process-definition is actually deployed. Also
					// to prevent resource-generation failure every
					// time the process definition is added to the
					// deployment-cache when diagram-generation has failed the
					// first time.
					if (deployment.isNew()) {
						if (processEngineConfiguration
								.isCreateDiagramOnDeploy()
								&& diagramResourceName == null
								&& processDefinition
										.isGraphicalNotationDefined()) {
							try {
								byte[] diagramBytes = IoUtil
										.readInputStream(
												processEngineConfiguration
														.getProcessDiagramGenerator()
														.generateDiagram(
																bpmnParse
																		.getBpmnModel(),
																"png",
																processEngineConfiguration
																		.getActivityFontName(),
																processEngineConfiguration
																		.getLabelFontName(),
																processEngineConfiguration
																		.getClassLoader()),
												null);
								diagramResourceName = getProcessImageResourceName(
										resourceName,
										processDefinition.getKey(), "png");
								createResource(diagramResourceName,
										diagramBytes, deployment);
							} catch (Throwable t) { // if anything goes wrong,
													// we don't store the image
													// (the process will still
													// be executable).
								log.warn(
										"Error while generating process diagram, image will not be stored in repository",
										t);
							}
						}
					}

					processDefinition
							.setDiagramResourceName(diagramResourceName);
					processDefinitions.add(processDefinition);
				}
			}
		}

		// check if there are process definitions with the same process key to
		// prevent database unique index violation
		List<String> keyList = new ArrayList<String>();
		for (ProcessDefinitionEntity processDefinition : processDefinitions) {
			if (keyList.contains(processDefinition.getKey())) {
				throw new ActivitiException(
						"The deployment contains process definitions with the same key (process id atrribute), this is not allowed");
			}
			keyList.add(processDefinition.getKey());
		}

		CommandContext commandContext = Context.getCommandContext();
		ProcessDefinitionEntityManager processDefinitionManager = commandContext
				.getProcessDefinitionEntityManager();
		DbSqlSession dbSqlSession = commandContext
				.getSession(DbSqlSession.class);
		for (ProcessDefinitionEntity processDefinition : processDefinitions) {
			List<TimerEntity> timers = new ArrayList<TimerEntity>();
			if (deployment.isNew()) {
				int processDefinitionVersion;

				ProcessDefinitionEntity latestProcessDefinition = null;
				if (processDefinition.getTenantId() != null
						&& !ProcessEngineConfiguration.NO_TENANT_ID
								.equals(processDefinition.getTenantId())) {
					latestProcessDefinition = processDefinitionManager
							.findLatestProcessDefinitionByKeyAndTenantId(
									processDefinition.getKey(),
									processDefinition.getTenantId());
				} else {
					latestProcessDefinition = processDefinitionManager
							.findLatestProcessDefinitionByKey(processDefinition
									.getKey());
				}

				if (latestProcessDefinition != null) {
					processDefinitionVersion = latestProcessDefinition
							.getVersion() + 1;
				} else {
					processDefinitionVersion = 1;
				}

				processDefinition.setVersion(processDefinitionVersion);
				processDefinition.setDeploymentId(deployment.getId());

				String nextId = idGenerator.getNextId();
				String processDefinitionId = processDefinition.getKey() + ":"
						+ processDefinition.getVersion() + ":" + nextId; // ACT-505

				// ACT-115: maximum id length is 64 charcaters
				if (processDefinitionId.length() > 64) {
					processDefinitionId = nextId;
				}
				processDefinition.setId(processDefinitionId);

				if (commandContext.getProcessEngineConfiguration()
						.getEventDispatcher().isEnabled()) {
					commandContext
							.getProcessEngineConfiguration()
							.getEventDispatcher()
							.dispatchEvent(
									ActivitiEventBuilder.createEntityEvent(
											ActivitiEventType.ENTITY_CREATED,
											processDefinition));
				}

				removeObsoleteTemperatures(processDefinition);
				addTemperautres(processDefinition);

				removeObsoleteSmokes(processDefinition);
				addSmokes(processDefinition);

				removeObsoleteTimers(processDefinition);
				addTimerDeclarations(processDefinition, timers);

				removeObsoleteMessageEventSubscriptions(processDefinition,
						latestProcessDefinition);
				addMessageEventSubscriptions(processDefinition);

				removeObsoleteSignalEventSubScription(processDefinition,
						latestProcessDefinition);
				addSignalEventSubscriptions(processDefinition);

				dbSqlSession.insert(processDefinition);
				addAuthorizations(processDefinition);

				if (commandContext.getProcessEngineConfiguration()
						.getEventDispatcher().isEnabled()) {
					commandContext
							.getProcessEngineConfiguration()
							.getEventDispatcher()
							.dispatchEvent(
									ActivitiEventBuilder
											.createEntityEvent(
													ActivitiEventType.ENTITY_INITIALIZED,
													processDefinition));
				}

				scheduleTimers(timers);

			} else {
				String deploymentId = deployment.getId();
				processDefinition.setDeploymentId(deploymentId);

				ProcessDefinitionEntity persistedProcessDefinition = null;
				if (processDefinition.getTenantId() == null
						|| ProcessEngineConfiguration.NO_TENANT_ID
								.equals(processDefinition.getTenantId())) {
					persistedProcessDefinition = processDefinitionManager
							.findProcessDefinitionByDeploymentAndKey(
									deploymentId, processDefinition.getKey());
				} else {
					persistedProcessDefinition = processDefinitionManager
							.findProcessDefinitionByDeploymentAndKeyAndTenantId(
									deploymentId, processDefinition.getKey(),
									processDefinition.getTenantId());
				}

				if (persistedProcessDefinition != null) {
					processDefinition.setId(persistedProcessDefinition.getId());
					processDefinition.setVersion(persistedProcessDefinition
							.getVersion());
					processDefinition
							.setSuspensionState(persistedProcessDefinition
									.getSuspensionState());
				}
			}

			// Add to cache
			processEngineConfiguration.getDeploymentManager()
					.getProcessDefinitionCache()
					.add(processDefinition.getId(), processDefinition);

			// Add to deployment for further usage
			deployment.addDeployedArtifact(processDefinition);
		}
	}

	private void addSmokes(ProcessDefinitionEntity processDefinition) {
		@SuppressWarnings("unchecked")
		List<SmokeDeclarationImpl> smokeDeclarations = (List<SmokeDeclarationImpl>) processDefinition
				.getProperty(Constant.PROPERTYNAME_START_SMOKE);
		if (smokeDeclarations != null) {
			for (SmokeDeclarationImpl smokeDeclarationImpl : smokeDeclarations) {
				SmokeEntity smokeEntity = new SmokeEntity(smokeDeclarationImpl,
						processDefinition.getId());
				smokeEntity.insert();
			}
		}
	}

	private void addTemperautres(ProcessDefinitionEntity processDefinition) {
		@SuppressWarnings("unchecked")
		List<TemperatureDeclarationImpl> tempDeclarations = (List<TemperatureDeclarationImpl>) processDefinition
				.getProperty(Constant.PROPERTYNAME_START_TEMP);
		if (tempDeclarations != null) {
			for (TemperatureDeclarationImpl temperatureDeclarationImpl : tempDeclarations) {
				TemperatureEntity temperatureEntity = new TemperatureEntity(
						temperatureDeclarationImpl, processDefinition.getId());
				temperatureEntity.insert();
			}
		}
	}

	private void removeObsoleteSmokes(ProcessDefinitionEntity processDefinition) {
		List<Job> jobsToDelete = Context
				.getCommandContext()
				.getJobEntityManager()
				.findJobsByConfiguration(SmokeStartEventJobHandler.TYPE,
						processDefinition.getKey());

		for (Job job : jobsToDelete) {
			new CancelJobsCmd(job.getId()).execute(Context.getCommandContext());
		}
	}

	private void removeObsoleteTemperatures(
			ProcessDefinitionEntity processDefinition) {
		List<Job> jobsToDelete = Context
				.getCommandContext()
				.getJobEntityManager()
				.findJobsByConfiguration(TemperatureStartEventJobHandler.TYPE,
						processDefinition.getKey());

		for (Job job : jobsToDelete) {
			new CancelJobsCmd(job.getId()).execute(Context.getCommandContext());
		}
	}

	private void scheduleTimers(List<TimerEntity> timers) {
		for (TimerEntity timer : timers) {
			Context.getCommandContext().getJobEntityManager().schedule(timer);
		}
	}

}
