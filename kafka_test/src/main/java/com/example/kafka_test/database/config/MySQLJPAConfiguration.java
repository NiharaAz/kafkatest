package com.example.kafka_test.database.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.example.kafka_test.database.itinerary.repository",
        entityManagerFactoryRef = "mySqlEntityManagerFactoryBean",
        transactionManagerRef = "mySqlTransactionManager"
)
public class MySQLJPAConfiguration {
    @Bean
    @Primary
    LocalContainerEntityManagerFactoryBean mySqlEntityManagerFactoryBean (EntityManagerFactoryBuilder entityManagerFactoryBuilder, @Qualifier("mysqlDatasource") DataSource dataSource){
        return entityManagerFactoryBuilder
                .dataSource(dataSource)
                .packages("com.example.kafka_test.database.itinerary.model")
                .build();
    }

    @Bean
    @Primary
    PlatformTransactionManager mySqlTransactionManager(@Qualifier("mySqlEntityManagerFactoryBean") LocalContainerEntityManagerFactoryBean emfb){
        return new JpaTransactionManager(emfb.getObject());
    }
}