package com.example.kafka_test.database.profile_terminal.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="person")

public class profile_terminal {
    @Id
    String Id;

    public profile_terminal(){

    }

}