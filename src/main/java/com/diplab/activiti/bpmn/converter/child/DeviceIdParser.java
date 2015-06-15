package com.diplab.activiti.bpmn.converter.child;

import javax.xml.stream.XMLStreamReader;

import org.activiti.bpmn.converter.child.BaseChildElementParser;
import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.BpmnModel;

import com.diplab.activiti.Constant;
import com.diplab.activiti.bpmn.model.SwitchTask;

public class DeviceIdParser extends BaseChildElementParser {

	@Override
	public String getElementName() {
		return Constant.ATTRIBUTE_DEVICE_ID;
	}

	@Override
	public void parseChildElement(XMLStreamReader xtr,
			BaseElement parentElement, BpmnModel model) throws Exception {

		if (parentElement instanceof SwitchTask) {
			((SwitchTask) parentElement).setDevice_id(xtr.getElementText());
		}
	}

}
