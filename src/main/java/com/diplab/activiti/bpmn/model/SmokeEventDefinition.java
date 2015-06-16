package com.diplab.activiti.bpmn.model;

import org.activiti.bpmn.model.EventDefinition;

public class SmokeEventDefinition extends EventDefinition {

	protected String mode;
	protected String condition; // ?ppm
	protected String time = "10";// defautl : 10s
	protected String sensor_id; // indicate which device

	@Override
	public EventDefinition clone() {
		SmokeEventDefinition clone = new SmokeEventDefinition();
		clone.setValues(this);
		return null;
	}

	public String getCondition() {
		return condition;
	}

	public String getMode() {
		return mode;
	}

	public String getSensorId() {
		return sensor_id;
	}

	public String getTime() {
		return time;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public void setSensorId(String id) {
		this.sensor_id = id;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public void setValues(SmokeEventDefinition otherDefinition) {
		super.setValues(otherDefinition);
		setMode(otherDefinition.getMode());
		setCondition(otherDefinition.getCondition());
		setTime(otherDefinition.getTime());
		setSensorId(otherDefinition.getSensorId());
	}

	@Override
	public String toString() {
		return "SmokeEventDefinition [mode=" + mode + ", condition="
				+ condition + ", time=" + time + ", sensor_id=" + sensor_id
				+ "]";
	}

}
