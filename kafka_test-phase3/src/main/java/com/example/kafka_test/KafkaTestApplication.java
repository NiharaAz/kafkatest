package com.example.kafka_test;

import io.cucumber.core.cli.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class KafkaTestApplication {

	@Autowired
	DemoApplication demo;

	public static void main(String[] args) {

		SpringApplication.run(KafkaTestApplication.class, args);

		try {
			String[] cucumberArgs = {
					"--plugin", "pretty",
					"--plugin", "html:C:\\Users\\nihara.azeem\\Desktop\\t.html",
					"--glue", "com.example.kafka_test.steps",
					"--glue", "com/example/kafka_test/configuration",
					"classpath:feature"
			};

			Main.main(cucumberArgs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
