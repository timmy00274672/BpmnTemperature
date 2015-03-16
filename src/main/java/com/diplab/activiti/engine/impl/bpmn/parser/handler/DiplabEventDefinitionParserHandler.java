package com.diplab.activiti.engine.impl.bpmn.parser.handler;

import java.util.Date;
import java.util.Map;

import org.activiti.bpmn.model.BaseElement;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.bpmn.parser.handler.AbstractBpmnParseHandler;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.diplab.activiti.bpmn.model.DiplabEventDefinition;
import com.diplab.activiti.engine.impl.jobexecutor.TemperatureDeclarationImpl;
import com.diplab.activiti.engine.impl.jobexecutor.TemperatureDeclarationType;
import com.diplab.activiti.temperature.DiplabTemperature;
import com.diplab.activiti.temperature.IsSatisfy;
import com.diplab.activiti.temperature.Temperature;
import com.diplab.activiti.temperature.TemperatureEventListener;
import com.diplab.activiti.temperature.delegate.SchedulerTask;

public class DiplabEventDefinitionParserHandler extends
		AbstractBpmnParseHandler<DiplabEventDefinition> {

	private static final Logger logger = LoggerFactory
			.getLogger(DiplabEventDefinitionParserHandler.class);

	@Override
	protected Class<? extends BaseElement> getHandledType() {
		return DiplabEventDefinition.class;
	}

	@Override
	protected void executeParse(BpmnParse bpmnParse,
			DiplabEventDefinition eventDefinition) {

		/*
		 * 1. prepare activity behavior
		 * 2. prepare TemperatureEventListener
		 * 3. add TemperatureEventListener into scheduler
		 */

		// 1. Behavior: go through next activity
		ActivityImpl temperatureEventActivity = bpmnParse.getCurrentActivity();
		temperatureEventActivity.setActivityBehavior(new ActivityBehavior() {
			private static final long serialVersionUID = 1L;

			@Override
			/**
			 * Just go through the next activity
			 * Only one transition should be taken
			 */
			public void execute(ActivityExecution execution) throws Exception {
				System.out.println("Temperature");
				execution.take(execution.getActivity().getOutgoingTransitions()
						.get(0));

			}
		});

		/*
		 * 2. prepare TemperatureEventListener
		 * 
		 * 2.1 Prepare TemperatureDeclarationType
		 * 2.2 Use TemperatureDeclarationType.prepareIsSatisfy()
		 * 2.3 New TemperatureEventListener with IsSatisfy
		 * and implement activate and isEnd
		 */

		TemperatureDeclarationType type;
		double condition = Double.parseDouble(eventDefinition.getCondition());
		if (eventDefinition.getMode().equalsIgnoreCase("greater")) {
			type = TemperatureDeclarationType.GREATER;
		} else if (eventDefinition.getMode().equalsIgnoreCase("lesser")) {
			type = TemperatureDeclarationType.LESSER;
		} else {
			logger.warn(String.format("%s is not supportted",
					eventDefinition.getMode()));
			return;
		}

		TemperatureDeclarationImpl declarationImpl = new TemperatureDeclarationImpl(
				type, condition);

		IsSatisfy isSatisfy = declarationImpl.prepareIsSatisfy();
		if (isSatisfy == null) {
			// isSatisfy is null -> it's impossible to be activated.
			return;
		}
		final ProcessDefinitionEntity processDefinition = bpmnParse
				.getCurrentProcessDefinition();

		TemperatureEventListener listener = new TemperatureEventListener(
				isSatisfy) {

			@Override
			public void activate(Map<Date, Temperature> records) {
				DiplabTemperature.processEngine.getRuntimeService()
						.startProcessInstanceById(processDefinition.getId());
			}

			@Override
			public boolean isEnd() {
				return false;
			}
		};

		// 3. schedule
		SchedulerTask.addTemperatureEventListener(listener);

	}
}
