package com.example.kafka_test.database.personDomain.repository;

import com.example.kafka_test.database.personDomain.model.personDomain;
import com.example.kafka_test.database.profile.model.profile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface personDomainDAO extends CrudRepository<personDomain,String>{


}
