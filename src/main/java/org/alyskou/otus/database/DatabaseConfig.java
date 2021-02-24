package org.alyskou.otus.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Primary
    @Bean(name = "dbMaster")
    @ConfigurationProperties(prefix="spring.mysql-master")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "dbSlave")
    @ConfigurationProperties(prefix="spring.mysql-slave")
    public DataSource slaveDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Autowired
    @Bean(name = "jdbcMaster")
    public JdbcTemplate masterJdbcTemplate(@Qualifier("dbMaster") DataSource dbMaster) {
        return new JdbcTemplate(dbMaster);
    }

    @Autowired
    @Bean(name = "jdbcSlave")
    public JdbcTemplate slaveJdbcTemplate(@Qualifier("dbSlave") DataSource dbSlave) {
        return new JdbcTemplate(dbSlave);
    }
}
