package com.example.kafka_test;

import com.example.kafka_test.database.validate_controller;
import com.example.kafka_test.queueData.ICS_data;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.kafka.core.KafkaTemplate;

import org.springframework.stereotype.Component;


@Component
public class updateBiomDemoApplication {
    @Autowired
    private KafkaTemplate<String, String> Template;
    @Autowired
    validate_controller validateController;

    @Autowired
    Utilities utilities;
    private final String Topic="T.ICS.EXT.TTP.P3";

    @Autowired
    private ResourceLoader resourceLoader;
    public void SendToKafka(ICS_data data) throws JsonProcessingException {
        DemoApplication.Log.info("*** Sending msg to Kafka Queue ***");
        ObjectMapper objectMapper= new ObjectMapper();
        String jsonString= objectMapper.writeValueAsString(data);

        //ingest into kafka queue
        ProducerRecord<String,String> record= new ProducerRecord<>(this.Topic,jsonString);
        record.headers().add("msgActCd", "A".getBytes());
        record.headers().add("msgCreateDateTime","2021-08-24T18:15:01Z".getBytes());
        Template.send(record);
    }


}
