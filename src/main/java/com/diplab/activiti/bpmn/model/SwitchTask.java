package com.diplab.activiti.bpmn.model;

import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Task;

public class SwitchTask extends Task {
	private String device_id;

	private String mode;

	@Override
	public FlowElement clone() {
		SwitchTask task = new SwitchTask();
		task.device_id = device_id;
		task.mode = mode;
		return task;
	}

	public String getDevice_id() {
		return device_id;
	}

	public String getMode() {
		return mode;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	@Override
	public String toString() {
		return "SwitchTask [device_id=" + device_id + ", mode=" + mode
				+ ", name=" + name + ", id=" + id + "]";
	}

}
