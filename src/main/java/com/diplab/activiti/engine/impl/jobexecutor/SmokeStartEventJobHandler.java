package com.diplab.activiti.engine.impl.jobexecutor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.delegate.event.impl.ActivitiEventBuilder;
import org.activiti.engine.impl.cmd.StartProcessInstanceCmd;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.jobexecutor.JobHandler;
import org.activiti.engine.impl.persistence.deploy.DeploymentManager;
import org.activiti.engine.impl.persistence.entity.ByteArrayRef;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.JobEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang3.SerializationUtils;

import com.diplab.activiti.engine.impl.persistence.entity.SmokeEntity;
import com.diplab.device.IsSatisfy;
import com.diplab.device.smoke.Smoke;
import com.diplab.device.smoke.SmokeReceiver;

public class SmokeStartEventJobHandler implements JobHandler {
	public static final String TYPE = "smoke-start-event";

	@Override
	public void execute(JobEntity job, String configuration,
			ExecutionEntity execution, CommandContext commandContext) {
		ByteArrayRef ref = new ByteArrayRef(configuration);
		SmokeEntity entity = (SmokeEntity) SerializationUtils.deserialize(ref
				.getBytes());

		SmokeReceiver receiver = SmokeReceiver.getReceiverByDeviceId(entity
				.getSensorId());
		if (receiver == null) {
			throw new ActivitiException(String.format(
					"%s sensor is not presenting yet", entity.getSensorId()));
		}

		DeploymentManager deploymentCache = Context
				.getProcessEngineConfiguration().getDeploymentManager();

		ProcessDefinition processDefinition = null;
		processDefinition = deploymentCache
				.findDeployedProcessDefinitionById(job.getProcessDefinitionId());

		if (processDefinition == null) {
			throw new ActivitiException(
					"Could not find process definition needed for smoke start event");
		}

		if (!processDefinition.isSuspended()) {
			if (commandContext.getEventDispatcher().isEnabled()) {
				commandContext.getEventDispatcher().dispatchEvent(
						ActivitiEventBuilder.createEntityEvent(
								ActivitiEventType.TIMER_FIRED, job));
			}
			List<Smoke> smokes = receiver.getSmokes();
			if (IsSatisfy.<Smoke> prepareIsSatisfy(entity.getMode(),
					entity.getTimes(), entity.getCondition(),
					temp -> temp.getSmoke()).isSatisfy(smokes)) {

				Map<String, Object> variables = new HashMap<String, Object>();
				variables.put("smokes", smokes);
				new StartProcessInstanceCmd<Object>(null,
						processDefinition.getId(), null, variables)
						.execute(commandContext);
			}
		}
	}

	@Override
	public String getType() {
		return TYPE;
	}
}
