package com.diplab.activiti.engine.impl.bpmn.parser.handler;

import org.activiti.bpmn.model.BaseElement;
import org.activiti.engine.impl.bpmn.behavior.BpmnActivityBehavior;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.bpmn.parser.handler.AbstractBpmnParseHandler;
import org.activiti.engine.impl.pvm.process.ActivityImpl;

import com.diplab.activiti.Constant;
import com.diplab.activiti.bpmn.model.ReadTemperatureTask;
import com.diplab.device.temperature.TemperatureReceiver;

public class ReadTemperatureTaskParserHandler extends
		AbstractBpmnParseHandler<ReadTemperatureTask> {
	// private static final Logger LOG = LoggerFactory
	// .getLogger(ReadTemperatureTaskParserHandler.class);

	@Override
	protected void executeParse(BpmnParse bpmnParse, ReadTemperatureTask task) {
		ActivityImpl activity = createActivityOnCurrentScope(bpmnParse, task,
				Constant.ELEMENT_TASK_READTEMP);
		activity.setActivityBehavior((execution) -> {
			execution.setVariable("temperatures", TemperatureReceiver
					.getReceiverByDeviceId(task.getSensorId())
					.getTemperatures());
			BpmnActivityBehavior bpmnActivityBehavior = new BpmnActivityBehavior();
			bpmnActivityBehavior.performDefaultOutgoingBehavior(execution);
		});

	}

	@Override
	protected Class<? extends BaseElement> getHandledType() {
		return ReadTemperatureTask.class;
	}

}
