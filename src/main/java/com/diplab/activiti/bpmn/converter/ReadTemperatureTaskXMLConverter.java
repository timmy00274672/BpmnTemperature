package com.diplab.activiti.bpmn.converter;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.activiti.bpmn.converter.BaseBpmnXMLConverter;
import org.activiti.bpmn.converter.util.BpmnXMLUtil;
import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.alfresco.AlfrescoStartEvent;
import org.apache.commons.lang3.StringUtils;

import com.diplab.activiti.Constant;
import com.diplab.activiti.bpmn.model.ReadTemperatureTask;

public class ReadTemperatureTaskXMLConverter extends BaseBpmnXMLConverter {

	@Override
	protected Class<? extends BaseElement> getBpmnElementType() {
		return ReadTemperatureTask.class;
	}

	@Override
	protected BaseElement convertXMLToElement(XMLStreamReader xtr,
			BpmnModel model) throws Exception {
		ReadTemperatureTask readTemperatureTask = null;
		readTemperatureTask = new ReadTemperatureTask();

		BpmnXMLUtil.addXMLLocation(readTemperatureTask, xtr);

		// Add our own parser > like idParser
		parseChildElements(getXMLElementName(), readTemperatureTask,
				Constant.DIP_PARSER, model, xtr);

		return readTemperatureTask;
	}

	@Override
	protected String getXMLElementName() {
		return Constant.ELEMENT_TASK_READTEMP;
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