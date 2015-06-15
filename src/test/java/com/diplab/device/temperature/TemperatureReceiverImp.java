package com.diplab.device.temperature;

import com.diplab.device.temperature.Temperature;
import com.diplab.device.temperature.TemperatureReceiver;

public class TemperatureReceiverImp extends TemperatureReceiver {

	private String id = "TEMP-S-01";

	public TemperatureReceiverImp() {
	}

	public TemperatureReceiverImp(String id) {
		this.id = id;
	}

	public Temperature getTemperature() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Temperature temperature = new Temperature();
		temperature.setTemperature(Math.random() * 50);

		return temperature;
	}

	@Override
	public String getTemperatureDeviceId() {
		return id;
	}
}
