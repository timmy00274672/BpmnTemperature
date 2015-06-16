package com.diplab.activiti.engine.impl.bpmn.parser.handler;

import java.util.ArrayList;
import java.util.List;

import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.bpmn.parser.handler.AbstractBpmnParseHandler;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.diplab.activiti.Constant;
import com.diplab.activiti.bpmn.model.SmokeEventDefinition;
import com.diplab.activiti.engine.impl.jobexecutor.SmokeDeclarationImpl;
import com.diplab.activiti.engine.impl.jobexecutor.SmokeStartEventJobHandler;
import com.diplab.device.RecordMode;

public class SmokeEventDefinitionParserHandler extends
		AbstractBpmnParseHandler<SmokeEventDefinition> {
	private static final Logger LOG = LoggerFactory
			.getLogger(SmokeEventDefinitionParserHandler.class);

	private SmokeDeclarationImpl prepareSmokeDeclaration(
			SmokeEventDefinition element) {
		double condition;
		int time;
		RecordMode mode = null;

		// deal mode
		for (RecordMode mode2 : RecordMode.values()) {
			if (element.getMode().equalsIgnoreCase(mode2.calendarName))
				mode = mode2;
		}

		if (mode == null) {
			mode = RecordMode.NONE;
			LOG.info((String.format("mode (%s) format error or null.",
					element.getMode())));
		}

		// deal condition . default 0
		try {
			condition = Double.parseDouble(element.getCondition());
		} catch (NullPointerException | NumberFormatException e) {
			condition = 0;
			LOG.info((String.format("condition (%s) format error or null.",
					element.getCondition())));
		}

		// deal time . default 1
		try {
			time = Integer.parseInt(element.getTime());
		} catch (NullPointerException | NumberFormatException e) {
			time = 1;
			LOG.info(String.format("time (%s) format error or null.",
					element.getTime()));
		}

		return new SmokeDeclarationImpl(condition, mode,
				SmokeStartEventJobHandler.TYPE, element.getSensorId(), time);
	}

	@Override
	protected void executeParse(BpmnParse bpmnParse,
			SmokeEventDefinition element) {

		ActivityImpl tempActivity = bpmnParse.getCurrentActivity();
		if (bpmnParse.getCurrentFlowElement() instanceof StartEvent) {

			ProcessDefinitionEntity processDefinition = bpmnParse
					.getCurrentProcessDefinition();
			tempActivity.setProperty("type", "startSmokeEvent");
			SmokeDeclarationImpl smokeDeclaration = prepareSmokeDeclaration(element);

			@SuppressWarnings("unchecked")
			List<SmokeDeclarationImpl> smokeDeclarations = (List<SmokeDeclarationImpl>) processDefinition
					.getProperty(Constant.PROPERTYNAME_START_SMOKE);
			if (smokeDeclarations == null) {
				smokeDeclarations = new ArrayList<SmokeDeclarationImpl>();
				processDefinition.setProperty(
						Constant.PROPERTYNAME_START_SMOKE, smokeDeclarations);
			}
			smokeDeclarations.add(smokeDeclaration);

		} else {
			LOG.error("Only start event support smokeEventDefinition");
		}
	}

	@Override
	protected Class<? extends BaseElement> getHandledType() {
		return SmokeEventDefinition.class;
	}

}
