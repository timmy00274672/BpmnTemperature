package com.diplab.activiti.engine.impl.cfg;

public class DipStandaloneInMemProcessEngineConfiguration extends
		DipProcessEngineConfiguration {

	public DipStandaloneInMemProcessEngineConfiguration() {
		this.databaseSchemaUpdate = DB_SCHEMA_UPDATE_CREATE_DROP;
		this.jdbcUrl = "jdbc:h2:mem:activiti";
	}

}
