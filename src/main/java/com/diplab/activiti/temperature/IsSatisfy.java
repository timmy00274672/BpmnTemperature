package com.diplab.activiti.temperature;

import java.util.List;

public interface IsSatisfy {
	//Assume the first element is the newest (order by time)
	public boolean isSatisfy(List<Temperature> temperatures);
}
