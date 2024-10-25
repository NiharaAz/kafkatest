package com.example.kafka_test.database.itinerary.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="ics_itinerary")

public class itinerary {

    String ITIN_ID;
    String STATUS;
    String ID_NO;
    String TD_NO;
    String NAT_CD;
    String DATE_OF_BIRTH;
    String TERMINAL;
    String DIRECTION;
    String VALIDITY_END;
    String PERSON_ID;
    @Id
    String TRANSLATED_ID;
    String IS_PROPAGATE;


}
