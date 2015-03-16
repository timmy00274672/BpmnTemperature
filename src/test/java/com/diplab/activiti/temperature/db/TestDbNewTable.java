package com.diplab.activiti.temperature.db;

import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.junit.Test;

import com.diplab.activiti.engine.impl.cfg.DipInMemProcessEngineConfiguration;

public class TestDbNewTable {

	public static void main(String[] args) {
	}

	@Test
	public void testDbCreate() {
		ProcessEngineConfigurationImpl configuration = new DipInMemProcessEngineConfiguration();
		configuration.setDatabaseSchemaUpdate("drop-create");
		configuration.buildProcessEngine();

		configuration.getCommandExecutor().execute(new Command<Void>() {

			@Override
			public Void execute(CommandContext commandContext) {
				Object selectOne = commandContext.getDbSqlSession().selectOne(
						"selectDbSchemaVersion", null);
				System.out.println(selectOne);
				return null;
			}
		});
	}
}
