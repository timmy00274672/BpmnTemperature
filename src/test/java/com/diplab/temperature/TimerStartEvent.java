package com.diplab.temperature;

import java.util.Date;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.ServiceImpl;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.JobEntity;

public class TimerStartEvent {

	public static void main(String[] args) {
		ProcessEngineConfigurationImpl standaloneInMemProcessEngineConfiguration = new StandaloneInMemProcessEngineConfiguration();

		standaloneInMemProcessEngineConfiguration.setJobExecutorActivate(true);

		ProcessEngine processEngine = standaloneInMemProcessEngineConfiguration
				.buildProcessEngine();

		processEngine.getProcessEngineConfiguration().getJobExecutor()
				.executeJobs(null);

		ServiceImpl service = (ServiceImpl) processEngine
				.getRepositoryService();
		service.getCommandExecutor().execute(new Command<String>() {

			@Override
			public String execute(CommandContext commandContext) {
				JobEntity jobEntity = new JobEntity() {
					@Override
					public Date getDuedate() {
						return new Date(0);
					}
				};
				jobEntity.insert();
				;
				commandContext.getJobEntityManager().findJobById("1");
				return jobEntity.getId();
			}
		});
		// System.out.println(Context.getJobExecutorContext().getCurrentJob());
		//
		processEngine.getRepositoryService().createDeployment()
				.disableSchemaValidation().disableBpmnValidation()
				.addClasspathResource("timerstart.bpmn20.xml").deploy();

	}

}
