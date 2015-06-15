package com.diplab.activiti.bpmn.converter;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.activiti.bpmn.converter.BaseBpmnXMLConverter;
import org.activiti.bpmn.converter.util.BpmnXMLUtil;
import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.BpmnModel;

import com.diplab.activiti.Constant;
import com.diplab.activiti.bpmn.model.SwitchTask;

public class SwitchTaskXMLConverter extends BaseBpmnXMLConverter {

	@Override
	protected BaseElement convertXMLToElement(XMLStreamReader xtr,
			BpmnModel model) throws Exception {
		SwitchTask switchTask = new SwitchTask();

		BpmnXMLUtil.addXMLLocation(switchTask, xtr);

		// Add our own parser > like idParser
		parseChildElements(getXMLElementName(), switchTask,
				Constant.DIP_PARSER, model, xtr);

		return switchTask;
	}

	@Override
	protected Class<? extends BaseElement> getBpmnElementType() {
		return SwitchTask.class;
	}

	@Override
	protected String getXMLElementName() {
		return Constant.ELEMENT_TASK_SWITCH;
	}

	@Override
	protected void writeAdditionalAttributes(BaseElement element,
			BpmnModel model, XMLStreamWriter xtw) throws Exception {

	}

	@Override
	protected void writeAdditionalChildElements(BaseElement element,
			BpmnModel model, XMLStreamWriter xtw) throws Exception {

	}

}