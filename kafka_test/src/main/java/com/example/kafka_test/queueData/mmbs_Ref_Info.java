package com.example.kafka_test.queueData;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter

public class mmbs_Ref_Info {
    String datasrc;
    String primaryExternalId;
    String secondaryExternalId;

}
