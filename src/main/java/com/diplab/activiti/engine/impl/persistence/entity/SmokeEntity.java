package com.diplab.activiti.engine.impl.persistence.entity;

import java.util.Calendar;

import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ByteArrayRef;
import org.activiti.engine.impl.persistence.entity.JobEntity;
import org.springframework.util.SerializationUtils;

import com.diplab.activiti.engine.impl.jobexecutor.SmokeDeclarationImpl;
import com.diplab.device.RecordMode;

public class SmokeEntity extends JobEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1141109910395127213L;

	private static final int INTERVAL_OF_SCANNING = 10;

	private double condition;

	private RecordMode mode;
	private String sensorId;
	private String self;
	private int time;

	public SmokeEntity() {

	}

	public SmokeEntity(SmokeDeclarationImpl declaration,
			String processDefinitionId) {

		jobHandlerType = declaration.getJobHandlerType();
		condition = declaration.getCondition();
		mode = declaration.getMode();
		sensorId = declaration.getSensorId();
		time = declaration.getTime();
		Calendar instance = Calendar.getInstance();
		instance.add(Calendar.SECOND, INTERVAL_OF_SCANNING);
		duedate = instance.getTime();
		this.processDefinitionId = processDefinitionId;

	}

	public SmokeEntity(SmokeEntity entity) {
		this.jobHandlerType = entity.jobHandlerType;
		this.condition = entity.condition;
		this.mode = entity.mode;
		this.sensorId = entity.sensorId;
		this.processDefinitionId = entity.processDefinitionId;

		Calendar instance = Calendar.getInstance();
		instance.add(Calendar.SECOND, INTERVAL_OF_SCANNING);
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

		// TODO: other action
		super.execute(commandContext);
		delete();
		schedule(self);
	}

	public double getCondition() {
		return condition;
	}

	public RecordMode getMode() {
		return mode;
	}

	public String getSelf() {
		return self;
	}

	public String getSensorId() {
		return sensorId;
	}

	public int getTimes() {
		return time;
	}

	@Override
	public void insert() {
		// store self into BYTE_ARRAY, because extends the job table is dirty
		// work
		// store the id into self which will be mapped on the job table

		ByteArrayRef ref = new ByteArrayRef();
		ref.setValue("self", SerializationUtils.serialize(this));

		setSelf(ref.getId());

		super.insert();
	}

	public void setCondition(double condition) {
		this.condition = condition;
	}

	public void setMode(RecordMode mode) {
		this.mode = mode;
	}

	public void setSelf(String self) {
		this.self = self;
	}

	public void setSensorId(String sensor_id) {
		this.sensorId = sensor_id;
	}

	public void setTimes(int times) {
		this.time = times;
	}

	@Override
	public String toString() {
		return "SmokeEntity [condition=" + condition + ", mode=" + mode
				+ ", sensorId=" + sensorId + ", self=" + self + ", times="
				+ time + ", toString()=" + super.toString() + "]";
	}

	private void schedule(String self2) {
		ByteArrayRef ref = new ByteArrayRef(self2);
		SmokeEntity entity = (SmokeEntity) SerializationUtils.deserialize(ref
				.getBytes());
		entity = new SmokeEntity(entity);
		entity.insert();

	}
}
