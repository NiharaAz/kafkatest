package com.example.kafka_test.database.profile.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="help")

public class person {
    @Id
    String Id;
    String name;
    public person(){

    }

}