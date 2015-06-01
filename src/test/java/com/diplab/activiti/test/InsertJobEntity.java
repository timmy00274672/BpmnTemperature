package com.diplab.activiti.test;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;

import com.diplab.activiti.Constant.TemperatureMode;
import com.diplab.activiti.engine.impl.cfg.DipProcessEngineConfiguration;
import com.diplab.activiti.engine.impl.jobexecutor.TemperatureDeclarationImpl;
import com.diplab.activiti.engine.impl.jobexecutor.TemperatureStartEventJobHandler;
import com.diplab.activiti.engine.impl.persistence.entity.TemperatureEntity;

public class InsertJobEntity {

	public static void main(String[] args) throws InterruptedException {
		ProcessEngineConfigurationImpl config = new DipProcessEngineConfiguration();
		config.setJdbcUrl("jdbc:h2:tcp://localhost/Activiti");
		// config.setDatabaseSchemaUpdate("drop-create");
		config.setJobExecutorActivate(true);

		final ProcessEngine processEngine = config.buildProcessEngine();

		processEngine.getManagementService().executeCommand(
				new Command<Void>() {

					@Override
					public Void execute(CommandContext commandContext) {
						TemperatureDeclarationImpl declaration = new TemperatureDeclarationImpl(
								23, TemperatureMode.EQUAL,
								TemperatureStartEventJobHandler.TYPE, "2");
						Context.getCommandContext()
								.getDbSqlSession()
								.insert(new TemperatureEntity(declaration, null));
						return null;
					}
				});

	}

}
