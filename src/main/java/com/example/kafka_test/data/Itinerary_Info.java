package com.example.kafka_test.data;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter @Getter
public class Itinerary_Info {
    String chkptCd;
    String statInOut;
    String validityEndDateTime;
    String itineraryId;

}
