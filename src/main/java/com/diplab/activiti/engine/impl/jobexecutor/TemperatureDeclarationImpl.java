package com.diplab.activiti.engine.impl.jobexecutor;

import java.io.Serializable;

import com.diplab.activiti.Constant.TemperatureMode;

public class TemperatureDeclarationImpl implements Serializable {

	private static final long serialVersionUID = 1L;

	protected double condition;
	protected TemperatureMode mode;
	protected String jobHandlerType;
	protected String sensorId;
	protected int time;

	public TemperatureDeclarationImpl(double condition, TemperatureMode mode,
			String jobHandlerType, String id, int time) {
		super();
		this.condition = condition;
		this.mode = mode;
		this.jobHandlerType = jobHandlerType;
		this.sensorId = id;
		this.time = time;
	}

	public double getCondition() {
		return condition;
	}

	public String getJobHandlerType() {
		return jobHandlerType;
	}

	public TemperatureMode getMode() {
		return mode;
	}

	public String getSensorId() {
		return sensorId;
	}

	public int getTime() {
		return time;
	}

	public void setCondition(double condition) {
		this.condition = condition;
	}

	public void setJobHandlerType(String jobHandlerType) {
		this.jobHandlerType = jobHandlerType;
	}

	public void setMode(TemperatureMode mode) {
		this.mode = mode;
	}

	public void setSensorId(String id) {
		this.sensorId = id;
	}

	public void setTime(int time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "TemperatureDeclarationImpl [condition=" + condition + ", mode="
				+ mode + ", jobHandlerType=" + jobHandlerType + ", sensorId="
				+ sensorId + ", time=" + time + "]";
	}

}
