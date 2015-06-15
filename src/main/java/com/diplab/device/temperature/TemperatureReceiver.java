package com.diplab.device.temperature;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class TemperatureReceiver {
	static public Map<String, TemperatureReceiver> receivers = new HashMap<>();

	static public void addReceiver(TemperatureReceiver receiver) {
		receivers.put(receiver.getTemperatureDeviceId(), receiver);
	}

	static public TemperatureReceiver getReceiverByDeviceId(String id) {
		return receivers.get(id);
	}

	public LinkedList<Temperature> temperatures = new LinkedList<>();

	public abstract Temperature getTemperature();

	public abstract String getTemperatureDeviceId();

	public List<Temperature> getTemperatures() {
		temperatures.addFirst(getTemperature());
		return temperatures;
	}
}
