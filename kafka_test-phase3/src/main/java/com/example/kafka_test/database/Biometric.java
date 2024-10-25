package com.example.kafka_test.database;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Biometric {
    private String bioType; // Base64 or URL of the face image
    private String bioSubType; // Base64 or URL of the left iris image (optional)
    private String bioData;
    private String format; // Format of the images (e.g., "jpeg")


}
