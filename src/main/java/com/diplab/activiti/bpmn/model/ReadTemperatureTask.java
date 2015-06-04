package com.diplab.activiti.bpmn.model;

import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Task;

public class ReadTemperatureTask extends Task {

	private String sensorId;

	@Override
	public FlowElement clone() {
		ReadTemperatureTask readTemperatureTask = new ReadTemperatureTask();
		readTemperatureTask.setSensorId(this.sensorId);
		return readTemperatureTask;
	}

	public String getSensorId() {
		return sensorId;
	}

	public void setSensorId(String sensorId) {
		this.sensorId = sensorId;
	}

}
