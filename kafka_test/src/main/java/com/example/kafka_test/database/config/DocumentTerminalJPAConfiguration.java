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
        basePackages = "com.example.kafka_test.database.document_terminal.repository",
        entityManagerFactoryRef = "DocumentTerminalEntityManagerFactoryBean",
        transactionManagerRef = "DocumentTerminalTransactionManager"
)
public class DocumentTerminalJPAConfiguration {
    @Bean
    LocalContainerEntityManagerFactoryBean DocumentTerminalEntityManagerFactoryBean(EntityManagerFactoryBuilder entityManagerFactoryBuilder, @Qualifier("documentTerminalDatasource") DataSource dataSource){
        return entityManagerFactoryBuilder
                .dataSource(dataSource)
                .packages("com.example.kafka_test.database.document_terminal.model")
                .build();
    }

    @Bean
    PlatformTransactionManager DocumentTerminalTransactionManager(@Qualifier("DocumentTerminalEntityManagerFactoryBean") LocalContainerEntityManagerFactoryBean emfb){
        return new JpaTransactionManager(emfb.getObject());
    }
}
