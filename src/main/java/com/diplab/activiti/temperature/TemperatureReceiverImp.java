package com.diplab.activiti.temperature;

public class TemperatureReceiverImp implements TemperatureReceiver {

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
