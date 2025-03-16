package com.example.kafka_test.database;


import com.example.kafka_test.database.biomProfile.model.biomProfile;
import com.example.kafka_test.database.biomProfile.model.biomProfileImage;
import com.example.kafka_test.database.document_terminal.model.document_passport;
import com.example.kafka_test.database.document_terminal.model.document_terminal;
import com.example.kafka_test.database.document_terminal.repository.document_passportDAO;
import com.example.kafka_test.database.document_terminal.repository.document_terminalDAO;
import com.example.kafka_test.database.itinerary.model.itinerary;
import com.example.kafka_test.database.itinerary.repository.ItineraryDAO;
import com.example.kafka_test.database.personDomain.model.personDomain;
import com.example.kafka_test.database.profile_terminal.model.profile_terminal;
import com.example.kafka_test.database.profile_terminal.repository.profile_terminalDAO;
import com.example.kafka_test.database.personDomain.repository.personDomainDAO;
import com.example.kafka_test.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

@Component
public class validate_controller {

    static final Logger Log = LoggerFactory.getLogger(validate_controller.class);

    @Autowired
    ItineraryDAO itineraryDAO;
    @Autowired
    profile_terminalDAO profileTerminalDAO;
    @Autowired
    personDomainDAO person_Domain_DAO;
    @Autowired
    com.example.kafka_test.database.biomProfile.repository.biomProfileDAO biomProfileDAO;
    @Autowired
    com.example.kafka_test.database.biomProfile.repository.biomProfileImageDAO biomProfileImageDAO;
    @Autowired
    document_passportDAO documentPassportDAO;
    @Autowired
    document_terminalDAO documentTerminalDAO;

    Utilities utilities=new Utilities();

    public String validate_personKey(String tdNo, String natCd, String dob) throws Exception {
        Log.info("*** Validating personKey **** ");
        Thread.sleep(5000);

        String pKey1= tdNo+"_"+natCd+"_"+dob;
        Log.info("pkey is "+pKey1);

        Iterable<personDomain> p = person_Domain_DAO.findAll();
        String  personId = null;
        boolean found = false;

        for (personDomain pd : p){

             if (pd.getPKEY().equals(pKey1))
             {
                 personId = pd.getPERSON_ID();
                 found=true;
                 break;
            }
        }
        if (found){
            Log.info(" **** PASSED - person key is validated ***"+pKey1);
            //Log.info("person is found. person id is "+personId+" person key is "+pKey1);
        }else{
            throw new Exception(" **** FAILED - person key is  not validated *** person key = "+pKey1);
        }
        return personId;

    }

    public String validate_itineraryId(String itinId, String nric, String tdNo, String natCd, String dob, String terminal,
                                      String direction, String VDT, String personId) throws Exception {
        Log.info("*** Validating ics_itinerary table **** ");

        String VDT_UTC = utilities.ChangeVDT_To_UTC(VDT);

        Iterable<itinerary> icsItinerayIterable = itineraryDAO.findAll();

        boolean found = false;
        String TranslatedId = null;
        for (itinerary itinerary : icsItinerayIterable) {
            /*Log.info("printing null nric "+itinerary.getID_NO());
            Log.info("itinerry dob is "+itinerary.getDATE_OF_BIRTH());
            Log.info("itineray itin id is"+itinerary.getITIN_ID()+"itineray direction is "+itinerary.getDIRECTION()+"natcd is "+itinerary.getNAT_CD()
            +"personid is "+itinerary.getPERSON_ID()+"tdno is"+itinerary.getTD_NO()+"vdet is " +itinerary.getVALIDITY_END().split("\\.")[0]);
*/
            if (itinerary.getITIN_ID().equals(itinId)
                    && itinerary.getID_NO().equals(nric)
                    && itinerary.getNAT_CD().equals(natCd)
                    && itinerary.getTD_NO().equals(tdNo)
                    && itinerary.getPERSON_ID().equals(personId)
                    // added new from here
                    && itinerary.getDATE_OF_BIRTH().equals(dob)
                    && itinerary.getDIRECTION().equals(direction)
                    && itinerary.getTERMINAL().equals(terminal))
                     {
                        found = true;
                        TranslatedId = itinerary.getTRANSLATED_ID();
                        break;
            }
        }
        if (found) {
            Log.info(" **** ICS_ITNERARAY validation PASSED ***");
           // Log.info("person is found. itin id is " + itinId);
        } else {
            throw new Exception(" **** ICS_ITNERARAY validation FAILED ");
        }

        return TranslatedId;

    }


    public void validate_terminal_personId(String personId) throws Exception {
        Log.info("*** Validating Profile's person Id **** ");
        boolean found =false;

        Iterable<profile_terminal> profile = profileTerminalDAO.findAll();

        if (profileTerminalDAO.existsById(personId)){
            found=true;
        }

        if (found){
            Log.info(" **** PROFILE TERMINAL PERSON table validation PASSED ***");
        }else{
            throw new Exception("*** PROFILE TERMINAL PERSON table validation FAILED ***");
        }
    }

    /*@ShellMethod
    public void count_bcbp(){
        Log.info("*** Counting No.of DOCUMENT_BCBP **** ");

        boolean found=false;
        long num= documentDAO.count();
        Log.info("count is"+num);
    }*/
    public void validate_doc_passport(String dob, String natCd, String pptNum, String nric) throws Exception {
        Log.info("*** Validating Profile Terminal's DOCUMENT_PASSPORT **** ");

        boolean found=false;
        Iterable<document_passport> documentPassportIterable= documentPassportDAO.findAll();

        for (document_passport doc_passport: documentPassportIterable)
        {
            if (doc_passport.getDob().equals(dob) && doc_passport.getNat_cd().equals(natCd)
            && doc_passport.getPpt_num().equals(pptNum) && doc_passport.getOptional_element().equals(nric))
            {
                found=true;
                break;
            }
        }
        if (found){
            Log.info(" **** PROFILE TERMINAL PASSPORT table validation PASSED ***");
            //Log.info("person is found - dob is "+dob + "natcd is "+natCd+"pptNum is "+pptNum+ "nric is "+nric);
        }else{
            throw new Exception("**** PROFILE TERMINAL PASSPORT table validation FAILED ***");
        }
    }


    public void validate_doc_bcbp(String direction, String terminal, String flt_arrvl_date,String docId) throws Exception {
        Log.info("*** Validating Profile Terminal's DOCUMENT_BCBP **** ");

        String flt_arrvl_date_SG = utilities.ChangeVDTtoSGT(flt_arrvl_date);
        boolean found = false;
        Iterable<document_terminal> documentTerminalIterable = documentTerminalDAO.findAll();


        for (document_terminal doc : documentTerminalIterable) {
            if (doc.getDirection().equals(direction) && doc.getTerminal().equals(terminal)
                    && doc.getFlt_arrvl_date_time().split("\\.")[0].equals(flt_arrvl_date_SG)
            && doc.getDoc_id().equals(docId)) {
                found = true;

                break;
            }
        }
        if (found) {
            Log.info("**** PROFILE TERMINAL BCBP table validation PASSED ***");
            //Log.info("person is found - direction is " + direction + "terminal is " + terminal + "flt_arrvl_datetime is " + flt_arrvl_date);

        } else {
            throw new Exception("**** PROFILE TERMINAL BCBP table validation FAILED ***");
        }

    }


    public void validate_biom_profile(String personId, String HAS_IMAGES){
        Log.info("*** Validating biometrics_domain's biom profile  **** ");

        boolean found=false;
        Iterable<biomProfile> biomProfileIterable= biomProfileDAO.findAll();
        for(biomProfile bio: biomProfileIterable){
            if(bio.getHAS_IMAGES().equals(HAS_IMAGES) && bio.getPERSON_ID().equals(personId)){
                found=true;
            }
        }
        if (found){
            Log.info(" **** BIOM DOMAIN BIOM_IMAGE table validation PASSED ***");
            //Log.info("person is found n biom_profile table- has_images is "+HAS_IMAGES + "personid is "+personId);
        }else {
            Log.info("**** BIOM DOMAIN BIOM_IMAGE table validation FAILED ***");
        }
    }

    public void validate_biom_profile_image(String personId)
    {
        Log.info("**** validating biometrics_domain's biom_profile_image table ***");
        boolean found=false;

        Iterable<biomProfileImage> biomProfileIterable= biomProfileImageDAO.findAll();

        for(biomProfileImage bio: biomProfileIterable){
            Log.info("printing fingerprint indicator "+bio.getFINGERPRINT_INDICATOR());
            Log.info("printing iris  indicator "+bio.getIRIS_INDICATOR());
            Log.info("printing face "+bio.getFACE_INDICATOR());

            if(bio.getPERSON_ID().equals(personId) && !bio.getIMAGE().isEmpty()
            && bio.getFACE_INDICATOR().equals("1") && !bio.getFINGERPRINT_INDICATOR().equals("0")){
                found=true;
            }
        }
        if (found){
          //  Log.info(" **** test case passed ***");
            Log.info("**** BIOM DOMAIN BIOM_PROFILE_IMAGE table validation PASSED ***");
        }else {
            Log.info("**** BIOM DOMAIN BIOM_PROFILE_IMAGE table validation FAILED ");
        }
    }

}

