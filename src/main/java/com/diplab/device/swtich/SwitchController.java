package com.diplab.device.swtich;

import java.util.HashMap;
import java.util.Map;

public abstract class SwitchController {

	static private Map<String, SwitchController> controllers = new HashMap<>();

	static public void addController(SwitchController receiver) {
		controllers.put(receiver.getDeviceId(), receiver);
	}

	static public SwitchController getControllerByDeviceId(String id) {
		return controllers.get(id);
	}

	public abstract String getDeviceId();

	public abstract void off();

	public abstract void on();

	public abstract void toggle() throws UnsupportedOperationException;
}