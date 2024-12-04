package com.example.kafka_test;

import com.example.kafka_test.database.APIs;
import com.example.kafka_test.database.Mapping;
import com.example.kafka_test.database.validate_controller;
import com.example.kafka_test.queueData.ICS_data;
import com.example.kafka_test.database.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.*;

@Component

public class DemoApplication  {
   /* @Autowired
    private KafkaTemplate<String, String> Template;*/

    public static final Logger Log = LoggerFactory.getLogger(DemoApplication.class);

    private final SendToQueue sendToQueue;

    private final validate_controller validateController;

    private final APIs apIs;
    private final Utilities utilities;
    private final Mapping mapping;

    Random random = new Random();


    @Autowired
    public DemoApplication(SendToQueue sendToQueue, validate_controller validateController, APIs apIs, Utilities utilities, Mapping mapping) {
        this.sendToQueue = sendToQueue;
        this.validateController = validateController;
        this.apIs = apIs;
        this.utilities = utilities;
        this.mapping = mapping;
    }

    private Map<String, String> terminalMapped;
    @PostConstruct
    public void init() {
        terminalMapped = mapping.mappingCheckpoint();
    }
 /*   //@ShellMethod
    public void SendToKafka(ICS_data data) throws JsonProcessingException {
        Log.info("*** Sending msg to Kafka Queue ***");
        ObjectMapper objectMapper= new ObjectMapper();
        String jsonString= objectMapper.writeValueAsString(data);

        //ingest into kafka queue
        ProducerRecord<String,String> record= new ProducerRecord<>(this.Topic,jsonString);
        record.headers().add("msgActCd", "A".getBytes());
        record.headers().add("msgCreateDateTime","2021-08-24T18:15:01Z".getBytes());
        Template.send(record);
    }*/


    private String bioMode;


    public void setBioMode(String mode) {
        this.bioMode = mode;
        System.out.println("BioMode set to: " + this.bioMode);
    }


    public void enroll(String TdNo, String ItinId, String nric,String VDT_input,String terminal) throws Exception {
        SetICSData setICSData= new SetICSData();
        ICS_data data= setICSData.SetData(TdNo, ItinId, nric,VDT_input,terminal);

        //ingest into kafka queue
        //SendToKafka(data);
        sendToQueue.SendToKafka(data);

        //validate if personKey in ingested data is == personKey in DB
        String dob= String.valueOf(data.getTravellerInfo().getDobTxt());
        String natCd= data.getTravellerInfo().getNatCd();
         terminal="SIN1";
        String direction=data.getItineraryInfo().getStatInOut();
        String has_images="1";


        Thread.sleep(3000);
        String personId = validateController.validate_personKey(TdNo,natCd,dob);

        Thread.sleep(3000);
        //ics itinerary VDET is in UTC timing
        String translatedId= validateController.validate_itineraryId(ItinId, nric, TdNo, natCd, dob, terminal,
                direction, VDT_input, personId);
        //validate if personId in ingested data is == personId in oneId DB
        validateController.validate_terminal_personId(personId);
        validateController.validate_doc_passport(dob,natCd,TdNo,nric);
        validateController.validate_doc_bcbp(direction,terminal, VDT_input,translatedId);
        validateController.validate_biom_profile(personId,has_images);
        //validateController.validate_biom_profile_image(personId);
        APIs apIs= new APIs(mapping, utilities);
        apIs.mmbsGETBIOMapi(personId,nric);
        apIs.identify(UUID.randomUUID().toString(),TdNo,natCd,dob,terminal,direction,VDT_input,nric);


    }


    public void enroll_hardcoded() throws Exception {
        Log.info("**** Enrolling 1 person with biom *** ");



        if (bioMode != null) {
            System.out.println("Enrolling with BioMode: " + bioMode);

        } else {
            System.out.println("BioMode not set. Please set it using 'setBioMode' command.");
            return;
        }

        utilities.dropTable();
        int statusCode = apIs.DeleteAPI();

        if (statusCode!=204){
            Log.info("***** unable to delete from mmbs. Resonse Status code is "+statusCode);
            return ;
        }

        int lengthTdNo = random.nextInt(1,21);
        String TdNoGenerated = utilities.generateRandomAlphanumericString(lengthTdNo);

        SetICSData setICSData= new SetICSData();
        String terminal="C";
        String TdNo=TdNoGenerated;
        String ItinId= UUID.randomUUID().toString();
        String nric="S1002D";

        String VDT_input = utilities.generateValidityEndDateTime();
        Log.info("VDT is "+VDT_input);

        ICS_data data= setICSData.SetData(TdNo, ItinId, nric,VDT_input,terminal);

        //ingest into kafka queue
        //SendToKafka(data);
        sendToQueue.SendToKafka(data);


        String dob= String.valueOf(data.getTravellerInfo().getDobTxt());
        String natCd= "MY";


        String terminal_mapped=terminalMapped.get(terminal);
        String direction=utilities.chooseDirection();
        String has_images="1";
        String personId=null;

        Thread.sleep(3000);

       // try{
            personId = validateController.validate_personKey(TdNo,natCd,dob);
            Thread.sleep(3000);
            //ics itinerary VDET is in UTC timing
            String translatedId = validateController.validate_itineraryId(ItinId, nric, TdNo, natCd, dob, terminal_mapped,
                    direction, VDT_input, personId);

            validateController.validate_terminal_personId(personId);
            validateController.validate_doc_passport(dob,natCd,TdNo,nric);
            validateController.validate_doc_bcbp(direction,terminal_mapped, VDT_input,translatedId);
            validateController.validate_biom_profile(personId,has_images);

            if(bioMode.equals("PERSIST")){
                validateController.validate_biom_profile_image(personId);
            }

            APIs apIs= new APIs(mapping, utilities);
            apIs.mmbsGETBIOMapi(personId,nric);
            apIs.identify(UUID.randomUUID().toString(),TdNo,natCd,dob,terminal_mapped,direction,VDT_input,nric);


       // }
      /*  catch(Exception e){
            Log.info(e.getMessage());
        }*/
    }
    //@ShellMethod
   /* public void Enroll2personDiffTdNo(String TdNo, String ItinId, String nric,String VDT_input, String TdNo2, String ItinId2,String terminal) throws Exception {

        Log.info(" **** Test case  Enroll 2 different person with different TdNo but same nric, dob & natCd");

        SetICSData setICSData= new SetICSData();
        ICS_data data= setICSData.SetData(TdNo, ItinId, nric,VDT_input,terminal);
        //ingest person A into kafka queue
        //SendToKafka(data);
        sendToQueue.SendToKafka(data);
        //validate if personKey in ingested data is == personKey in DB
        String dob= String.valueOf(data.getTravellerInfo().getDobTxt());
        String natCd= data.getTravellerInfo().getNatCd();
         terminal="SIN1";
        String direction=data.getItineraryInfo().getStatInOut();

       // SetICSData setICSData1= new SetICSData();
        ICS_data data1= setICSData.SetData(TdNo2, ItinId2, nric,VDT_input,terminal);
        //ingest person B into kafka queue
        SendToKafka(data1);
        //validate if personKey in ingested data is == personKey in DB
        Thread.sleep(3000);

        String personid= validateController.validate_personKey(TdNo,natCd,dob);
        String personid2= validateController.validate_personKey(TdNo2,natCd,dob);

        //Thread.sleep(3000);
        validateController.validate_itineraryId(ItinId,nric,TdNo,natCd,dob,terminal,direction
        ,VDT_input,personid);
        validateController.validate_terminal_personId(personid);

        validateController.validate_itineraryId(ItinId2,nric,TdNo2,natCd,dob,terminal,direction
                ,VDT_input,personid2);
        validateController.validate_terminal_personId(personid2);

    }*/

    public void Enroll2personDiffTdNo_hardcoded() throws Exception {

        Log.info(" **** Test case  Enroll 2 different person with different TdNo but same nric, dob & natCd");

        utilities.dropTable();

        String terminal="C";
        String TdNo="payload1";
        String ItinId= UUID.randomUUID().toString();
        String nric="S1002D";
        String VDT_input = "2028-11-11T00:09:00Z";
        String TdNo2="payload2";
        String ItinId2= UUID.randomUUID().toString();

        SetICSData setICSData= new SetICSData();
        ICS_data data= setICSData.SetData(TdNo, ItinId, nric,VDT_input,terminal);
        //ingest person A into kafka queue
        //SendToKafka(data);
        Log.info("*** Sending 1st message to queue ****");
        sendToQueue.SendToKafka(data);
        //validate if personKey in ingested data is == personKey in DB
        String dob= String.valueOf(data.getTravellerInfo().getDobTxt());
        String natCd= "SG";

        String direction=data.getItineraryInfo().getStatInOut();

        // SetICSData setICSData1= new SetICSData();
        ICS_data data1= setICSData.SetData(TdNo2, ItinId2, nric,VDT_input,terminal);
        //ingest person B into kafka queue
        //SendToKafka(data1);
        Log.info("*** Sending 2nd message to queue ****");
        sendToQueue.SendToKafka(data1);
        //validate if personKey in ingested data is == personKey in DB
        Thread.sleep(3000);
        String terminal_Mapped = terminalMapped.get(terminal);

        Log.info("*** Validating person key of 1st msg *** ");
        String personid= validateController.validate_personKey(TdNo,natCd,dob);
        Log.info("*** Validating person key of 2nd msg *** ");
        String personid2= validateController.validate_personKey(TdNo2,natCd,dob);

        if(personid==null || personid2 == null){
            Log.info("*** person not found in personKEY table ***");
            return;
        }
        //Thread.sleep(3000);
        Log.info("*** Validating 1st msg in ics_itinerary and profile terminal tables *** ");
        validateController.validate_itineraryId(ItinId,nric,TdNo,natCd,dob,terminal_Mapped,direction
                ,VDT_input,personid);
        validateController.validate_terminal_personId(personid);


        Log.info("*** Validating 2nd msg in ics_itinerary and profile terminal tables *** ");
        validateController.validate_itineraryId(ItinId2,nric,TdNo2,natCd,dob,terminal_Mapped,direction
                ,VDT_input,personid2);
        validateController.validate_terminal_personId(personid2);
    }

    /*
    @ShellMethod //23-10 new change
    public void Enroll2personDiffnatCd_hardcoded() throws Exception {

        Log.info(" **** Test case  Enroll 2 different person with different natCd but same nric, TdNo & dob");

        utilities.dropTable();

        String terminal="C";
        String TdNo="payload1";
        String ItinId= UUID.randomUUID().toString();
        String nric="S1002D";
        String VDT_input = "2028-11-11T00:09:00Z";
        String ItinId2= UUID.randomUUID().toString();
        String natCd1= "SG";
        String natCd2= "MY";

        SetICSData setICSData= new SetICSData();
        ICS_data data= setICSData.SetDatawithnatCd(TdNo, ItinId, nric,VDT_input,terminal,natCd1);
        //ingest person A into kafka queue
        SendToKafka(data);
        //validate if personKey in ingested data is == personKey in DB
        String dob= String.valueOf(data.getTravellerInfo().getDobTxt());
        String direction=data.getItineraryInfo().getStatInOut();

        // SetICSData setICSData1= new SetICSData();
        ICS_data data1= setICSData.SetDatawithnatCd(TdNo, ItinId2, nric,VDT_input,terminal,natCd2);
        //ingest person B into kafka queue
        SendToKafka(data1);
        //validate if personKey in ingested data is == personKey in DB
        Thread.sleep(3000);
        String terminal_Mapped = terminalMapped.get(terminal);

        String personid= validateController.validate_personKey(TdNo,natCd1,dob);
        String personid2= validateController.validate_personKey(TdNo,natCd2,dob);

        if(personid==null || personid2 == null){
            Log.info("*** person not found in personKEY table ***");
            return;
        }
        //Thread.sleep(3000);
        validateController.validate_itineraryId(ItinId,nric,TdNo,natCd1,dob,terminal_Mapped,direction
                ,VDT_input,personid);
        validateController.validate_terminal_personId(personid);

        validateController.validate_itineraryId(ItinId2,nric,TdNo,natCd2,dob,terminal_Mapped,direction
                ,VDT_input,personid2);
        validateController.validate_terminal_personId(personid2);
    }

    @ShellMethod //23-10 new change
    public void Enroll2personDiffDob_hardcoded() throws Exception {

        Log.info(" **** Test case  Enroll 2 different person with different Dob but same nric, TdNo & natCd");

        utilities.dropTable();

        String terminal="C";
        String TdNo="payload1";
        String ItinId= UUID.randomUUID().toString();
        String nric="S1002D";
        String VDT_input = "2028-11-11T00:09:00Z";
        String ItinId2= UUID.randomUUID().toString();
        String natCd="SG";
        String dob1="20090101";
        String dob2="20140817";


        SetICSData setICSData= new SetICSData();
        ICS_data data= setICSData.SetDataWithdob(TdNo, ItinId, nric,VDT_input,terminal,dob1);
        //ingest person A into kafka queue
        SendToKafka(data);
        //validate if personKey in ingested data is == personKey in DB
        String direction=data.getItineraryInfo().getStatInOut();

        // SetICSData setICSData1= new SetICSData();
        ICS_data data1= setICSData.SetDataWithdob(TdNo, ItinId2, nric,VDT_input,terminal,dob2);
        //ingest person B into kafka queue
        SendToKafka(data1);
        //validate if personKey in ingested data is == personKey in DB
        Thread.sleep(3000);
        String terminal_Mapped = terminalMapped.get(terminal);

        String personid= validateController.validate_personKey(TdNo,natCd,dob1);
        String personid2= validateController.validate_personKey(TdNo,natCd,dob2);

        if(personid==null || personid2 == null){
            Log.info("*** person not found in personKEY table ***");
            return;
        }
        //Thread.sleep(3000);
        validateController.validate_itineraryId(ItinId,nric,TdNo,natCd,dob1,terminal_Mapped,direction
                ,VDT_input,personid);
        validateController.validate_terminal_personId(personid);

        validateController.validate_itineraryId(ItinId2,nric,TdNo,natCd,dob2,terminal_Mapped,direction
                ,VDT_input,personid2);
        validateController.validate_terminal_personId(personid2);
    }

     */


    public void Enroll2Itin1person_hardcoded() throws Exception {
        Log.info(" **** Test case : Enroll 2 different Itin for 1 person");

        utilities.dropTable();

        String terminal="C";
        String TdNo="payload1";
        String ItinId= UUID.randomUUID().toString();
        String nric="S1002D";
        String VDT_input = "2028-11-11T00:09:00Z";
        String ItinId2= UUID.randomUUID().toString();

        SetICSData setICSData= new SetICSData();
        ICS_data data= setICSData.SetData(TdNo, ItinId, nric,VDT_input,terminal);
        //ingest person A into kafka queue

        Log.info("*** Sending 1st message to queue ****");
        sendToQueue.SendToKafka(data);//newchange 24-10

        //validate if personKey in ingested data is == personKey in DB
        String dob= String.valueOf(data.getTravellerInfo().getDobTxt());
        String natCd="SG";

        String direction=data.getItineraryInfo().getStatInOut();

        // SetICSData setICSData1= new SetICSData();
        ICS_data data1= setICSData.SetData(TdNo, ItinId2, nric,VDT_input,terminal);
        //ingest person B into kafka queue
        //SendToKafka(data1);
        Log.info("*** Sending 2nd message to queue ****");
        sendToQueue.SendToKafka(data1);//new change 24-10

        //validate if personKey in ingested data is == personKey in DB
        Thread.sleep(3000);
        String terminal_Mapped=terminalMapped.get(terminal);

        String personid= validateController.validate_personKey(TdNo,natCd,dob);
        if(personid==null){
            return;
        }
        Log.info("**** Validating itineraryId 1 *** ");
        //validate 1st itinId
         String TranslatedId1= validateController.validate_itineraryId(ItinId,nric,TdNo,natCd,dob,terminal_Mapped,direction
                ,VDT_input,personid);
        //validate 2nd ItinId
        Log.info("**** Validating itineraryId 2 *** ");
        String TranslatedId2 =validateController.validate_itineraryId(ItinId2,nric,TdNo,natCd,dob,terminal_Mapped,direction
                ,VDT_input,personid);

        validateController.validate_terminal_personId(personid);
        validateController.validate_doc_bcbp("I",terminal_Mapped,VDT_input,TranslatedId1);
        validateController.validate_doc_bcbp("I",terminal_Mapped,VDT_input,TranslatedId2);
        validateController.validate_doc_passport(dob,natCd,TdNo,nric);

    }


    public  void EnrollIdNoNull() throws SQLException, ClassNotFoundException, JsonProcessingException {
        Log.info(" **** Test case  Enroll IdNo is NULL ");

        utilities.dropTable();

        String terminal="C";
        String TdNo="1002";
        String ItinId= UUID.randomUUID().toString();
        String nric="";//new change-18/10
        String VDT_input = "2028-11-11T00:09:00Z";
        String natCd="SG";
        String dob="20240101";


        SetICSData setICSData= new SetICSData();
        ICS_data data= setICSData.SetDataWithdob(TdNo, ItinId, nric,VDT_input,terminal,dob);

        sendToQueue.SendToKafka(data);

        String terminal_Mapped= terminalMapped.get(terminal);
        try{
            String personId = validateController.validate_personKey(TdNo,natCd,dob);
            validateController.validate_itineraryId(ItinId,nric,TdNo,natCd,dob,terminal_Mapped,"I",VDT_input,personId);

            apIs.identify(UUID.randomUUID().toString(),TdNo,natCd,dob,terminal_Mapped,"I",VDT_input,"S1002D");

            Log.info("**** test case passed - IdNo NULL is enrolled ***");
        }catch (Exception e){
            Log.info(e.getMessage());
        }


    }


    public void enroll_biom_S1002D_S1003D_S1004D_hardcoded(String nric) throws Exception {
        Log.info("**** Enrolling 1 person with biom *** ");


       /* if (bioMode != null) {
            System.out.println("Enrolling with BioMode: " + bioMode);

        } else {
            System.out.println("BioMode not set. Please set it using 'setBioMode' command.");
            return;
        }*/

        utilities.dropTable();
        int statusCode = apIs.DeleteAPI();
        if(statusCode!= 204){
            Log.info("unable to delete from mmbs. status code is "+statusCode);
            return ;
        }
        int lengthTdNo = random.nextInt(1,21);

        String TdNoGenerated = utilities.generateRandomAlphanumericString(lengthTdNo);

        SetICSData setICSData= new SetICSData();
        String terminal="C";
        String TdNo=TdNoGenerated;
        String ItinId= UUID.randomUUID().toString();

        String VDT_input = "2028-11-11T00:09:00Z";

        ICS_data data= setICSData.SetData(TdNo, ItinId, nric,VDT_input,terminal);


        sendToQueue.SendToKafka(data);

        String dob= String.valueOf(data.getTravellerInfo().getDobTxt());
        String natCd= data.getTravellerInfo().getNatCd();

        String terminal_mapped=terminalMapped.get(terminal);
        String direction=data.getItineraryInfo().getStatInOut();
        String has_images="1";
        String personId=null;

        Thread.sleep(3000);

        try{
            personId = validateController.validate_personKey(TdNo,"MY",dob);
            Thread.sleep(3000);
            //ics itinerary VDET is in UTC timing
            String translatedId = validateController.validate_itineraryId(ItinId, nric, TdNo, natCd, dob, terminal_mapped,
                    direction, VDT_input, personId);
            //validate if personId in ingested data is == personId in oneId DB
            validateController.validate_terminal_personId(personId);
            validateController.validate_doc_passport(dob,natCd,TdNo,nric);
            validateController.validate_doc_bcbp(direction,terminal_mapped, VDT_input,translatedId);
            validateController.validate_biom_profile(personId,has_images);

            if(bioMode.equals("PERSIST")){
                validateController.validate_biom_profile_image(personId);
            }

           // APIs apIs= new APIs(mapping, utilities);
            apIs.mmbsGETBIOMapi(personId,nric);
            apIs.identify(UUID.randomUUID().toString(),TdNo,natCd,dob,terminal_mapped,direction,VDT_input,nric);

        }catch(Exception e){
            Log.info(e.getMessage());
        }
    }

}
