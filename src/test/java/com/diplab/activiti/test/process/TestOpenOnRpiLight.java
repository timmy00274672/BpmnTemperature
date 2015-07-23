package com.diplab.activiti.test.process;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;

import com.diplab.activiti.engine.impl.cfg.DipStandaloneInMemProcessEngineConfiguration;
import com.diplab.device.swtich.RpiTrunLightController;
import com.diplab.device.swtich.SwitchController;

public class TestOpenOnRpiLight {

	public static void main(String[] args) throws InterruptedException {
		ProcessEngineConfigurationImpl config = new DipStandaloneInMemProcessEngineConfiguration();
		// config.setJobExecutorActivate(true);

		final ProcessEngine processEngine = config.buildProcessEngine();

		SwitchController.addController(new RpiTrunLightController());

		processEngine.getRepositoryService().createDeployment()
				.disableSchemaValidation().disableBpmnValidation()
				.addClasspathResource("bpmn/turnOnLightOnRpi.bpmn").deploy();

		processEngine.getRuntimeService()
				.startProcessInstanceByKey("myProcess");
	}
}
