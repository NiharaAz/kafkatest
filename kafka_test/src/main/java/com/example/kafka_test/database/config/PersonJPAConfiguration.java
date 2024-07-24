package com.example.kafka_test.database.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.example.kafka_test.database.profile.repository",
        entityManagerFactoryRef = "PersonEntityManagerFactoryBean",
        transactionManagerRef = "PersonTransactionManager"
)
public class PersonJPAConfiguration {
    @Bean
    LocalContainerEntityManagerFactoryBean PersonEntityManagerFactoryBean (EntityManagerFactoryBuilder entityManagerFactoryBuilder, @Qualifier("personDatasource") DataSource dataSource){
        return entityManagerFactoryBuilder
                .dataSource(dataSource)
                .packages("com.example.kafka_test.database.profile.model")
                .build();
    }

    @Bean
    PlatformTransactionManager PersonTransactionManager(@Qualifier("PersonEntityManagerFactoryBean") LocalContainerEntityManagerFactoryBean emfb){
        return new JpaTransactionManager(emfb.getObject());
    }
}
