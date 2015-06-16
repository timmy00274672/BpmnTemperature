package com.diplab.device;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.function.ToDoubleFunction;

import com.diplab.device.RecordMode;

public interface IsSatisfy<T> {
	public static final double TOLERANCE = 0.1;

	public static <T> IsSatisfy<T> prepareIsSatisfy(RecordMode mode, int time,
			double condition, ToDoubleFunction<? super T> todouble) {
		switch (mode) {
		case EQUAL:
			return (recordsOfT) -> {
				if (recordsOfT.size() < time)
					return false;
				DoubleSummaryStatistics summaryStatistics = recordsOfT.stream()
						.mapToDouble(todouble).summaryStatistics();
				if (summaryStatistics.getCount() < time)
					return false;

				return Math.abs(summaryStatistics.getAverage() - condition) < TOLERANCE;

			};
		case Greater:
			return (recordsOfT) -> {
				if (recordsOfT.size() < time)
					return false;
				DoubleSummaryStatistics summaryStatistics = recordsOfT.stream()
						.mapToDouble(todouble).summaryStatistics();
				if (summaryStatistics.getCount() < time)
					return false;

				return summaryStatistics.getAverage() - condition > 0;

			};
		case LESSER:
			return (recordsOfT) -> {
				if (recordsOfT.size() < time)
					return false;
				DoubleSummaryStatistics summaryStatistics = recordsOfT.stream()
						.mapToDouble(todouble).summaryStatistics();
				if (summaryStatistics.getCount() < time)
					return false;

				return summaryStatistics.getAverage() - condition < 0;

			};
		case NONE:
			return (x) -> {
				return true;
			};
		default:
			return null;
		}
	}

	// Assume the first element is the newest (order by time)
	public boolean isSatisfy(List<T> records);
}