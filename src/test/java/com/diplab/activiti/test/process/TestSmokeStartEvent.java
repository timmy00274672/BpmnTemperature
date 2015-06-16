package com.diplab.activiti.test.process;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;

import com.diplab.activiti.engine.impl.cfg.DipProcessEngineConfiguration;
import com.diplab.device.smoke.SmokeReceiver;
import com.diplab.device.smoke.SmokeReceiverImp;

public class TestSmokeStartEvent {

	public static void main(String[] args) throws InterruptedException {
		ProcessEngineConfigurationImpl config = new DipProcessEngineConfiguration();
		config.setJdbcUrl("jdbc:h2:tcp://localhost/Activiti");
		config.setDatabaseSchemaUpdate("drop-create");
		config.setJobExecutorActivate(true);
		SmokeReceiver.addReceiver(new SmokeReceiverImp("smoke-S-01"));

		final ProcessEngine processEngine = config.buildProcessEngine();

		processEngine.getRepositoryService().createDeployment()
				.disableSchemaValidation().disableBpmnValidation()
				.addClasspathResource("bpmn/smokeStart.bpmn").deploy();

	}
}
