/*
package com.example.kafka_test;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.stereotype.Component;

@Component
@ShellComponent
public class ConsumeMessage {

    @ShellMethod
    @KafkaListener(topics = "T.ICS.EXT.TTP.P3",groupId = "groupId")
    public void listenGroup(String message) {
        System.out.println("Received Message in group : " + message);

    }
}
*/
