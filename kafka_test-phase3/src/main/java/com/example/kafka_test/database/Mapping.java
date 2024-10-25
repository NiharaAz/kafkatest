package com.example.kafka_test.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Mapping {

    BioData bioData;

    @Autowired
    public Mapping(BioData bioData) {
        this.bioData = bioData;
    }


    public Map<String, List<Biometric>> mapping_of_biom_to_nric() throws IOException {
        Biometric biometric1 = new Biometric();
        biometric1.setBioType("FACE");
        biometric1.setBioSubType("FACE");
        biometric1.setBioData(bioData.getFaceBioData());
        biometric1.setFormat("JPG");

        Biometric biometric2 = new Biometric();
        biometric2.setBioType("FINGER");
        biometric2.setBioSubType("LEFT_THUMB");
        biometric2.setBioData(bioData.getThumbBioData()); // Fixed to use bioData
        biometric2.setFormat("WSQ");

        Biometric biometric3 = new Biometric();
        biometric3.setBioType("IRIS");
        biometric3.setBioSubType("LEFT_EYE");
        biometric3.setBioData(bioData.getLeftEyeBioData()); // Fixed to use bioData
        biometric3.setFormat("PNG");

        Biometric biometric4= new Biometric();
        biometric4.setBioType("IRIS");
        biometric4.setBioSubType("RIGHT_EYE");
        biometric4.setFormat("PNG");
        biometric4.setBioData(bioData.getRightEyeBioData());

        Map<String, List<Biometric>> biometricsRequestMap = new HashMap<>();
        biometricsRequestMap.put("S1002D", List.of(biometric1, biometric2));
        biometricsRequestMap.put("S1003D", List.of(biometric1, biometric3));
        biometricsRequestMap.put("S1004D",List.of(biometric1,biometric3,biometric4));

        return biometricsRequestMap;

    }

    public Map<String,String> mappingCheckpoint(){
        Map<String,String> chkpt= new HashMap<>();
        chkpt.put("C","SIN1");
        chkpt.put("D","SIN2");
        return chkpt;

    }
}
