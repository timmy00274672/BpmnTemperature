package com.diplab.activiti.temperature;

import com.diplab.activiti.temperature.Temperature;
import com.diplab.activiti.temperature.TemperatureReceiver;

public class TemperatureReceiverImp implements TemperatureReceiver {
	public Temperature getTemperature() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return new Temperature(Math.random() * (45 - 30 + 1) + 30);
	}
}
