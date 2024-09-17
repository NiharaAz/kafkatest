package com.example.kafka_test.database.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class ItineraryDatasourceConfiguration {

    @ConfigurationProperties("spring.datasource.itinerary")
    @Bean
    public DataSourceProperties itineraryDatasourceProperties(){
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    public DataSource itineraryDatasource(){
        return itineraryDatasourceProperties().initializeDataSourceBuilder().build();
    }
}
