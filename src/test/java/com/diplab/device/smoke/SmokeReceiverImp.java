package com.diplab.device.smoke;

import java.util.Random;

public class SmokeReceiverImp extends SmokeReceiver {

	private String id;

	public SmokeReceiverImp() {
	}

	public SmokeReceiverImp(String id) {
		this.id = id;
	}

	@Override
	public Smoke getSmoke() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Smoke smoke = new Smoke();
		smoke.setSmoke(new Random().nextInt(10000));

		return smoke;
	}

	@Override
	public String getSmokeDeviceId() {
		return id;
	}
}
