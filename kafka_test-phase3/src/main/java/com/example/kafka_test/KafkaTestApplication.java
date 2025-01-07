package com.example.kafka_test;

import io.cucumber.core.cli.Main;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


@SpringBootApplication
public class KafkaTestApplication {

	@Autowired
	DemoApplication demo;

	public static void main(String[] args) {

//		SpringApplication.run(KafkaTestApplication.class, args);

		try {
			String[] cucumberArgs = {
					"--plugin", "pretty",
					"--plugin", "html:target/cucumber-reports/cucumber-report.html", // HTML report location
					"--plugin", "json:target/cucumber-reports/CucumberTestReport.json",
					"--glue", "com.example.kafka_test.steps",
					"--glue", "com/example/kafka_test/configuration",
					"classpath:feature"
			};

			// Run Cucumber tests and wait for them to finish
			int exitCode = Main.run(cucumberArgs, Thread.currentThread().getContextClassLoader());
			generateCucumberReport();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


		private static void generateCucumberReport() {
			// Location of the generated JSON report file from Cucumber
			File reportOutputDirectory = new File("target/cucumber-reports");

			// List of JSON files containing Cucumber results
			List<String> jsonFiles = new ArrayList<>();
			jsonFiles.add("target/cucumber-reports/CucumberTestReport.json"); // Ensure correct file path

			// Configuration for the report (e.g., build number, project name)
			String buildNumber = "1";
			String projectName = "cucumberProject";

			// Initialize configuration
			Configuration configuration = new Configuration(reportOutputDirectory, projectName);
			configuration.setBuildNumber(buildNumber);

			// Generate the report
			ReportBuilder reportBuilder = new ReportBuilder(jsonFiles, configuration);
			reportBuilder.generateReports();

			System.out.println("Finished generating Cucumber reports.");
		}
	}

