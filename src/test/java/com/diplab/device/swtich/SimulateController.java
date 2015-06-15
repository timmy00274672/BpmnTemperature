package com.diplab.device.swtich;

public class SimulateController extends SwitchController {

	private String deviceId;

	public SimulateController(String deviceId) {
		super();
		this.deviceId = deviceId;
	}

	@Override
	public String getDeviceId() {
		return deviceId;
	}

	@Override
	public void off() {
		System.out.format("device[%s]: off%n", deviceId);
	}

	@Override
	public void on() {
		System.out.format("device[%s]: on%n", deviceId);
	}

	@Override
	public void toggle() throws UnsupportedOperationException {
		throw new UnsupportedOperationException(String.format(
				"device[%s] doesn't support toggle command", deviceId));
	}
}
