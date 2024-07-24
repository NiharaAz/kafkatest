package com.example.kafka_test.database.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class PersonDatasourceConfiguration {
    @ConfigurationProperties("spring.datasource.person")
    @Bean
    public DataSourceProperties PersonDatasourceProperties(){
        return new DataSourceProperties();
    }

    @Bean
    public DataSource personDatasource(){
        return PersonDatasourceProperties().initializeDataSourceBuilder().build();
    }
}
