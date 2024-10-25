package com.example.kafka_test.database.biomProfile.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="BIOM_PROFILE_IMAGE")
public class biomProfileImage {
    @Id
    String PERSON_ID;
    String IMAGE;
    String FACE_INDICATOR;
    String FINGERPRINT_INDICATOR;
    String IRIS_INDICATOR;

}
