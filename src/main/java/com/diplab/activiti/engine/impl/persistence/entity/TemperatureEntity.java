package com.diplab.activiti.engine.impl.persistence.entity;

import java.util.Calendar;

import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ByteArrayRef;
import org.activiti.engine.impl.persistence.entity.JobEntity;
import org.springframework.util.SerializationUtils;

import com.diplab.activiti.Constant.TemperatureMode;
import com.diplab.activiti.engine.impl.jobexecutor.TemperatureDeclarationImpl;

public class TemperatureEntity extends JobEntity {

	// private Logger logger = LoggerFactory.getLogger(TemperatureEntity.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = -252628523662521870L;

	private double condition;
	private TemperatureMode mode;
	private String sensor_id;
	private String self;

	public String getSelf() {
		return self;
	}

	public void setSelf(String self) {
		this.self = self;
	}

	public double getCondition() {
		return condition;
	}

	public void setCondition(double condition) {
		this.condition = condition;
	}

	public TemperatureMode getMode() {
		return mode;
	}

	public void setMode(TemperatureMode mode) {
		this.mode = mode;
	}

	public String getSensor_id() {
		return sensor_id;
	}

	public void setSensor_id(String sensor_id) {
		this.sensor_id = sensor_id;
	}

	@Override
	public void insert() {
		ByteArrayRef ref = new ByteArrayRef();
		ref.setValue("self", SerializationUtils.serialize(this));
		
		setSelf(ref.getId());
		
		super.insert();
	}

	public TemperatureEntity(TemperatureDeclarationImpl declaration,
			String processDefinitionId) {

		jobHandlerType = declaration.getJobHandlerType();
		condition = declaration.getCondition();
		mode = declaration.getMode();
		sensor_id = declaration.getId();
		Calendar instance = Calendar.getInstance();
		instance.add(Calendar.SECOND, 10);
		duedate = instance.getTime();
		this.processDefinitionId = processDefinitionId;

	}

	public TemperatureEntity(TemperatureEntity entity) {
		this.jobHandlerType = entity.jobHandlerType;
		this.condition = entity.condition;
		this.mode = entity.mode;
		this.sensor_id = entity.sensor_id;
		this.processDefinitionId = entity.processDefinitionId;

		Calendar instance = Calendar.getInstance();
		instance.add(Calendar.SECOND, 10);
		this.duedate = instance.getTime();
	}

	public TemperatureEntity() {

	}

	@Override
	public void execute(CommandContext commandContext) {
		// TODO Auto-generated method stub
		super.execute(commandContext);
		delete();
		schedule(self);
	}

	@Override
	public void delete() {
		super.delete();
		ByteArrayRef ref = new ByteArrayRef(self);
		ref.delete();
	}

	private void schedule(String self2) {
		ByteArrayRef ref = new ByteArrayRef(self2);
		TemperatureEntity entity = (TemperatureEntity) SerializationUtils
				.deserialize(ref.getBytes());
		entity = new TemperatureEntity(entity);
		entity.insert();

	}

}
