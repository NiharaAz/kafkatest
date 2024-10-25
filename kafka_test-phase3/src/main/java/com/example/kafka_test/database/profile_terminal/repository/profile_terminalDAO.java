package com.example.kafka_test.database.profile_terminal.repository;

import com.example.kafka_test.database.profile_terminal.model.profile_terminal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface profile_terminalDAO extends CrudRepository<profile_terminal,String> {
}
