package com.diplab.activiti.engine.impl.bpmn.parser.handler;

import java.util.ArrayList;
import java.util.List;

import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.bpmn.parser.handler.AbstractBpmnParseHandler;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.diplab.activiti.Constant;
import com.diplab.activiti.Constant.TemperatureMode;
import com.diplab.activiti.bpmn.model.TemperatureEventDefinition;
import com.diplab.activiti.engine.impl.jobexecutor.TemperatureDeclarationImpl;
import com.diplab.activiti.engine.impl.jobexecutor.TemperatureStartEventJobHandler;

public class TemperatureEventDefinitionParserHandler extends
		AbstractBpmnParseHandler<TemperatureEventDefinition> {
	private static final Logger LOG = LoggerFactory
			.getLogger(TemperatureEventDefinitionParserHandler.class);

	@Override
	protected void executeParse(BpmnParse bpmnParse,
			TemperatureEventDefinition element) {

		ActivityImpl tempActivity = bpmnParse.getCurrentActivity();
		if (bpmnParse.getCurrentFlowElement() instanceof StartEvent) {

			ProcessDefinitionEntity processDefinition = bpmnParse
					.getCurrentProcessDefinition();
			tempActivity.setProperty("type", "startTempEvent");
			TemperatureDeclarationImpl tempDeclaration = prepareTemperatureDeclaration(element);

			@SuppressWarnings("unchecked")
			List<TemperatureDeclarationImpl> tempDeclarations = (List<TemperatureDeclarationImpl>) processDefinition
					.getProperty(Constant.PROPERTYNAME_START_TEMP);
			if (tempDeclarations == null) {
				tempDeclarations = new ArrayList<TemperatureDeclarationImpl>();
				processDefinition.setProperty(Constant.PROPERTYNAME_START_TEMP,
						tempDeclarations);
			}
			tempDeclarations.add(tempDeclaration);

		} else {
			LOG.error("Only start event support temperatureEventDefinition");
		}
	}

	@Override
	protected Class<? extends BaseElement> getHandledType() {
		return TemperatureEventDefinition.class;
	}

	private TemperatureDeclarationImpl prepareTemperatureDeclaration(
			TemperatureEventDefinition element) {
		double condition;
		int time;
		TemperatureMode mode = null;

		for (TemperatureMode mode2 : TemperatureMode.values()) {
			if (element.getMode().equalsIgnoreCase(mode2.calendarName))
				mode = mode2;
		}

		if (mode == null)
			throw new ActivitiException(String.format("%s is not supported.",
					element.getMode()));

		try {
			condition = Double.parseDouble(element.getCondition());
		} catch (NullPointerException | NumberFormatException e) {
			throw new ActivitiException(String.format(
					"condition (%s) format error or null.",
					element.getCondition()));
		}

		try {
			time = Integer.parseInt(element.getTime());
		} catch (NullPointerException | NumberFormatException e) {
			throw new ActivitiException(String.format(
					"time (%s) format error or null.", element.getTime()));
		}

		return new TemperatureDeclarationImpl(condition, mode,
				TemperatureStartEventJobHandler.TYPE, element.getId(), time);
	}

}
