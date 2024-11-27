package com.example.kafka_test.configuration;

import com.example.kafka_test.KafkaTestApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;


@SpringBootTest
@CucumberContextConfiguration
@ContextConfiguration(classes = KafkaTestApplication.class)

public class CucumberConfig {

}
