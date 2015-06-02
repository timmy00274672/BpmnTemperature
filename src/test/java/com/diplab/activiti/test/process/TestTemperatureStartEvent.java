package com.diplab.activiti.test.process;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;

import com.diplab.activiti.engine.impl.cfg.DipProcessEngineConfiguration;
import com.diplab.activiti.temperature.TemperatureReceiver;
import com.diplab.activiti.temperature.TemperatureReceiverImp;

public class TestTemperatureStartEvent {

	public static void main(String[] args) throws InterruptedException {
		ProcessEngineConfigurationImpl config = new DipProcessEngineConfiguration();
		config.setJdbcUrl("jdbc:h2:tcp://localhost/Activiti");
		config.setDatabaseSchemaUpdate("drop-create");
		config.setJobExecutorActivate(true);

		final ProcessEngine processEngine = config.buildProcessEngine();
		
		TemperatureReceiver.addReceiver(new TemperatureReceiverImp("TEMP-S-01"));
		TemperatureReceiver.addReceiver(new TemperatureReceiverImp("TEMP-S-02"));
		
		processEngine.getRepositoryService().createDeployment()
				.disableSchemaValidation().disableBpmnValidation()
				.addClasspathResource("bpmn/temperatureStartEvent.bpmn").deploy();

	}
}
