package com.example.kafka_test;

import com.example.kafka_test.queueData.ICS_data;
import com.example.kafka_test.queueData.Itinerary_Info;
import com.example.kafka_test.queueData.mmbs_Ref_Info;
import com.example.kafka_test.queueData.traveller_Info;

public class SetICSData {


    public ICS_data SetData() {
        ICS_data icsData= new ICS_data();

        traveller_Info t= new traveller_Info();
        t.setDobTxt(20010901);
        t.setIdNo("S1003D");
        t.setTdNo("E1234567K");
        t.setNatCd("SG");

        t.setEligibleForContactless(true);

        Itinerary_Info a= new Itinerary_Info();
        //a.setChkptCd("C");
        a.setItineraryId("0999");
        a.setStatInOut("I");
        a.setValidityEndDateTime("2025-06-30T10:15:20Z");

        mmbs_Ref_Info m = new mmbs_Ref_Info();
        m.setDatasrc("TRANSIENT");
        m.setSecondaryExternalId("B1900132212");
        m.setSecondaryExternalId("S7128971J");

        icsData.setItineraryInfo(a);
        icsData.setTravellerInfo(t);
        icsData.setMmbsRefInfo(m);

        return icsData;
    }
}
