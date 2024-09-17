package com.example.kafka_test.database.config.phase3;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class Phase3PersonDomainDatasourceConfiguration {
    @ConfigurationProperties("spring.datasource.phase3.person-domain")
    @Bean
    public DataSourceProperties phase3PersonDomainDatasourceProperties(){
        return new DataSourceProperties();
    }

    @Bean
    public DataSource phase3personDomainDatasource(){
        return phase3PersonDomainDatasourceProperties().initializeDataSourceBuilder().build();
    }
}
