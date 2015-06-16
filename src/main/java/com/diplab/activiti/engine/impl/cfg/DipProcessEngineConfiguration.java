package com.diplab.activiti.engine.impl.cfg;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.engine.impl.bpmn.parser.BpmnParseHandlers;
import org.activiti.engine.impl.bpmn.parser.BpmnParser;
import org.activiti.engine.impl.bpmn.parser.factory.DefaultActivityBehaviorFactory;
import org.activiti.engine.impl.bpmn.parser.factory.DefaultListenerFactory;
import org.activiti.engine.impl.cfg.DefaultBpmnParseFactory;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.impl.jobexecutor.JobHandler;
import org.activiti.engine.impl.persistence.deploy.Deployer;
import org.activiti.engine.impl.util.ReflectUtil;
import org.activiti.engine.parse.BpmnParseHandler;

import com.diplab.activiti.bpmn.converter.DiplabStartEventXMLConverter;
import com.diplab.activiti.bpmn.converter.ReadTemperatureTaskXMLConverter;
import com.diplab.activiti.bpmn.converter.SwitchTaskXMLConverter;
import com.diplab.activiti.engine.impl.bpmn.deployer.DiplabBpmnDeployer;
import com.diplab.activiti.engine.impl.bpmn.parser.handler.DiplabStartEventParserHandler;
import com.diplab.activiti.engine.impl.bpmn.parser.handler.ReadTemperatureTaskParserHandler;
import com.diplab.activiti.engine.impl.bpmn.parser.handler.SmokeEventDefinitionParserHandler;
import com.diplab.activiti.engine.impl.bpmn.parser.handler.SwitchTaskParserHandler;
import com.diplab.activiti.engine.impl.bpmn.parser.handler.TemperatureEventDefinitionParserHandler;
import com.diplab.activiti.engine.impl.jobexecutor.SmokeStartEventJobHandler;
import com.diplab.activiti.engine.impl.jobexecutor.TemperatureStartEventJobHandler;

public class DipProcessEngineConfiguration extends
		StandaloneProcessEngineConfiguration {

	static {
		BpmnXMLConverter.addConverter(new DiplabStartEventXMLConverter());
		BpmnXMLConverter.addConverter(new ReadTemperatureTaskXMLConverter());
		BpmnXMLConverter.addConverter(new SwitchTaskXMLConverter());
	}

	public DipProcessEngineConfiguration() {
		this.setCustomDefaultBpmnParseHandlers(Arrays
				.<BpmnParseHandler> asList(new DiplabStartEventParserHandler()));

		this.setPostBpmnParseHandlers(Arrays.<BpmnParseHandler> asList(
				new TemperatureEventDefinitionParserHandler(),
				new SmokeEventDefinitionParserHandler(),
				new ReadTemperatureTaskParserHandler(),
				new SwitchTaskParserHandler()));

		this.setCustomJobHandlers(Arrays.<JobHandler> asList(
				new TemperatureStartEventJobHandler(),
				new SmokeStartEventJobHandler()));
	}

	@Override
	protected Collection<? extends Deployer> getDefaultDeployers() {
		List<Deployer> defaultDeployers = new ArrayList<Deployer>();

		if (bpmnDeployer == null) {
			bpmnDeployer = new DiplabBpmnDeployer();
		}

		bpmnDeployer.setExpressionManager(expressionManager);
		bpmnDeployer.setIdGenerator(idGenerator);

		if (bpmnParseFactory == null) {
			bpmnParseFactory = new DefaultBpmnParseFactory();
		}

		if (activityBehaviorFactory == null) {
			DefaultActivityBehaviorFactory defaultActivityBehaviorFactory = new DefaultActivityBehaviorFactory();
			defaultActivityBehaviorFactory
					.setExpressionManager(expressionManager);
			activityBehaviorFactory = defaultActivityBehaviorFactory;
		}

		if (listenerFactory == null) {
			DefaultListenerFactory defaultListenerFactory = new DefaultListenerFactory();
			defaultListenerFactory.setExpressionManager(expressionManager);
			listenerFactory = defaultListenerFactory;
		}

		if (bpmnParser == null) {
			bpmnParser = new BpmnParser();
		}

		bpmnParser.setExpressionManager(expressionManager);
		bpmnParser.setBpmnParseFactory(bpmnParseFactory);
		bpmnParser.setActivityBehaviorFactory(activityBehaviorFactory);
		bpmnParser.setListenerFactory(listenerFactory);

		List<BpmnParseHandler> parseHandlers = new ArrayList<BpmnParseHandler>();
		if (getPreBpmnParseHandlers() != null) {
			parseHandlers.addAll(getPreBpmnParseHandlers());
		}
		parseHandlers.addAll(getDefaultBpmnParseHandlers());
		if (getPostBpmnParseHandlers() != null) {
			parseHandlers.addAll(getPostBpmnParseHandlers());
		}

		BpmnParseHandlers bpmnParseHandlers = new BpmnParseHandlers();
		bpmnParseHandlers.addHandlers(parseHandlers);
		bpmnParser.setBpmnParserHandlers(bpmnParseHandlers);

		bpmnDeployer.setBpmnParser(bpmnParser);

		defaultDeployers.add(bpmnDeployer);
		return defaultDeployers;
	}

	@Override
	protected InputStream getMyBatisXmlConfigurationSteam() {
		return ReflectUtil.getResourceAsStream("mappings.xml");
	}

}
