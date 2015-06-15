package com.diplab.device.temperature;

import java.util.List;

import com.diplab.device.RecordMode;

public interface IsSatisfy {
	// Assume the first element is the newest (order by time)
	public boolean isSatisfy(List<Temperature> temperatures);

	public static final double TOLERANCE = 0.1;

	public static IsSatisfy prepareIsSatisfy(RecordMode mode, int time,
			double condition) {
		switch (mode) {
		case EQUAL:
			return (x) -> {
				double sum = 0;
				int numOfRecorder = 0;
				if (x.size() < time)
					return false;
				for (Temperature temperature : x) {
					numOfRecorder++;
					sum += temperature.getTemperature();
					if (numOfRecorder == time)
						break;
				}
				if (numOfRecorder == 0)
					return false;
				sum = sum / numOfRecorder;
				return Math.abs(sum - condition) < TOLERANCE;

			};
		case Greater:
			return (x) -> {
				double sum = 0;
				int numOfRecorder = 0;
				if (x.size() < time)
					return false;
				for (Temperature temperature : x) {
					numOfRecorder++;
					sum += temperature.getTemperature();
					if (numOfRecorder == time)
						break;
				}
				if (numOfRecorder == 0)
					return false;
				sum = sum / numOfRecorder;
				return sum - condition > 0;
			};
		case LESSER:
			return (x) -> {
				double sum = 0;
				int numOfRecorder = 0;
				if (x.size() < time)
					return false;
				for (Temperature temperature : x) {
					numOfRecorder++;
					sum += temperature.getTemperature();
					if (numOfRecorder == time)
						break;
				}
				if (numOfRecorder == 0)
					return false;
				sum = sum / numOfRecorder;
				return sum - condition < 0;
			};
		case NONE:
			return (x) -> {
				return true;
			};
		default:
			return null;
		}
	}
}
