package com.example.kafka_test;

import com.example.kafka_test.database.validate_controller;
import com.example.kafka_test.queueData.ICS_data;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ShellComponent
public class DemoApplication  {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    validate_controller validateController;
    String Topic="T.ICS.TTP.EXT";

    @ShellMethod
    public void sendMessage(){
        SetICSData setICSData= new SetICSData();
        ICS_data data= setICSData.SetData();

        //kafkaTemplate.send(Topic,data);
        List<Header> headers= new ArrayList<>();
        headers.add(new RecordHeader("msgActCd", "A".getBytes()));
        headers.add(new RecordHeader("msgCreateDatetime","2020-08-24T14:15:22+08:00".getBytes()));
        

        var record= new ProducerRecord<String, ICS_data>(Topic,data);
        record.headers().add("msgActCd", "A".getBytes());
        record.headers().add("msgCreateDatetime","2020-08-24T14:15:22+08:00".getBytes());
        kafkaTemplate.send((Message<?>) record);


    }
    @ShellMethod
    public void SendToKafka(ICS_data data) throws JsonProcessingException {
        //ICS_data data= SetICSData.SetData();

        System.out.println(data.toString());
        ObjectMapper objectMapper= new ObjectMapper();
        String jsonString= objectMapper.writeValueAsString(data);
        System.out.println("jsonString is" +jsonString);
        ProducerRecord<String,String> record= new ProducerRecord<>(this.Topic,jsonString);
        record.headers().add("msgActCd", "A".getBytes());
        record.headers().add("msgCreateDatetime","2020-08-24T14:15:22+08:00".getBytes());
        kafkaTemplate.send(record);

    }

    @ShellMethod
    public void enroll() throws JsonProcessingException {
        SetICSData setICSData= new SetICSData();
        ICS_data data= setICSData.SetData();

        //ingest into kafka queue
        SendToKafka(data);


        //validate if personKey in ingested data is == personKey in DB
        String dob= String.valueOf(data.getTravellerInfo().getDobTxt());
        String natCd= data.getTravellerInfo().getNatCd();
        String tdNo = data.getTravellerInfo().getTdNo();
        String personId = validateController.validate_personKey(tdNo,natCd,dob);
        //validate if itineraryId in ingested data is == itineraryId in DB
        validateController.validate_itineraryId(data.getTravellerInfo().getIdNo());
        //validate if personId in ingested data is == personId in oneId DB
        validateController.validate_central_personId(personId);

    }

}
