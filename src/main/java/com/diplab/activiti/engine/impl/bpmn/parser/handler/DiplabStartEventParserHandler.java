package com.diplab.activiti.engine.impl.bpmn.parser.handler;

import org.activiti.bpmn.model.EventDefinition;
import org.activiti.bpmn.model.MessageEventDefinition;
import org.activiti.bpmn.model.SignalEventDefinition;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.TimerEventDefinition;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.bpmn.parser.handler.StartEventParseHandler;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.apache.commons.lang3.StringUtils;

import com.diplab.activiti.bpmn.model.TemperatureEventDefinition;

public class DiplabStartEventParserHandler extends StartEventParseHandler {

	@Override
	protected void createProcessDefinitionStartEvent(BpmnParse bpmnParse,
			ActivityImpl startEventActivity, StartEvent startEvent,
			ProcessDefinitionEntity processDefinition) {
		if (StringUtils.isNotEmpty(startEvent.getInitiator())) {
			processDefinition.setProperty(PROPERTYNAME_INITIATOR_VARIABLE_NAME,
					startEvent.getInitiator());
		}

		// all start events share the same behavior
		startEventActivity.setActivityBehavior(bpmnParse
				.getActivityBehaviorFactory()
				.createNoneStartEventActivityBehavior(startEvent));
		if (!startEvent.getEventDefinitions().isEmpty()) {
			EventDefinition eventDefinition = startEvent.getEventDefinitions()
					.get(0);
			if (eventDefinition instanceof TimerEventDefinition
					|| eventDefinition instanceof MessageEventDefinition
					|| eventDefinition instanceof SignalEventDefinition
					|| eventDefinition instanceof TemperatureEventDefinition) {
				bpmnParse.getBpmnParserHandlers().parseElement(bpmnParse,
						eventDefinition);
			}
		}
	}

}
