package com.example.kafka_test.database.profile.repository;

import com.example.kafka_test.database.profile.model.profile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface profileDAO extends CrudRepository<profile,String> {
}
