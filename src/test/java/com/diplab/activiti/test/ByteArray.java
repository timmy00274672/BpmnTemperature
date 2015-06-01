package com.diplab.activiti.test;

import java.io.Serializable;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ByteArrayRef;
import org.springframework.util.SerializationUtils;

import com.diplab.activiti.engine.impl.cfg.DipProcessEngineConfiguration;

public class ByteArray {

	public static void main(String[] args) throws InterruptedException {
		ProcessEngineConfigurationImpl config = new DipProcessEngineConfiguration();
		config.setJdbcUrl("jdbc:h2:tcp://localhost/Activiti");
		// config.setDatabaseSchemaUpdate("drop-create");
		// config.setJobExecutorActivate(true);

		final ProcessEngine processEngine = config.buildProcessEngine();

		processEngine.getManagementService().executeCommand(
				new Command<Void>() {

					@Override
					public Void execute(CommandContext commandContext) {
						ByteArrayRef byteArrayRef = new ByteArrayRef();
						byteArrayRef.setValue("name", SerializationUtils
								.serialize(new Temperature("HI")));
						System.out.println(byteArrayRef.getId());

						ByteArrayRef byteArrayRef2 = new ByteArrayRef(
								byteArrayRef.getId());
						System.out.println(SerializationUtils
								.deserialize(byteArrayRef2.getBytes()));

						return null;
					}
				});

	}

	static class Temperature implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 3430156490697045617L;
		String name;

		@Override
		public String toString() {
			return "Temperature [name=" + name + "]";
		}

		public Temperature(String name) {
			super();
			this.name = name;
		}
	}

}
