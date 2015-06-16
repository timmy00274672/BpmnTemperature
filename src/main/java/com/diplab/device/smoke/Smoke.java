package com.diplab.device.smoke;

import java.io.Serializable;
import java.util.Date;

public class Smoke implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 643084574412312280L;

	private double smoke;

	private Date time = new Date();

	public double getSmoke() {
		return smoke;
	}

	public Date getTime() {
		return time;
	}

	public void setSmoke(double smoke) {
		this.smoke = smoke;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return String.format("%s S=%-10fppm", time.toString(), smoke);
	}

}
