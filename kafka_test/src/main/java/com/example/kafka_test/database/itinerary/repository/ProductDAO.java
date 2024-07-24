package com.example.kafka_test.database.itinerary.repository;

import com.example.kafka_test.database.itinerary.model.itinerary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDAO extends CrudRepository<itinerary, String> {

}
