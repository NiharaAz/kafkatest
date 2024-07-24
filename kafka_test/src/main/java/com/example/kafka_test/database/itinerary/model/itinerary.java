package com.example.kafka_test.database.itinerary.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="ITINERARY")

public class itinerary {
    @Id
    String ID;
    String TD_NO;
    String NAT_CD;
    String BIRTH_DATE;
    String TRANSLATED_ID;
    String MESSAGE_DATETIME;
    String AUDIT_DATETIME;
    public itinerary(){

    }

}
