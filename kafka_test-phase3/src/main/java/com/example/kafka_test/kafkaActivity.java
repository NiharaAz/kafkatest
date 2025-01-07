package com.example.kafka_test;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.DeleteConsumerGroupsResult;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.common.KafkaFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;

import javax.sound.midi.SysexMessage;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

@Component
public class kafkaActivity {

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;


    public boolean startListener(String id){
        MessageListenerContainer container= kafkaListenerEndpointRegistry.getListenerContainer(id);
        container.start();
        System.out.println("Listener started" +id);
        return true;
    }

    public boolean stopListener (String id){
        MessageListenerContainer container= kafkaListenerEndpointRegistry.getListenerContainer(id);
        container.stop();
        System.out.println("Listener stopped "+id);
        return true;

    }

    public void DeleteConsumer() throws ExecutionException{
        //Map<String, Object> config = new HashMap<>();
        Properties config= new Properties();
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "172.16.24.71:9094");
        AdminClient admin = KafkaAdminClient.create(config);
        String a[] = new String[]{"test123"};

        DeleteConsumerGroupsResult deleteConsumerGroupsResult = admin.deleteConsumerGroups(Arrays.asList(a));

        KafkaFuture<Void> resultFuture = deleteConsumerGroupsResult.all();
        try {
            resultFuture.get();
        }catch (Exception e){
            System.out.println("exception logged" +e.getMessage());
        }

        admin.close();
    }


}
