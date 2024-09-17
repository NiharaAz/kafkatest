package com.example.kafka_test.database.document_terminal.repository;

import com.example.kafka_test.database.document_terminal.model.document_terminal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface document_terminalDAO extends CrudRepository<document_terminal,String> {
}
