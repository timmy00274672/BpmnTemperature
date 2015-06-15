package com.diplab.activiti.engine.impl.bpmn.parser.handler;

import org.activiti.bpmn.model.BaseElement;
import org.activiti.engine.impl.bpmn.behavior.BpmnActivityBehavior;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.bpmn.parser.handler.AbstractBpmnParseHandler;
import org.activiti.engine.impl.pvm.process.ActivityImpl;

import com.diplab.activiti.Constant;
import com.diplab.activiti.bpmn.model.SwitchTask;
import com.diplab.device.swtich.SwitchController;

public class SwitchTaskParserHandler extends
		AbstractBpmnParseHandler<SwitchTask> {

	@Override
	protected void executeParse(BpmnParse bpmnParse, SwitchTask task) {
		ActivityImpl activity = createActivityOnCurrentScope(bpmnParse, task,
				Constant.ELEMENT_TASK_READTEMP);
		activity.setProperty("switch_device_id", task.getDevice_id());
		activity.setProperty("switch_mode", task.getMode());

		activity.setActivityBehavior((execution) -> {
			String device = (String) execution.getActivity().getProperty(
					"switch_device_id");
			SwitchController controller = SwitchController
					.getControllerByDeviceId(device);
			if (controller == null)
				throw new RuntimeException(String.format(
						"cannot find controller module for device[%s]", device));
			String mode = (String) execution.getActivity().getProperty(
					"switch_mode");
			switch (mode.toLowerCase()) {
			case "on":
				controller.on();
				break;
			case "off":
				controller.off();
				break;
			case "toggle":
				controller.toggle();
				break;
			default:
				throw new UnsupportedOperationException(String.format(
						"device[%s] doesn't support %s command", device, mode));
			}
			BpmnActivityBehavior bpmnActivityBehavior = new BpmnActivityBehavior();
			bpmnActivityBehavior.performDefaultOutgoingBehavior(execution);
		});

	}

	@Override
	protected Class<? extends BaseElement> getHandledType() {
		return SwitchTask.class;
	}

}
