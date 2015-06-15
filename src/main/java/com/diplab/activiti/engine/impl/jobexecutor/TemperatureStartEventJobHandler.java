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
import org.springframework.util.SerializationUtils;

import com.diplab.activiti.engine.impl.persistence.entity.TemperatureEntity;
import com.diplab.device.temperature.Temperature;
import com.diplab.device.temperature.TemperatureReceiver;

public class TemperatureStartEventJobHandler implements JobHandler {
	public static final String TYPE = "temperature-start-event";

	// private Logger logger = LoggerFactory
	// .getLogger(TemperatureStartEventJobHandler.class);

	@Override
	public void execute(JobEntity job, String configuration,
			ExecutionEntity execution, CommandContext commandContext) {
		ByteArrayRef ref = new ByteArrayRef(configuration);
		TemperatureEntity entity = (TemperatureEntity) SerializationUtils
				.deserialize(ref.getBytes());

		TemperatureReceiver receiver = TemperatureReceiver
				.getReceiverByDeviceId(entity.getSensorId());
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
					"Could not find process definition needed for temperature start event");
		}

		if (!processDefinition.isSuspended()) {
			if (commandContext.getEventDispatcher().isEnabled()) {
				commandContext.getEventDispatcher().dispatchEvent(
						ActivitiEventBuilder.createEntityEvent(
								ActivitiEventType.TIMER_FIRED, job));
			}

			List<Temperature> temperatures = receiver.getTemperatures();
			if (entity.prepareIsSatisfy().isSatisfy(temperatures)) {

				Map<String, Object> variables = new HashMap<String, Object>();
				variables.put("temperatures", temperatures);
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
