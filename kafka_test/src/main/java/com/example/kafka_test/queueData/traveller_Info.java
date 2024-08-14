package com.example.kafka_test.queueData;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class traveller_Info {
    String idNo;
    String tdNo;
    String natCd;
    int dobTxt;
    boolean eligibleForContactless;

}
