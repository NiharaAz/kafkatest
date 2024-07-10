package com.example.kafka_test;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class kafkaConfig {
    @Bean
    public ProducerFactory<String, ICS_data> producerFactory() {
        Map<String, Object> config = new HashMap<>();

        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(config);
    }


    @Bean
    public KafkaTemplate<String, ICS_data> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    //private static final String Topic="test";

    //@Autowired
    //private KafkaTemplate<String, Employee> kafkaTemplate;
   /* public void sendMessage(){
        Employee emp= new Employee();
        emp.setEmpId(1);
        emp.setFirstName("name");
        emp.setLastName("lasewt");
        kafkaTemplate.send(Topic,emp);
    }*/


}
