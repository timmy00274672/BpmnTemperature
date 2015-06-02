package com.diplab.activiti.temperature;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public abstract class TemperatureReceiver {
	static public List<TemperatureReceiver> receivers = new ArrayList<TemperatureReceiver>();

	static public void addReceiver(TemperatureReceiver receiver) {
		receivers.add(receiver);
	}

	static public TemperatureReceiver getReceiverByDeviceId(String id) {
		TemperatureReceiver receiver = null;
		for (Iterator<TemperatureReceiver> iterator = receivers.iterator(); iterator
				.hasNext();) {
			TemperatureReceiver temperatureReceiver = iterator.next();
			if (temperatureReceiver.getTemperatureDeviceId().equalsIgnoreCase(
					id)) {
				receiver = temperatureReceiver;
			}
		}

		return receiver;
	}

	protected LinkedList<Temperature> temperatures = new LinkedList<>();
	
	protected abstract Temperature getTemperature();

	public abstract String getTemperatureDeviceId();

	public List<Temperature> getTemperatures(){
		temperatures.addFirst(getTemperature());
		return temperatures;
	}
}
