package com.example.kafka_test.database;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AuthenticateRequest {
    private List<Biometric> biometric;
    private uid UID;

}
