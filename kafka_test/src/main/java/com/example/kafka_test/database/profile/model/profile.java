package com.example.kafka_test.database.profile.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="person")

public class profile {
    @Id
    String Id;

    public profile(){

    }

}