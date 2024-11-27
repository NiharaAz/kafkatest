package com.example.kafka_test;

import com.example.kafka_test.database.Mapping;
import com.example.kafka_test.database.validate_controller;
import com.example.kafka_test.queueData.ICS_data;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import static com.example.kafka_test.DemoApplication.Log;


@Component
public class updateItinDemoApplication {
    @Autowired
    private KafkaTemplate<String, String> Template;
    @Autowired
    validate_controller validateController;
    @Autowired
    Utilities utilities;
    private final String Topic="T.ICS.EXT.TTP.P3";

    @Autowired
    Mapping mapping;

    @Autowired
    private ResourceLoader resourceLoader;

    public void SendToKafka(ICS_data data) throws JsonProcessingException {
        Log.info("*** Sending msg to Kafka Queue ***");
        ObjectMapper objectMapper= new ObjectMapper();
        String jsonString= objectMapper.writeValueAsString(data);

        //ingest into kafka queue
        ProducerRecord<String,String> record= new ProducerRecord<>(this.Topic,jsonString);
        record.headers().add("msgActCd", "A".getBytes());
        record.headers().add("msgCreateDateTime","2021-08-24T18:15:01Z".getBytes());
        Template.send(record);
    }

    //@ShellMethod
    public void updateVDET(String TdNo, String ItinId, String nric,String VDT,String terminal) throws Exception {
        Log.info( " **** Testing update VDET test case ***" );

        SetICSData setICSData= new SetICSData();
        ICS_data data= setICSData.SetData(TdNo, ItinId,nric,VDT,terminal);
        SendToKafka(data);
        String natCd= data.getTravellerInfo().getNatCd();
        String dob= data.getTravellerInfo().getDobTxt();
        String personId = validateController.validate_personKey(TdNo,natCd,dob);
        terminal="SIN1";
        String translatedId = validateController.validate_itineraryId(ItinId,nric,TdNo,natCd,dob,terminal,"I",VDT,personId);
        validateController.validate_doc_bcbp("I",terminal,VDT,translatedId);
        Scanner sc= new Scanner(System.in);
        System.out.println("whats the updated VDET?");
        String VDT_update=sc.nextLine();

        ICS_data data1= setICSData.SetData(TdNo,ItinId,nric,VDT_update,terminal);
        SendToKafka(data1);
        String translatedId1 =validateController.validate_itineraryId(ItinId,nric,TdNo,natCd,dob,
                "SIN1","I",VDT_update,personId);
        validateController.validate_doc_bcbp("I","SIN1",VDT_update,translatedId1);
    }


    public void updateVDET_hardcoded() throws Exception {
        Log.info( " **** Testing update VDET test case ***" );

        utilities.dropTable();

        String terminal="C";
        String TdNo="345";
        String ItinId= UUID.randomUUID().toString();
        String nric="S1002D";
        String VDT = "2028-11-11T00:09:00Z";
        String VDT_update= "2028-12-12T01:00:00+08:00";

        SetICSData setICSData= new SetICSData();
        ICS_data data= setICSData.SetData(TdNo, ItinId,nric,VDT,terminal);
        SendToKafka(data);
        String natCd= "SG";
        String dob= data.getTravellerInfo().getDobTxt();
        String personId = validateController.validate_personKey(TdNo,natCd,dob);
        if(personId==null){
            Log.info("*** person not found in personKEY table ***");
            return;
        }
        terminal="SIN1";
        String translatedId =validateController.validate_itineraryId(ItinId,nric,TdNo,natCd,dob,terminal,"I",VDT,personId);
        validateController.validate_doc_bcbp("I",terminal,VDT,translatedId);


        ICS_data data1= setICSData.SetData(TdNo,ItinId,nric,VDT_update,"C");
        SendToKafka(data1);
        Thread.sleep(5000);
        translatedId =validateController.validate_itineraryId(ItinId,nric,TdNo,natCd,dob,
                terminal,"I",VDT_update,personId);
        validateController.validate_doc_bcbp("I",terminal,VDT_update,translatedId);

        //Log.info("*** test case passed - VDET is updated ***");
    }

    //@ShellMethod
    public void updateChkpt (String TdNo, String ItinId, String nric,String VDT ,String terminal) throws Exception {
        Log.info( " **** Testing update checkpoint test case ***" );

        SetICSData setICSData= new SetICSData();
        ICS_data data= setICSData.SetData(TdNo, ItinId,nric,VDT,terminal);
        SendToKafka(data);
        String natCd= data.getTravellerInfo().getNatCd();
        String dob= data.getTravellerInfo().getDobTxt();
        String personId = validateController.validate_personKey(TdNo,natCd,dob);
        String translatedId =validateController.validate_itineraryId(ItinId,nric,TdNo,natCd,dob,"SIN1","I",VDT,personId);
        validateController.validate_doc_bcbp("I","SIN1",VDT,translatedId);

        Scanner sc= new Scanner(System.in);
        System.out.println("whats the updated checkpoint?");
        String terminal_update=sc.nextLine();
        //Mapping map= new Mapping();
        Map<String,String> chkpt = mapping.mappingCheckpoint();
        String chkpt_update= chkpt.get(terminal_update);

        ICS_data data1= setICSData.SetData(TdNo,ItinId,nric,VDT,terminal_update);
        SendToKafka(data1);
        translatedId =validateController.validate_itineraryId(ItinId,nric,TdNo,natCd,dob, chkpt_update,"I",VDT,personId);
        validateController.validate_doc_bcbp("I",chkpt_update,VDT,translatedId);

    }


    public void updateChkpt_hardcoded () throws Exception {
        Log.info( " **** Testing update checkpoint test case ***" );

        utilities.dropTable();
        String terminal="C";
        String TdNo="345";
        String ItinId= "789";
        String nric="S1002D";
        String VDT = "2028-11-11T00:09:00Z";
        String terminal_update="D";
        String personId = null;
        
        SetICSData setICSData= new SetICSData();
        ICS_data data= setICSData.SetData(TdNo, ItinId,nric,VDT,terminal);
        SendToKafka(data);
        String natCd= data.getTravellerInfo().getNatCd();
        String dob= data.getTravellerInfo().getDobTxt();
        
        try{
            personId = validateController.validate_personKey(TdNo,natCd,dob);
            String translatedId =validateController.validate_itineraryId(ItinId,nric,TdNo,natCd,dob,"SIN1","I",VDT,personId);
            validateController.validate_doc_bcbp("I","SIN1",VDT,translatedId);

            Map<String,String> chkpt = mapping.mappingCheckpoint();
            String chkpt_update= chkpt.get(terminal_update);

            Log.info("*** sending terminal = SIN2 ***");
            ICS_data data1= setICSData.SetData(TdNo,ItinId,nric,VDT,terminal_update);
            SendToKafka(data1);
            Thread.sleep(5000);


            validateController.validate_itineraryId(ItinId,nric,TdNo,natCd,dob, "SIN2","I",VDT,personId);
            //different terminal cannot do identify request
            Log.info("**** test case passed - Chkpt updated ***");

        }catch(Exception e){
            Log.info(e.getMessage());
        }

    }


}
