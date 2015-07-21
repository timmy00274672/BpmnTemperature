package com.diplab.activiti.test.process;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;

import com.diplab.activiti.engine.impl.cfg.DipStandaloneInMemProcessEngineConfiguration;
import com.diplab.device.temperature.TemperatureReceiver;
import com.diplab.device.temperature.TemperatureReceiverImp;

public class TestReadTemperatureTask {

	public static void main(String[] args) throws InterruptedException {
		ProcessEngineConfigurationImpl config = new DipStandaloneInMemProcessEngineConfiguration();
		// config.setJobExecutorActivate(true);

		final ProcessEngine processEngine = config.buildProcessEngine();

		TemperatureReceiver
				.addReceiver(new TemperatureReceiverImp("TEMP-S-01"));

		processEngine.getRepositoryService().createDeployment()
				.disableSchemaValidation().disableBpmnValidation()
				.addClasspathResource("bpmn/readTempTask.bpmn").deploy();

		processEngine.getRuntimeService().startProcessInstanceByKey(
				"testReadTempTask");

	}
}
