package com.example.kafka_test.database.document_terminal.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="passport")
public class document_passport {
    String dob;
    String nat_cd;
    String optional_element;
    String ppt_num;
    @Id
    String doc_id;

}
