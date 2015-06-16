package com.diplab.activiti.bpmn.converter.child;

import javax.xml.stream.XMLStreamReader;

import org.activiti.bpmn.converter.child.BaseChildElementParser;
import org.activiti.bpmn.converter.util.BpmnXMLUtil;
import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.Event;

import com.diplab.activiti.Constant;
import com.diplab.activiti.bpmn.model.SmokeEventDefinition;

public class SmokeEventDefinitionParser extends BaseChildElementParser {

	@Override
	public String getElementName() {

		return Constant.ELEMENT_SMOKE_EVENT_DEFINITION;
	}

	@Override
	public void parseChildElement(XMLStreamReader xtr,
			BaseElement parentElement, BpmnModel model) throws Exception {
		if (parentElement instanceof Event == false)
			return;

		SmokeEventDefinition smokeEventDefinition = new SmokeEventDefinition();
		BpmnXMLUtil.addXMLLocation(smokeEventDefinition, xtr);

		// We need other child parser for node: mode, condition, id
		BpmnXMLUtil.parseChildElements(getElementName(), smokeEventDefinition,
				xtr, Constant.DIP_PARSER, model);

		((Event) parentElement).getEventDefinitions().add(smokeEventDefinition);
	}

}
