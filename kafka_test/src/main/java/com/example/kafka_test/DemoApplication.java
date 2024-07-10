package com.example.kafka_test;

import com.example.kafka_test.data.Itinerary_Info;
import com.example.kafka_test.data.mmbs_Ref_Info;
import com.example.kafka_test.data.traveller_Info;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaderMapper;
import org.springframework.messaging.MessageHeaders;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@ShellComponent
public class DemoApplication  {

    @Autowired
    private KafkaTemplate<String, ICS_data> kafkaTemplate;


    private static final String Topic="test";

    @ShellMethod
    public void sendMessage(){
        ICS_data data =SetData();

        //kafkaTemplate.send(Topic,data);
        List<Header> headers= new ArrayList<>();
        headers.add(new RecordHeader("msgActCd", "A".getBytes()));
        headers.add(new RecordHeader("msgCreateDatetime","2020-08-24T14:15:22+08:00".getBytes()));
        

        var record= new ProducerRecord<String, ICS_data>(Topic,data);
        record.headers().add("msgActCd", "A".getBytes());
        record.headers().add("msgCreateDatetime","2020-08-24T14:15:22+08:00".getBytes());
        kafkaTemplate.send(record);

        //kafkaTemplate.send()
    }

    private ICS_data SetData() {
        ICS_data icsData= new ICS_data();

        traveller_Info t= new traveller_Info();
        t.setDobTxt(20010901);
        t.setIdNo("S1003D");
        t.setTdNo("E1234567K");
        t.setNatCd("SG");
        t.setEligibleForContactless(true);

        Itinerary_Info a= new Itinerary_Info();
        a.setChkptCd("C");
        a.setItineraryId("0999");
        a.setStatInOut("I");
        a.setValidityEndDateTime("2025-06-30T10:15:20Z");
        a.setItineraryId("200");

        mmbs_Ref_Info m = new mmbs_Ref_Info();
        m.setDatasrc("TRANSIENT");
        m.setSecondaryExternalId("B1900132212");
        m.setSecondaryExternalId("S7128971J");

        icsData.setItineraryInfo(a);
        icsData.setTravellerInfo(t);
        icsData.setMmbsRefInfo(m);

        return icsData;
    }

}
