package com.diplab.device.swtich;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.RaspiPin;

public class RpiTrunLightController extends SwitchController {

	GpioPinDigitalOutput pin;

	public RpiTrunLightController() {
		GpioController gpio = GpioFactory.getInstance();
		// provision gpio pin #01 as an output pin and turn on
		pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01);
	}

	@Override
	public String getDeviceId() {
		return "light_on_rpi";
	}

	@Override
	public void off() {
		pin.low();
		System.out.println("device should be off");
	}

	@Override
	public void on() {
		pin.high();
		System.out.println("device should be on");
	}

}
