package com.example.kafka_test;

import com.example.kafka_test.database.validate_controller;
import com.example.kafka_test.queueData.ICS_data;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.jline.utils.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
@ShellComponent
public class DemoApplication  {

    @Autowired
    private KafkaTemplate<String, String> Template;
    @Autowired
    validate_controller validateController;
    String Topic="T.ICS.EXT.TTP";

    @Autowired
    private ResourceLoader resourceLoader;
    @ShellMethod
    public void sendMessage1() throws IOException {

        //Path file = ResourceUtils.getFile("classpath:txt.json").toPath();
        Log.info("test1");
        Path p = resourceLoader.getResource("classpath:txt.json").getFile().toPath();
        //InputStream inputStream = getClass().getClassLoader().getResourceAsStream("classpath:txt.json");
        Log.info("test2");

        //String exampleRequest = FileUtils.readFileToString(new File(file), StandardCharsets.UTF_8);
        byte[] bytes = Files.readAllBytes(p);
        Log.info("test3");
        String st= new String(bytes, StandardCharsets.UTF_8);
        Log.info("test4");
        ProducerRecord<String,String> record= new ProducerRecord<>(this.Topic,st);
        record.headers().add("msgActCd", "A".getBytes());
        record.headers().add("msgCreateDateTime","2020-08-24T14:15:22+08:00".getBytes());
        Template.send(record);

    }

    @ShellMethod
    public void sendMessage() throws IOException {

        byte[] data = this.getClass().getClassLoader()
                .getResourceAsStream("txt.json").readAllBytes();
        //ObjectMapper objectMapper= new ObjectMapper();

        String st= new String(data, StandardCharsets.UTF_8);
        //ICS_data car = objectMapper.readValue(new File(resource.getURI()),ICS_data.class);
        //String direction1 = car1.getItineraryInfo().getStatInOut();
        //String direction = car.getItineraryInfo().getStatInOut();
        //Log.info(direction);

        //Log.info(direction1);
        ProducerRecord<String,String> record= new ProducerRecord<>(this.Topic,st);
        record.headers().add("msgActCd", "A".getBytes(StandardCharsets.UTF_8));
        record.headers().add("msgCreateDateTime","2020-08-24T14:15:22+08:00".getBytes(StandardCharsets.UTF_8));
        Log.info("template is "+Template.toString());
        Template.send(record);


    }
    @ShellMethod
    public void SendToKafka(String jsonString) throws JsonProcessingException {
        Log.info("*** Sending msg to Kafka Queue ***");
     /*   ObjectMapper objectMapper= new ObjectMapper();*/
       /* SetICSData setICSData= new SetICSData();
        ICS_data data= setICSData.SetData(TdNo,ItinId);*/
       /* String jsonString= objectMapper.writeValueAsString(data);*/
       /* System.out.println("jsonString is" +jsonString);*/
        ProducerRecord<String,String> record= new ProducerRecord<>(this.Topic,jsonString);
        record.headers().add("msgActCd", "A".getBytes());
        record.headers().add("msgCreateDateTime","2021-08-24T18:15:01Z".getBytes());
        Template.send(record);
    }
    @ShellMethod
    public void SendToKafka_withoutenroll(String TdNo, String ItinId, String nric) throws JsonProcessingException {
        Log.info("*** Sending msg to Kafka Queue ***");
           ObjectMapper objectMapper= new ObjectMapper();
        SetICSData setICSData= new SetICSData();
        ICS_data data= setICSData.SetData(TdNo,ItinId,nric);
        String jsonString= objectMapper.writeValueAsString(data);
        System.out.println("jsonString is" +jsonString);
        ProducerRecord<String,String> record= new ProducerRecord<>(this.Topic,jsonString);
        record.headers().add("msgActCd", "A".getBytes());
        record.headers().add("msgCreateDateTime","2021-08-24T18:15:01Z".getBytes());
        Template.send(record);
    }

    @ShellMethod
    public void enroll(String TdNo, String ItinId, String nric) throws JsonProcessingException, InterruptedException {
        SetICSData setICSData= new SetICSData();
        ICS_data data= setICSData.SetData(TdNo, ItinId, nric);
        ObjectMapper objectMapper= new ObjectMapper();
        String jsonString= objectMapper.writeValueAsString(data);

        //ingest into kafka queue
        SendToKafka(jsonString);

        //validate if personKey in ingested data is == personKey in DB
        String dob= String.valueOf(data.getTravellerInfo().getDobTxt());
        String natCd= data.getTravellerInfo().getNatCd();

        Thread.sleep(3000);
        String personId = validateController.validate_personKey(TdNo,natCd,dob);
        //validate if itineraryId in ingested data is == itineraryId in
        Thread.sleep(3000);
        validateController.validate_itineraryId(ItinId);
        //validate if personId in ingested data is == personId in oneId DB
        validateController.validate_terminal_personId(personId);
        validateController.validate_doc_passport(dob,natCd,TdNo,nric);
        validateController.validate_doc_bcbp(data.getItineraryInfo().getStatInOut(),data.getItineraryInfo().getChkptCd(),data.getItineraryInfo().getValidityEndDateTime());
       // validateController.validate_biom_profile();

        //validateController.validate_doc_passport();

        //enroll two ppl and validate
        // personId as a list
        // get personid from list and check found =2 .
        //bcbp is 2. ppt is 2

        //enroll ingest msg
        //validate - personid
        //enroll update msg
        //validate person id in persondb. -> whats the new terminal


    }
    public void updateCkpoint(String TdNo, String ItinId, String nric) throws JsonProcessingException {
        SetICSData setICSData= new SetICSData();
        ICS_data data= setICSData.SetData(TdNo, ItinId,nric);
        //SendToKafka();

        //new data
        //ICS_data data1= setICSData.SetData();
        //();

       // validateController.validate_doc_bcbp(data1.getTravellerInfo().getNatCd(),data1.getItineraryInfo().getChkptCd());

    }
    public void enroll2person() throws IOException {
        sendMessage();//1st guy
        sendMessage(); //2nd guy

    }

}
