package com.example.kafka_test.database;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BiometricsRequest {
    private List<Biometric> biometric;
    private String direction;
    private boolean returnPpt;


}
