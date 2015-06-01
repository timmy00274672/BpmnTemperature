package com.diplab.activiti;

import java.util.HashMap;
import java.util.Map;

import org.activiti.bpmn.converter.child.BaseChildElementParser;

import com.diplab.activiti.bpmn.converter.child.ConditionParser;
import com.diplab.activiti.bpmn.converter.child.IdParser;
import com.diplab.activiti.bpmn.converter.child.ModeParser;
import com.diplab.activiti.bpmn.converter.child.TemperatureEventDefinitionParser;

public class Constant {
	public enum TemperatureMode {
		Greater("greater"), LESSER("lesser"), EQUAL("equal"), NONE("");

		public final String calendarName;

		TemperatureMode(String caledarName) {
			this.calendarName = caledarName;
		}
	}

	public static final String ATTRIBUTE_MODE = "mode";
	public static final String ATTRIBUTE_CONDITION = "condition";
	public static final String ATTRIBUTE_ID = "id";

	public static final String ELEMENT_TEMPERATURE_EVENT_DEFINITION = "temperatureEventDefinition";
	public static final String PROPERTYNAME_START_TEMP = "property-start-temp"; // List<TemperatureDeclarationImpl>

	private Constant() {
	}

	public static Map<String, BaseChildElementParser> DIP_PARSER;

	static {
		DIP_PARSER = new HashMap<String, BaseChildElementParser>();
		BaseChildElementParser[] parsers = new BaseChildElementParser[] {
				new TemperatureEventDefinitionParser(), new ModeParser(),
				new ConditionParser(), new IdParser() };

		for (BaseChildElementParser parser : parsers) {
			DIP_PARSER.put(parser.getElementName(), parser);
		}
	}

}
