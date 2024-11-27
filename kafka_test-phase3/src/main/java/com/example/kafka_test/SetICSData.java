package com.example.kafka_test;

import com.example.kafka_test.queueData.ICS_data;
import com.example.kafka_test.queueData.Itinerary_Info;
import com.example.kafka_test.queueData.mmbs_Ref_Info;
import com.example.kafka_test.queueData.traveller_Info;

public class SetICSData {


    public ICS_data SetData(String TdNo, String ItinId, String nric, String VDT, String terminal) {
        ICS_data icsData= new ICS_data();

        traveller_Info t= new traveller_Info();
        t.setDobTxt("20010901");
        t.setIdNo(nric);
        t.setTdNo(TdNo);
        t.setNatCd("SG");

        t.setEligibleForContactless(true);

        Itinerary_Info a= new Itinerary_Info();
        a.setChkptCd(terminal);
        a.setItineraryId(ItinId);
        a.setStatInOut("I");
        a.setValidityEndDateTime(VDT);

        mmbs_Ref_Info m = new mmbs_Ref_Info();
        m.setDataSrc("TRANSIENT");
        m.setPrimaryExternalId("B1900132212");
        m.setSecondaryExternalId("S7128971J");

        icsData.setItineraryInfo(a);
        icsData.setTravellerInfo(t);
        icsData.setMmbsRefInfo(m);

        return icsData;
    }

    public ICS_data SetDataWithdob(String TdNo,String ItinId,String nric,String VDT_input,String terminal,String dob1) {
        ICS_data icsData= new ICS_data();

        traveller_Info t= new traveller_Info();
        t.setDobTxt(dob1);
        t.setIdNo(nric);
        t.setTdNo(TdNo);
        t.setNatCd("SG");

        t.setEligibleForContactless(true);

        Itinerary_Info a= new Itinerary_Info();
        a.setChkptCd(terminal);
        a.setItineraryId(ItinId);
        a.setStatInOut("I");
        a.setValidityEndDateTime(VDT_input);

        mmbs_Ref_Info m = new mmbs_Ref_Info();
        m.setDataSrc("TRANSIENT");
        m.setPrimaryExternalId("B1900132212");
        m.setSecondaryExternalId("S7128971J");

        icsData.setItineraryInfo(a);
        icsData.setTravellerInfo(t);
        icsData.setMmbsRefInfo(m);

        return icsData;
    }

    public ICS_data SetDatawithnatCd(String TdNo, String ItinId, String nric, String VDT,  String terminal,String natCd) {
        ICS_data icsData= new ICS_data();

        traveller_Info t= new traveller_Info();
        t.setDobTxt("20090101");
        t.setIdNo(nric);
        t.setTdNo(TdNo);
        t.setNatCd(natCd);

        t.setEligibleForContactless(true);

        Itinerary_Info a= new Itinerary_Info();
        a.setChkptCd(terminal);
        a.setItineraryId(ItinId);
        a.setStatInOut("I");
        a.setValidityEndDateTime(VDT);

        mmbs_Ref_Info m = new mmbs_Ref_Info();
        m.setDataSrc("TRANSIENT");
        m.setPrimaryExternalId("B1900132212");
        m.setSecondaryExternalId("S7128971J");

        icsData.setItineraryInfo(a);
        icsData.setTravellerInfo(t);
        icsData.setMmbsRefInfo(m);

        return icsData;
    }
}
