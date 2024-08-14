package com.example.kafka_test.database.itinerary.repository;

import com.example.kafka_test.database.itinerary.model.itinerary;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItineraryDAO extends CrudRepository<itinerary, String> {

}
