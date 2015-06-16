package com.diplab.device.smoke;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class SmokeReceiver {
	static public Map<String, SmokeReceiver> receivers = new HashMap<>();

	static public void addReceiver(SmokeReceiver receiver) {
		receivers.put(receiver.getSmokeDeviceId(), receiver);
	}

	static public SmokeReceiver getReceiverByDeviceId(String id) {
		return receivers.get(id);
	}

	public LinkedList<Smoke> smokes = new LinkedList<>();

	public abstract String getSmokeDeviceId();

	public List<Smoke> getSmokes() {
		smokes.addFirst(getSmoke());
		return smokes;
	}

	protected abstract Smoke getSmoke();
}
