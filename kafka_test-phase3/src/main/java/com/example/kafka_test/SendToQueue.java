package com.example.kafka_test;

import com.example.kafka_test.queueData.ICS_data;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PreDestroy;
import kafkaResponse.KafkaJsonWhole;
import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConsumerSeekAware;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import static com.example.kafka_test.DemoApplication.Log;

@Component
//@ShellComponent
public class SendToQueue implements ConsumerSeekAware {
    @Autowired
    private KafkaTemplate<String, String> Template;

    private final String Topic = "T.ICS.EXT.TTP.P3";

    @Getter @Setter
    String tdNo,natCd,direction,dob,vdet;

    @Autowired
    KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;


    public void SendToKafka(ICS_data data) throws JsonProcessingException {
        Log.info("*** Sending msg to Kafka Queue ***");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(data);

        //ingest into kafka queue
        ProducerRecord<String, String> record = new ProducerRecord<>(this.Topic, jsonString);
        record.headers().add("msgActCd", "A".getBytes());
        record.headers().add("msgCreateDateTime", "2021-08-24T18:15:01Z".getBytes());
        Template.send(record);
    }

    public void SendToKafkaFromjsonString(String jsonString) throws JsonProcessingException {
        Log.info("*** Sending msg to Kafka Queue ***");

        //ingest into kafka queue
        ProducerRecord<String, String> record = new ProducerRecord<>(this.Topic, jsonString);
        record.headers().add("msgActCd", "A".getBytes());
        record.headers().add("msgCreateDateTime", "2021-08-24T18:15:01Z".getBytes());
        Template.send(record);
    }



    @KafkaListener(id="id3",  groupId ="#{@dynamicGrpId}",topics = "QUE.V3.ONEID.ENROLL.ITIN.AND.BIOMETRIC.SIN1",autoStartup = "false",containerFactory = "kafkaListenerContainerFactory"
    )
    public void ConsumeFromKafka(String message) throws JsonProcessingException {

        Log.info("*** Consuming msg from Kafka Queue ***");

       // System.out.println("Received Message in group : " + message);
        ObjectMapper mapper = new ObjectMapper();
        KafkaJsonWhole kafkaData = mapper.readValue(message, KafkaJsonWhole.class);
        //System.out.println("icsData1 is " + kafkaData);
        String pptnum=kafkaData.getData().getDoc().get(0).getPptNum();
        String natCd= kafkaData.getData().getDoc().get(0).getNatcd();
        String dob= kafkaData.getData().getDoc().get(0).getDob();
        String idNo= kafkaData.getData().getDoc().get(0).getOptionalElement();
        String direction= kafkaData.getData().getDoc().get(1).getDirection();
        String VDET= kafkaData.getData().getDoc().get(1).getFltArrvlDateTime();
        System.out.println(" PersonId from data is " + kafkaData.getData().getPersonId());
        System.out.println("tdno is "+pptnum+"natcd is "+natCd+"dob is "+dob+"idno is "
        +idNo+"direction is "+direction+"VDET is"+VDET);
        setTdNo(pptnum);
        setDob(dob); setDirection(direction); setNatCd(natCd);
        setVdet(VDET);

        //     return pptnum;
//        ICS_data ics_data= mapper.convertValue(message,ICS_data.class);
//        System.out.println("icsData2 is "+ics_data);

    }

    public void awaitMessage() {
    }
}
