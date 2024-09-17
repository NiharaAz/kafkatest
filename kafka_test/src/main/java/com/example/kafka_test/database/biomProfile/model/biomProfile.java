package com.example.kafka_test.database.biomProfile.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="BIOM_PROFILE")
public class biomProfile {
    String HAVE_IMAGES;
    String personId;

}
