package com.example.kafka_test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.command.annotation.CommandScan;

@SpringBootApplication
@CommandScan(basePackages = "com.example.kafka_test")
public class KafkaTestApplication {

	@Autowired
	DemoApplication demo;

	public static void main(String[] args) {
		SpringApplication.run(KafkaTestApplication.class, args);
	}

}
