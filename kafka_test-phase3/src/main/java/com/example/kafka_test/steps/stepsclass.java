package com.example.kafka_test.steps;

import com.example.kafka_test.*;
import com.example.kafka_test.database.APIs;
import com.example.kafka_test.database.validate_controller;
import com.example.kafka_test.queueData.ICS_data;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
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

    String personid;
    String tdno_userInput;
    String natcd_userInput;
    String DOB_userInput;
    String VDT_userInput;
    String nric_userInput;
    String direction_userInput;
    String Itinid=  UUID.randomUUID().toString();
    String translatedId;


    @Autowired
    private ResourceLoader resourceLoader;


    public static final Logger Log = LoggerFactory.getLogger(stepsclass.class);
    private Map<String, Object> personData;
    public static Map<String, Object> extractedFields= new HashMap<>();

    ObjectMapper obj= new ObjectMapper();

    public static Map<String, Object> getExtractedFields() {
        return extractedFields;
    }

    @Before
    public void beforeScenario() throws SQLException, ClassNotFoundException, IOException, URISyntaxException, InterruptedException {
        utilities.dropTable();
        int statusCode = apIs.DeleteAPI();
        assertEquals(statusCode,204);
        // kafkaActivity.startListener("id21");
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


    @When("Ingest person into queue with tdNo={string},natCd={string},nric={string},dob={string},direction={string}")//check if this works
    public void ingest_person_into_queue_with_td_no(String tdNo, String natCd, String nric, String dob, String direction) throws Exception {

        Log.info("Test case 1 :Enroll 1 person into ics kafka queue");

        SetICSData setICSData= new SetICSData();
        //String TdNoGenerated = utilities.generateRandomAlphanumericString(lengthTdNo);

        Log.info("tdno received from user is "+tdNo);
        Log.info("natcd received from user is "+natCd);
        Log.info("nric received from user is "+nric);
        Log.info("dob received from user is "+dob);

        String VDT = utilities.generateValidityEndDateTime();
        ICS_data data = setICSData.SetData(tdNo, Itinid,nric,VDT,"C",natCd,dob,direction);

        this.tdno_userInput=tdNo;
        this.natcd_userInput=natCd;
        this.DOB_userInput= dob;
        this.nric_userInput= nric;
        this.VDT_userInput= VDT;
        this.direction_userInput=direction;


        Log.info("starting consumer **** ");
        kafkaActivity.startListener("id3");
        Log.info("consumer started *** ");

        Log.info("sending message ");
        sendToQueue.SendToKafka(data);


        Thread.sleep(6000);
        Thread.sleep(12000);

        // kafkaActivity.stopListener("id2"); //stopping kafka listener

       /* //set tdno to be the tdno consumed from kafka
        tdno_OneidKafka = sendToQueue.getTdNo();*/

    }


    // Helper method to extract multiple values from the personData map
    private Map<String, Object> getFieldsFromFile(Map<String,Object> personData){


        Map<String, Object> travellerInfo = (Map<String, Object>) personData.get("travellerInfo");
        String idno= (String) travellerInfo.get("idNo");
        String tdNo= (String) travellerInfo.get("tdNo");
        String natCd = (String) travellerInfo.get("natCd");
        int dob = (int) travellerInfo.get("dobTxt");
        String dob_str= String.valueOf(dob);


        // Extract itinerary info
        Map<String, Object> itineraryInfo = (Map<String, Object>) personData.get("itineraryInfo");
        String direction = (String) itineraryInfo.get("statInOut");
        String VDET = (String) itineraryInfo.get("validityEndDateTime");
        String itinId= (String) itineraryInfo.get("itineraryId");
        String chkpt= (String) itineraryInfo.get("chkptCd");

        extractedFields.put("idNo",idno);
        extractedFields.put("tdNo",tdNo);
        extractedFields.put("natCd",natCd);
        extractedFields.put("dob",dob_str);
        extractedFields.put("direction",direction);
        extractedFields.put("VDET",VDET);
        extractedFields.put("itinId",itinId);
        extractedFields.put("chkpt",chkpt);

        return extractedFields;
    }
    public String readJsonFile(String filename) throws IOException {

        Resource resource = resourceLoader.getResource("classpath:"+filename);
        if (!resource.exists()) {
            throw new IOException("File not found: ");
        }
        String jsonString;
        try (InputStream inputStream = resource.getInputStream()) {
            jsonString = new String(inputStream.readAllBytes());
            personData = obj.readValue(jsonString, Map.class);
            getFieldsFromFile(personData);
        }
        return jsonString;
    }

    @When("I ingest person into ics kafka topic") //put singapore passport here paramter  14/03
    public void ingestPersonIntoQueue(String fileName) throws Exception {

        String jsonString= readJsonFile(fileName);

        Log.info("starting consumer **** ");
        kafkaActivity.startListener("id3");
        Log.info("consumer started *** ");
        Thread.sleep(6000);

        Log.info("sending message ");
        sendToQueue.SendToKafkaFromjsonString(jsonString);


        Thread.sleep(10000);
        Thread.sleep(12000);

        //tdno_OneidKafka = sendToQueue.getTdNo();
    }

    @When("Ingest person into queue with json string")
    public void ingestPersonIntoQueueWithBelowData(String jsonData) throws JsonProcessingException {

        sendToQueue.SendToKafkaFromjsonString(jsonData);

        //personData = obj.readValue(jsonData, Map.class);
    }

    @Then("I validate personKey in ecp's person Key table")
    public void validatePersonKeyTableWithTdNo() throws Exception {

        String tdnoLocal= (String) extractedFields.get("tdNo");
        String natcdLocal= (String) extractedFields.get("natCd");
        String dobLocal = (String) extractedFields.get("dob");

        this.personid= validateController.validate_personKey(tdnoLocal, natcdLocal, dobLocal);
    }

    @Then("I validate itinId,idNo,tdNo,natCd,dob,terminal, direction,VDET,personId in ecp's ics_itinerary table")
    public void validate_itineraryId() throws Exception {

        String terminal = demoApplication.getTerminalMapped().get("C");

        String tdNo= (String) extractedFields.get("tdNo");
        String idNo= (String) extractedFields.get("idNo");
        String natCd= (String) extractedFields.get("natCd");
        String dob= (String) extractedFields.get("dob");
        String itinId= (String) extractedFields.get("itinId");
        String direction= (String) extractedFields.get("direction");
        String VDET= (String) extractedFields.get("VDET");

        translatedId =  validateController.validate_itineraryId(itinId,idNo,tdNo,natCd
                , dob,terminal,direction,VDET,personid);
    }

    @Then("I validate oneid terminal doc exist in doc_bcbp table")
    public void validateDocBcbp() throws Exception {

        String direction= (String) extractedFields.get("direction");
        String VDET = (String) extractedFields.get("VDET");

        validateController.validate_doc_bcbp(direction,"SIN1",VDET,translatedId);
    }

    @Then("I validate oneid terminal person exist in person table")
    public void validateProfileTerminal() throws Exception {
        validateController.validate_terminal_personId(personid);
    }

    @Then("I validate central's kafka topic consists of correct tdNo")
    public void validateKafkaQueue() throws ExecutionException, InterruptedException {
        Log.info("*** validate oneid central kafka TdNo ***");

        //set tdno to be the tdno consumed from kafka
        String tdno_OneidKafka = sendToQueue.getTdNo();
        String tdNo_ingested = (String) extractedFields.get("tdNo");
        String natCd_ingested = (String) extractedFields.get("natCd");
        String dob_ingested=(String) extractedFields.get("dob");
        String direction_ingested= (String) extractedFields.get("direction");
        String natCd_kafka= sendToQueue.getNatCd();
        String direction_kafka= sendToQueue.getDirection();
        String dob_kafka= sendToQueue.getDob();

        Log.info("direction_kafka"+direction_kafka);
        Log.info("natcd"+natCd_ingested);
        assertEquals(tdNo_ingested,tdno_OneidKafka);
        assertEquals(natCd_ingested,natCd_kafka);
        assertEquals(direction_ingested,direction_kafka);
        assertEquals(dob_kafka,dob_kafka);
    }


    @When("I update the person's natCd = {string} and send updated person to Kafka ICS topic")
    public void updateFieldsinJsonFile(String natCd) throws IOException, InterruptedException {

        Log.info("*** updating natcd=SG ***");

        Map<String, Object> travellerInfo = (Map<String, Object>) personData.get("travellerInfo");

        travellerInfo.put("natCd",natCd);

        // Convert the modified Map back to a JSON string
        String updatedJsonString = obj.writeValueAsString(personData);
        Log.info("new updated json string is "+updatedJsonString);
        Log.info("sending message ");
        Log.info("persondata natcd is "+((Map<?, ?>) personData.get("travellerInfo")).get("natCd"));
        sendToQueue.SendToKafkaFromjsonString(updatedJsonString);

        Thread.sleep(6000);
        Thread.sleep(12000);

    }
    @Then("I compare natCd before update and natCd send to update")
    public void CheckWhetherFieldsAreUpdated(){

        String natCd= (String) extractedFields.get("natCd");

        Map<String, Object> travellerInfo = (Map<String, Object>) personData.get("travellerInfo");
        String natcdUpdated = (String) travellerInfo.get("natCd");
        Log.info("personData natcd is "+natcdUpdated);
        Log.info("natcd from extracted field is "+natCd);

        if(natcdUpdated.equals(natCd)){
            fail("Field did not get updated");
        }else{
            Log.info("natcd is updated");
            getFieldsFromFile(personData); //sync personData to extractedfield map
        }

    }
    @Then("I make a GET Biometrics call to MBSS to verify corrrect bio types are present")
    public void GetBiomAPICall() throws URISyntaxException, IOException, InterruptedException {

        String nric = (String) extractedFields.get("idNo");
        apIs.mmbsGETBIOMapi(personid,nric);
    }




}



