package com.example.kafka_test.queueData;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data

public class ICS_data {

    Itinerary_Info itineraryInfo;
    mmbs_Ref_Info mmbsRefInfo;
    traveller_Info travellerInfo;


}
