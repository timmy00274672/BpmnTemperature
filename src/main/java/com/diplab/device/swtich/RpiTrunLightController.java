package com.diplab.device.swtich;

public class RpiTrunLightController extends SwitchController {
	
	static{
		System.loadLibrary("device"); //libdevice.so
	}
	
	@Override
	public String getDeviceId() {
		return "light_on_rpi";
	}

	@Override
	public native void off();
	
	@Override
	public native void on();

}
