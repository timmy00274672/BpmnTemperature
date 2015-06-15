package com.diplab.activiti.bpmn.converter.child;

import javax.xml.stream.XMLStreamReader;

import org.activiti.bpmn.converter.child.BaseChildElementParser;
import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.BpmnModel;

import com.diplab.activiti.Constant;
import com.diplab.activiti.bpmn.model.ReadTemperatureTask;
import com.diplab.activiti.bpmn.model.SmokeEventDefinition;
import com.diplab.activiti.bpmn.model.TemperatureEventDefinition;

// TODO change id -> sensor_id
public class IdParser extends BaseChildElementParser {

	@Override
	public String getElementName() {
		return Constant.ATTRIBUTE_ID;
	}

	@Override
	public void parseChildElement(XMLStreamReader xtr,
			BaseElement parentElement, BpmnModel model) throws Exception {

		if (parentElement instanceof TemperatureEventDefinition) {
			((TemperatureEventDefinition) parentElement).setSensorId(xtr
					.getElementText());
		} else if (parentElement instanceof SmokeEventDefinition) {
			((SmokeEventDefinition) parentElement).setSensorId(xtr
					.getElementText());
		} else if (parentElement instanceof ReadTemperatureTask) {
			((ReadTemperatureTask) parentElement).setSensorId(xtr
					.getElementText());
		}
	}

}
