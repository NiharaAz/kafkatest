package com.example.kafka_test.database.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class ProfileTerminalDatasourceConfiguration {
    @ConfigurationProperties("spring.datasource.profile-terminal")
    @Bean
    public DataSourceProperties ProfileTerminalDatasourceProperties(){
        return new DataSourceProperties();
    }

    @Bean
    public DataSource profileTerminalDatasource(){
        return ProfileTerminalDatasourceProperties().initializeDataSourceBuilder().build();
    }
}
