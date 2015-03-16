package com.diplab.activiti.engine.impl.cfg;

import java.io.InputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.ProcessEngineImpl;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.impl.db.DbSqlSession;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.util.ReflectUtil;

public class DipProcessEngineConfiguration extends
		StandaloneProcessEngineConfiguration {
	@Override
	public ProcessEngine buildProcessEngine() {
		init();
		// new out tables
		commandExecutor.execute(getSchemaCommandConfig(), new Command<Void>() {

			@Override
			public Void execute(CommandContext commandContext) {
				DbSqlSession session = commandContext
						.getSession(DbSqlSession.class);
				session.executeSchemaResource(
						"create",
						"temperature",
						"com/diplab/activiti/temperature/db/create/activiti.h2.create.temperature.sql",
						false);
				return null;
			}
		});
		return new ProcessEngineImpl(this);
	}

	@Override
	protected InputStream getMyBatisXmlConfigurationSteam() {
		return ReflectUtil
				.getResourceAsStream("com/diplab/activiti/temperature/db/mapping/mappings.xml");
	}
}
