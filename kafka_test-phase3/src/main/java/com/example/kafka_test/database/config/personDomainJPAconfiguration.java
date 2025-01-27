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
        basePackages = "com.example.kafka_test.database.personDomain.repository",
        entityManagerFactoryRef = "personDomainEntityManagerFactoryBean",
        transactionManagerRef = "personDomainTransactionManager"
)
public class personDomainJPAconfiguration {
    @Bean
    LocalContainerEntityManagerFactoryBean personDomainEntityManagerFactoryBean(EntityManagerFactoryBuilder entityManagerFactoryBuilder, @Qualifier("personDomainDatasource") DataSource dataSource){
        return entityManagerFactoryBuilder
                .dataSource(dataSource)
                .packages("com.example.kafka_test.database.personDomain.model")
                .build();
    }
    @Bean
    PlatformTransactionManager personDomainTransactionManager(@Qualifier("personDomainEntityManagerFactoryBean") LocalContainerEntityManagerFactoryBean emfb){
        return new JpaTransactionManager(emfb.getObject());
    }
}
