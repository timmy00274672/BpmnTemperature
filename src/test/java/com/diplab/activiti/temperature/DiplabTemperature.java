package com.diplab.activiti.temperature;

import java.util.Arrays;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.parse.BpmnParseHandler;

import com.diplab.activiti.bpmn.converter.DiplabStartEventXMLConverter;
import com.diplab.activiti.engine.impl.bpmn.parser.handler.DiplabEventDefinitionParserHandler;
import com.diplab.activiti.engine.impl.bpmn.parser.handler.DiplabStartEventParserHandler;

public class DiplabTemperature {

	public static ProcessEngine processEngine;

	public static void main(String[] args) throws InterruptedException {
		ProcessEngineConfigurationImpl standaloneInMemProcessEngineConfiguration = (ProcessEngineConfigurationImpl) ProcessEngineConfiguration
				.createStandaloneInMemProcessEngineConfiguration();

		BpmnXMLConverter.addConverter(new DiplabStartEventXMLConverter());

		standaloneInMemProcessEngineConfiguration
				.setCustomDefaultBpmnParseHandlers(Arrays
						.<BpmnParseHandler> asList(new DiplabStartEventParserHandler()));

		standaloneInMemProcessEngineConfiguration
				.setPostBpmnParseHandlers(Arrays
						.<BpmnParseHandler> asList(new DiplabEventDefinitionParserHandler()));

		// standaloneInMemProcessEngineConfiguration.setCustomPostDeployers(customPostDeployers)

		standaloneInMemProcessEngineConfiguration.setJobExecutorActivate(true);

		final ProcessEngine processEngine = standaloneInMemProcessEngineConfiguration
				.buildProcessEngine();
		DiplabTemperature.processEngine = processEngine;

		processEngine
				.getRepositoryService()
				.createDeployment()
				.disableSchemaValidation()
				.disableBpmnValidation()
				.addClasspathResource(
						"com/diplab/activiti/temperature/process/ReadTempProcess.bpmn")
				.addClasspathResource(
						"com/diplab/activiti/temperature/process/SchedulerProcess.bpmn")
				.addClasspathResource(
						"temperature.bpmn20.xml")
				.deploy();

		processEngine.getRuntimeService().startProcessInstanceByKey(
				"ReadTempProcess");
		processEngine.getRuntimeService().startProcessInstanceByKey(
				"schedulerProcess");

	}
}
