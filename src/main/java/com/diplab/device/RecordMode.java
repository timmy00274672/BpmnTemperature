package com.diplab.device;

public enum RecordMode {
	Greater("greater"), LESSER("lesser"), EQUAL("equal"), NONE("NONE");

	public final String calendarName;

	RecordMode(String caledarName) {
		this.calendarName = caledarName;
	}
}