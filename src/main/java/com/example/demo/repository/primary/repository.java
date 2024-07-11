package com.example.demo.repository.primary;

import com.example.demo.model.primary.itinerary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface repository extends JpaRepository<itinerary, String> {

}
