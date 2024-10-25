package com.example.kafka_test.database.personDomain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="PERSON_KEY")
public class personDomain {
    @Id
    String PKEY;
    String PERSON_ID;

}
