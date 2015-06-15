package com.diplab.activiti.bpmn.converter.child;

import javax.xml.stream.XMLStreamReader;

import org.activiti.bpmn.converter.child.BaseChildElementParser;
import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.BpmnModel;

import com.diplab.activiti.Constant;
import com.diplab.activiti.bpmn.model.SmokeEventDefinition;
import com.diplab.activiti.bpmn.model.TemperatureEventDefinition;

public class ConditionParser extends BaseChildElementParser {

	@Override
	public String getElementName() {
		return Constant.ATTRIBUTE_CONDITION;
	}

	@Override
	public void parseChildElement(XMLStreamReader xtr,
			BaseElement parentElement, BpmnModel model) throws Exception {

		if (parentElement instanceof TemperatureEventDefinition) {
			((TemperatureEventDefinition) parentElement).setCondition(xtr
					.getElementText());
		} else if (parentElement instanceof SmokeEventDefinition) {
			((SmokeEventDefinition) parentElement).setCondition(xtr
					.getElementText());
		}
	}

}
