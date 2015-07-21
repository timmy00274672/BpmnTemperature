package com.diplab.activiti.test.process;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;

import com.diplab.activiti.engine.impl.cfg.DipStandaloneInMemProcessEngineConfiguration;

public class AsynConfig {

	public static void main(String[] args) throws InterruptedException {
		ProcessEngineConfigurationImpl config = new DipStandaloneInMemProcessEngineConfiguration();
		config.setJobExecutorActivate(true);

		final ProcessEngine processEngine = config.buildProcessEngine();

		processEngine.getRepositoryService().createDeployment()
				.disableSchemaValidation().disableBpmnValidation()
				.addClasspathResource("bpmn/async-continuation.bpmn").deploy();

		processEngine.getRuntimeService().startProcessInstanceByKey(
				"asyn-continuation");
		System.out.println(processEngine.getManagementService()
				.createJobQuery().list().get(0));

	}
}
