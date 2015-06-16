package com.diplab.activiti;

import java.util.HashMap;
import java.util.Map;

import org.activiti.bpmn.converter.child.BaseChildElementParser;

import com.diplab.activiti.bpmn.converter.child.ConditionParser;
import com.diplab.activiti.bpmn.converter.child.DeviceIdParser;
import com.diplab.activiti.bpmn.converter.child.IdParser;
import com.diplab.activiti.bpmn.converter.child.ModeParser;
import com.diplab.activiti.bpmn.converter.child.SmokeEventDefinitionParser;
import com.diplab.activiti.bpmn.converter.child.TemperatureEventDefinitionParser;
import com.diplab.activiti.bpmn.converter.child.TimeParser;

public class Constant {

	public static final String ATTRIBUTE_MODE = "mode";
	public static final String ATTRIBUTE_CONDITION = "condition";
	public static final String ATTRIBUTE_ID = "id";
	public static final String ATTRIBUTE_TIME = "time";
	public static final String ATTRIBUTE_DEVICE_ID = "device_id";

	public static final String ELEMENT_TEMPERATURE_EVENT_DEFINITION = "temperatureEventDefinition";
	public static final String ELEMENT_SMOKE_EVENT_DEFINITION = "smokeEventDefinition";
	public static final String ELEMENT_TASK_READTEMP = "reatTemperatureTask";
	public static final String ELEMENT_TASK_SWITCH = "switchTask";

	public static final String PROPERTYNAME_START_TEMP = "property-start-temp"; // List<TemperatureDeclarationImpl>
	public static final String PROPERTYNAME_START_SMOKE = "property-start-smoke"; // List<SmokeDeclarationImpl>

	public static Map<String, BaseChildElementParser> DIP_PARSER;

	static {
		DIP_PARSER = new HashMap<String, BaseChildElementParser>();
		BaseChildElementParser[] parsers = new BaseChildElementParser[] {
				new TemperatureEventDefinitionParser(),
				new SmokeEventDefinitionParser(), new ModeParser(),
				new ConditionParser(), new IdParser(), new TimeParser(),
				new DeviceIdParser() };

		for (BaseChildElementParser parser : parsers) {
			DIP_PARSER.put(parser.getElementName(), parser);
		}
	}

	private Constant() {
	}

}
