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
	public String toString() {
		return "SimulateController [deviceId=" + deviceId + "]";
	}

	
}
