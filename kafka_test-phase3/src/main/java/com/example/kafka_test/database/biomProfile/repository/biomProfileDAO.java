package com.example.kafka_test.database.biomProfile.repository;

import com.example.kafka_test.database.biomProfile.model.biomProfile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface biomProfileDAO extends CrudRepository<biomProfile,String> {
}
