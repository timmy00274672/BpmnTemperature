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
	private String sensorId;
	private String self;

	public TemperatureEntity() {

	}

	public TemperatureEntity(TemperatureDeclarationImpl declaration,
			String processDefinitionId) {

		jobHandlerType = declaration.getJobHandlerType();
		condition = declaration.getCondition();
		mode = declaration.getMode();
		sensorId = declaration.getSensorId();
		Calendar instance = Calendar.getInstance();
		instance.add(Calendar.SECOND, 10);
		duedate = instance.getTime();
		this.processDefinitionId = processDefinitionId;

	}

	public TemperatureEntity(TemperatureEntity entity) {
		this.jobHandlerType = entity.jobHandlerType;
		this.condition = entity.condition;
		this.mode = entity.mode;
		this.sensorId = entity.sensorId;
		this.processDefinitionId = entity.processDefinitionId;

		Calendar instance = Calendar.getInstance();
		instance.add(Calendar.SECOND, 10);
		this.duedate = instance.getTime();
	}

	@Override
	public void delete() {
		super.delete();
		ByteArrayRef ref = new ByteArrayRef(self);
		ref.delete();
	}

	@Override
	public void execute(CommandContext commandContext) {
		// 1. call the job handler
		// 2. delete the touple of data both in Job and ByteArray tables.
		// 3. re-schedule the job (the default action)
		
		//TODO: other action
		super.execute(commandContext);
		delete();
		schedule(self);
	}

	public double getCondition() {
		return condition;
	}

	public TemperatureMode getMode() {
		return mode;
	}

	public String getSelf() {
		return self;
	}

	public String getSensorId() {
		return sensorId;
	}

	@Override
	public void insert() {
		//store self into BYTE_ARRAY, because extends the job table is dirty work
		//store the id into self which will be mapped on the job table
		
		ByteArrayRef ref = new ByteArrayRef();
		ref.setValue("self", SerializationUtils.serialize(this));
		
		setSelf(ref.getId());
		
		super.insert();
	}

	private void schedule(String self2) {
		ByteArrayRef ref = new ByteArrayRef(self2);
		TemperatureEntity entity = (TemperatureEntity) SerializationUtils
				.deserialize(ref.getBytes());
		entity = new TemperatureEntity(entity);
		entity.insert();

	}

	public void setCondition(double condition) {
		this.condition = condition;
	}

	public void setMode(TemperatureMode mode) {
		this.mode = mode;
	}

	public void setSelf(String self) {
		this.self = self;
	}

	public void setSensorId(String sensor_id) {
		this.sensorId = sensor_id;
	}

}
