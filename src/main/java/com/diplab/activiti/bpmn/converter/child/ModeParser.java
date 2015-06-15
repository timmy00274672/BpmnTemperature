package com.diplab.activiti.bpmn.converter.child;

import javax.xml.stream.XMLStreamReader;

import org.activiti.bpmn.converter.child.BaseChildElementParser;
import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.BpmnModel;

import com.diplab.activiti.Constant;
import com.diplab.activiti.bpmn.model.SmokeEventDefinition;
import com.diplab.activiti.bpmn.model.SwitchTask;
import com.diplab.activiti.bpmn.model.TemperatureEventDefinition;

public class ModeParser extends BaseChildElementParser {

	@Override
	public String getElementName() {
		return Constant.ATTRIBUTE_MODE;
	}

	@Override
	public void parseChildElement(XMLStreamReader xtr,
			BaseElement parentElement, BpmnModel model) throws Exception {

		if (parentElement instanceof TemperatureEventDefinition) {
			((TemperatureEventDefinition) parentElement).setMode(xtr
					.getElementText());
		} else if (parentElement instanceof SmokeEventDefinition) {
			((SmokeEventDefinition) parentElement)
					.setMode(xtr.getElementText());
		} else if (parentElement instanceof SwitchTask) {
			((SwitchTask) parentElement).setMode(xtr.getElementText());
		}
	}

}
