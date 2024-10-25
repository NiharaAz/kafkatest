package com.example.kafka_test.database.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@EnableTransactionManagement
@Configuration
@EnableJpaRepositories(
        basePackages = "com.example.kafka_test.database.profile_terminal.repository",
        entityManagerFactoryRef = "ProfileTerminalEntityManagerFactoryBean",
        transactionManagerRef = "ProfileTerminalTransactionManager"
)
public class ProfileTerminalJPAConfiguration {
    @Bean
    LocalContainerEntityManagerFactoryBean ProfileTerminalEntityManagerFactoryBean(EntityManagerFactoryBuilder entityManagerFactoryBuilder, @Qualifier("profileTerminalDatasource") DataSource dataSource){
        return entityManagerFactoryBuilder
                .dataSource(dataSource)
                .packages("com.example.kafka_test.database.profile_terminal.model")
                .build();
    }

    @Bean
    PlatformTransactionManager ProfileTerminalTransactionManager(@Qualifier("ProfileTerminalEntityManagerFactoryBean") LocalContainerEntityManagerFactoryBean emfb){
        return new JpaTransactionManager(emfb.getObject());
    }
}
