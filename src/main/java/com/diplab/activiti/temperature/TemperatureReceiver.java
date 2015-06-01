package com.diplab.activiti.temperature;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public interface TemperatureReceiver {
	static public List<TemperatureReceiver> receivers = new ArrayList<TemperatureReceiver>();
	static public void addReceiver(TemperatureReceiver receiver) {
		receivers.add(receiver);
	}
	
	static public TemperatureReceiver getReceiverByDeviceId(String id) {
		TemperatureReceiver receiver = null;
		for (Iterator<TemperatureReceiver> iterator = receivers.iterator(); iterator
				.hasNext();) {
			TemperatureReceiver temperatureReceiver = iterator.next();
			if (temperatureReceiver.getTemperatureDeviceId().equalsIgnoreCase(id)) {
				receiver = temperatureReceiver;
			}
		}

		return receiver;
	}

	public Temperature getTemperature();

	public String getTemperatureDeviceId();
}
