package com.example.kafka_test;

import org.jline.utils.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Component
@ShellComponent
public class Utilities {

    @Autowired
    private Environment env;
    @Autowired
    private ResourceLoader resourceLoader;
    String ALPHANUMERIC_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    SecureRandom RANDOM = new SecureRandom();
    public String chooseDirection() {
        Random random = new Random();

        // Array of characters to choose from
        char[] options = {'I', 'O'};
        // Randomly select an index (0 or 1)
        int index = random.nextInt(options.length);

        return String.valueOf(options[index]);
    }

    public String generateRandomAlphanumericString(int length) {
        if (length < 1 || length > 20) {
            throw new IllegalArgumentException("Length must be between 1 and 20.");
        }

        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(ALPHANUMERIC_CHARACTERS.length());
            result.append(ALPHANUMERIC_CHARACTERS.charAt(index));
        }
        return result.toString();
    }
    public void dropTable() throws ClassNotFoundException, SQLException {
        Log.info("*** DROPPING ALL PHASE 2 TABLES ****");
        Resource resource = resourceLoader.getResource("classpath:SQLfiles/dropALLTABLES.sql");

        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String jdbcURL = env.getProperty("db.url");
        String username = env.getProperty("db.username"); // Use an environment variable or properties file
        String password = env.getProperty("db.password"); // Use an environment variable or properties file

        try (Connection connection = DriverManager.getConnection(jdbcURL, username, password)) {
            ScriptUtils.executeSqlScript(connection, resource);
            Log.info("Tables dropped successfully.");
        } catch (SQLException e) {
            Log.error("Error dropping tables: " + e.getMessage(), e);
            throw e; // Rethrow or handle as needed
        }
    }

    public String ChangeVDTtoSGT(String VDT) throws ParseException {
        // Define the desired output format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Parse the input VDT as OffsetDateTime
        ZonedDateTime utcDateTime = OffsetDateTime.parse(VDT).toZonedDateTime();

        // Convert to Singapore Time
        ZonedDateTime sgDateTime = utcDateTime.withZoneSameInstant(ZoneId.of("Asia/Singapore"));

        // Format the output
        String formattedOutput = sgDateTime.format(formatter);
        System.out.println("UTC: " + utcDateTime + " -> Singapore Time: " + sgDateTime);
        return formattedOutput;
    }

    // Converts a VDT to UTC
    public String ChangeVDT_To_UTC(String VDT) {
        // Define the desired output format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Parse the input VDT as OffsetDateTime
        ZonedDateTime utcDateTime = OffsetDateTime.parse(VDT).toZonedDateTime();

        // Convert to UTC
        ZonedDateTime utcTime = utcDateTime.withZoneSameInstant(ZoneId.of("UTC"));

        // Format the output
        String formattedOutput = utcTime.format(formatter);
        System.out.println("Original: " + utcDateTime + " -> UTC: " + formattedOutput);
        return formattedOutput;
    }
    public String ChangeVDT_to_SGt_identifyAPI(String VDT){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss");

        // Parse the input VDT as OffsetDateTime
        ZonedDateTime utcDateTime = OffsetDateTime.parse(VDT).toZonedDateTime();

        // Convert to Singapore Time
        ZonedDateTime sgDateTime = utcDateTime.withZoneSameInstant(ZoneId.of("Asia/Singapore"));

        // Format the output
        String formattedOutput = sgDateTime.format(formatter);
        System.out.println("UTC: " + utcDateTime + " -> Singapore Time: " + sgDateTime);
        return formattedOutput;
    }

    @ShellMethod
    public String generateValidityEndDateTime() {
        // Create a Random instance
        Random random = new Random();

        // Get the current time in UTC
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        System.out.println("Current Time (UTC): " + now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX")));

        // Generate a random number of days (1 to 365) to ensure the date is in the future
        int daysToAdd = random.nextInt(365) + 1; // Randomly between 1 and 365 days
        System.out.println("Days to Add: " + daysToAdd);

        // Create a future date
        ZonedDateTime randomFutureDateTime = now.plusDays(daysToAdd);
        System.out.println("Random Future Date Time (before offset): " + randomFutureDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX")));

        // Set a random offset between -12 and +12 hours
        int offsetHours = random.nextInt(25) - 12; // Random offset from -12 to +12 hours
        // Ensure correct offset format
        String offsetString = String.format("%s%02d:00", offsetHours >= 0 ? "+" : "-", Math.abs(offsetHours));
        System.out.println("Random Timezone Offset: " + offsetString);

        // Apply the random timezone offset
        randomFutureDateTime = randomFutureDateTime.withZoneSameInstant(ZoneOffset.of(offsetString));
        System.out.println("Random Future Date Time (after offset): " + randomFutureDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")));

        // Format the date to the desired string format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        return randomFutureDateTime.format(formatter);
    }

}
