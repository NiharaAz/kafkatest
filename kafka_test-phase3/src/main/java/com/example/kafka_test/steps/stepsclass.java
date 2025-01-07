package com.example.kafka_test.steps;

import com.example.kafka_test.*;
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
import java.util.concurrent.ExecutionException;

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
    private kafkaActivity kafkaActivity;
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
       // kafkaActivity.startListener("id21");
        System.out.println("kafka listened message");
    }

  /*  @When("Enroll person")
    public void enrollperson()  {

        try{
            Log.info("Test case 1 : Enrolling and validating person in PERSIST mode");
            demoApplication.setBioMode("PERSIST");
//            demoApplication.enroll_hardcoded();
        } catch (Exception e) {
            Log.info("excetion is "+e.getMessage());
            fail(e.getMessage());
        }



    }
    @When("Enroll person with Biom S1003D")
    public void enrollpersonwithS1003D() throws Exception {
        Log.info("Test case 2 : Enrolling and validating person with biom S1003D in PERSIST mode");
        //demoApplication.enroll_biom_S1002D_S1003D_S1004D_hardcoded("S1003D");
        //what happens if excetion is not caught and logged. will the test fail . throws excetion and
        //prints in console but test does not fail

    }
    @When("Enroll person with IdNo is NULL")
    public void EnrollpersonIdnoNull() throws SQLException, ClassNotFoundException, JsonProcessingException {
        Log.info("Test case 3 : Enrolling and validating person with biom in idNo NULL");
        //demoApplication.EnrollIdNoNull();
    }*/


    @When("Ingest person into queue with tdNo={string},natCd={string} and terminal={}")//check if this works
    public void ingest_person_into_queue_with_td_no(String tdNo,String natCd,String terminal) throws Exception {

        Log.info("Test case 4 :Enroll 2 person with diff TdNo but same DOB and natCd");

        SetICSData setICSData= new SetICSData();
        //String TdNoGenerated = utilities.generateRandomAlphanumericString(lengthTdNo);
        String Itinid=  UUID.randomUUID().toString();
        System.out.println("tdno is "+tdNo);
        System.out.println("natCd is "+natCd);
        System.out.println("terminal is "+terminal);
        if (natCd.isEmpty()){
            natCd="SG";
        }
        System.out.println("new natcd is "+natCd);

        ICS_data data = setICSData.SetData(tdNo, Itinid,"S1002D",utilities.generateValidityEndDateTime(),"C");
        Log.info("starting consumer **** ");
        kafkaActivity.startListener("id21");
        Log.info("consumer started *** ");
        sendToQueue.SendToKafka(data);

        //sendToQueue.awaitMessage();  // Ensure message is consumed
        Log.info("Message consumed from Kafka.");

        Thread.sleep(6000);
        Thread.sleep(12000);
//        Thread.sleep(12000);
       // kafkaActivity.stopListener("id2"); //stopping kafka listener

        //06-01 pass td no from kafka oneid queue to here to validate kafka msg
        String tdno_function = sendToQueue.getTdNo();
        System.out.println("td no from function is "+tdno_function);


    }
    @Then("Validate person Key table with tdNo={string}")
    public void validatePersonKeyTableWithTdNo(String tdNo) throws Exception {
        validateController.validate_personKey(tdNo,"SG","20010901");
    }

    @Then("Validate itineraryId")
    public void validate_itineraryId(){
       // validateController.validate_itineraryId(ItinId,"S1002D",td)
    }

    @Then("Validate kafka queue oneid terminal")
    public void validateKafkaQueue() throws ExecutionException, InterruptedException {
        Log.info("testing kafka oneid msg");

        //Log.info("deleting consumer grou");
        //Thread.sleep(6000);//to check if consumer grp exists in kafka ui before deleting
       // kafkaActivity.DeleteConsumer();

    }



}
