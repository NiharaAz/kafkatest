package com.example.kafka_test.queueData;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

@Data
@Setter @Getter
public class Itinerary_Info {
    @Value("C")
    String chkptCd;
    String statInOut;
    String validityEndDateTime;
    String itineraryId;

}
