package com.diplab.activiti.engine.impl.cfg;

public class DipInMemProcessEngineConfiguration extends
		DipProcessEngineConfiguration {

	public DipInMemProcessEngineConfiguration() {
		this.databaseSchemaUpdate = DB_SCHEMA_UPDATE_DROP_CREATE;
		this.jdbcUrl = "jdbc:h2:mem:activiti";
	}
}
