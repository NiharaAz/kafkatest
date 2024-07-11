package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaTestApplication {

//	@Autowired
//	DemoApplication demo;

	public static void main(String[] args) {
		SpringApplication.run(KafkaTestApplication.class, args);
	}

	/*@Override
	public void run(String... args) throws Exception {
		demo.sendMessage();
	}*/
}
