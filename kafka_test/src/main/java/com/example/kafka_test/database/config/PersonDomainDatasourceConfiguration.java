package com.example.kafka_test.database.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class PersonDomainDatasourceConfiguration {
    @ConfigurationProperties("spring.datasource.person-domain")
    @Bean
    public DataSourceProperties PersonDomainDatasourceProperties(){
        return new DataSourceProperties();
    }

    @Bean
    public DataSource personDomainDatasource(){
        return PersonDomainDatasourceProperties().initializeDataSourceBuilder().build();
    }
}
