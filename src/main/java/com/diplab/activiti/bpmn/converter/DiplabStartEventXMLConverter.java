package com.diplab.activiti.bpmn.converter;

import javax.xml.stream.XMLStreamReader;

import org.activiti.bpmn.converter.StartEventXMLConverter;
import org.activiti.bpmn.converter.util.BpmnXMLUtil;
import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.alfresco.AlfrescoStartEvent;
import org.apache.commons.lang3.StringUtils;

import com.diplab.activiti.Constant;

public class DiplabStartEventXMLConverter extends StartEventXMLConverter {

	@Override
	protected BaseElement convertXMLToElement(XMLStreamReader xtr,
			BpmnModel model) throws Exception {
		String formKey = xtr.getAttributeValue(ACTIVITI_EXTENSIONS_NAMESPACE,
				ATTRIBUTE_FORM_FORMKEY);
		StartEvent startEvent = null;
		if (StringUtils.isNotEmpty(formKey)) {
			if (model.getStartEventFormTypes() != null
					&& model.getStartEventFormTypes().contains(formKey)) {
				startEvent = new AlfrescoStartEvent();
			}
		}
		if (startEvent == null) {
			startEvent = new StartEvent();
		}
		BpmnXMLUtil.addXMLLocation(startEvent, xtr);
		startEvent
				.setInitiator(xtr.getAttributeValue(
						ACTIVITI_EXTENSIONS_NAMESPACE,
						ATTRIBUTE_EVENT_START_INITIATOR));
		startEvent.setFormKey(formKey);

		// Add our own parser > like TemperatureEventDefinitionParser, SmokeEventDefinitionParser
		parseChildElements(getXMLElementName(), startEvent,
				Constant.DIP_PARSER, model, xtr);

		return startEvent;
	}

}