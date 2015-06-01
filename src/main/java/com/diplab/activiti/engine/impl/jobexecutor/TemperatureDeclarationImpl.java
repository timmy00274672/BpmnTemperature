package com.diplab.activiti.engine.impl.jobexecutor;

import java.io.Serializable;

import com.diplab.activiti.Constant.TemperatureMode;

public class TemperatureDeclarationImpl implements Serializable {

	private static final long serialVersionUID = 1L;

	protected double condition;

	protected TemperatureMode mode;

	protected String jobHandlerType;

	protected String id;

	public double getCondition() {
		return condition;
	}

	public void setCondition(double condition) {
		this.condition = condition;
	}

	public TemperatureMode getMode() {
		return mode;
	}

	public void setMode(TemperatureMode mode) {
		this.mode = mode;
	}

	public void setJobHandlerType(String jobHandlerType) {
		this.jobHandlerType = jobHandlerType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public TemperatureDeclarationImpl(double condition, TemperatureMode mode,
			String jobHandlerType, String id) {
		super();
		this.condition = condition;
		this.mode = mode;
		this.jobHandlerType = jobHandlerType;
		this.id = id;
	}

	public String getJobHandlerType() {
		return jobHandlerType;
	}

	@Override
	public String toString() {
		return "TemperatureDeclarationImpl [condition=" + condition + ", mode="
				+ mode + ", jobHandlerType=" + jobHandlerType + ", id=" + id
				+ "]";
	}

}
