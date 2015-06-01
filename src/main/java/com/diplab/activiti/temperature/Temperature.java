package com.diplab.activiti.temperature;

import java.io.Serializable;
import java.util.Date;

public class Temperature implements Serializable {

	private static final long serialVersionUID = -349485433882229849L;
	private double temperature;
	private Date time = new Date();

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return String.format("%s T=%f", time.toString(), temperature);
	}
}
