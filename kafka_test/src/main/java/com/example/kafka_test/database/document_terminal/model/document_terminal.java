package com.example.kafka_test.database.document_terminal.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="bcbp")
public class document_terminal {
    @Id
    String doc_id;
    String direction;
    String terminal;
    String flt_arrvl_date_time;
}
