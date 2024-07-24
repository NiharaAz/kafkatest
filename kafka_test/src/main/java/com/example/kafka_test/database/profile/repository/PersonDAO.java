package com.example.kafka_test.database.profile.repository;

import com.example.kafka_test.database.profile.model.person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonDAO extends CrudRepository<person,String> {
}
