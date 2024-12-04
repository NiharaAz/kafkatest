package com.example.kafka_test.steps;

import com.example.kafka_test.DemoApplication;
import com.example.kafka_test.SendToQueue;
import com.example.kafka_test.SetICSData;
import com.example.kafka_test.Utilities;
import com.example.kafka_test.database.APIs;
import com.example.kafka_test.database.validate_controller;
import com.example.kafka_test.queueData.ICS_data;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


public class stepsclass {

    @Autowired
    private DemoApplication demoApplication;

    @Autowired
    private Utilities utilities;
    @Autowired
    private APIs apIs;

    @Autowired
    private SendToQueue sendToQueue ;
    @Autowired
    private validate_controller validateController ;
    Random random= new Random();
    int lengthTdNo = random.nextInt(1,21);


    public static final Logger Log = LoggerFactory.getLogger(stepsclass.class);

    @Before
    public void beforeScenario() throws SQLException, ClassNotFoundException, IOException, URISyntaxException, InterruptedException {
        utilities.dropTable();
        int statusCode = apIs.DeleteAPI();
        assertEquals(statusCode,204);
    }

    @When("Enroll person")
    public void enrollperson()  {

        try{
            Log.info("Test case 1 : Enrolling and validating person in PERSIST mode");
            demoApplication.setBioMode("PERSIST");
            demoApplication.enroll_hardcoded();
        } catch (Exception e) {
            Log.info("excetion is "+e.getMessage());
            fail(e.getMessage());
        }


        ///demoApplication.setBioMode("DEFAULT");
        //demoApplication.enroll_hardcoded();

    }
    @When("Enroll person with Biom S1003D")
    public void enrollpersonwithS1003D() throws Exception {
        Log.info("Test case 2 : Enrolling and validating person with biom S1003D in PERSIST mode");
        demoApplication.enroll_biom_S1002D_S1003D_S1004D_hardcoded("S1003D");
        //what happens if excetion is not caught and logged. will the test fail . throws excetion and
        //prints in console but test does not fail

    }
    @When("Enroll person with IdNo is NULL")
    public void EnrollpersonIdnoNull() throws SQLException, ClassNotFoundException, JsonProcessingException {
        Log.info("Test case 3 : Enrolling and validating person with biom in idNo NULL");
        demoApplication.EnrollIdNoNull();
    }
    @When("Ingest person into queue with tdNo={string}")
    public void ingest_person_into_queue_with_td_no(String tdNo) throws JsonProcessingException {
        Log.info("Test case 4 :Enroll 2 person with diff TdNo but same DOB and natCd");
        SetICSData setICSData= new SetICSData();
        //String TdNoGenerated = utilities.generateRandomAlphanumericString(lengthTdNo);
        String Itinid=  UUID.randomUUID().toString();
        ICS_data data = setICSData.SetData(tdNo, Itinid,"S1002D",utilities.generateValidityEndDateTime(),"C");
        sendToQueue.SendToKafka(data);


    }

}
