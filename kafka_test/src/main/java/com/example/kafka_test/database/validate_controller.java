package com.example.kafka_test.database;


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
import org.jline.utils.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.stereotype.Component;

import java.util.Collections;

@ShellComponent
@Component
public class validate_controller {

    @Autowired
    ItineraryDAO itineraryDAO;
    @Autowired
    profile_terminalDAO profileTerminalDAO;
    @Autowired
    personDomainDAO person_Domain_DAO;

    @Autowired
    document_passportDAO documentPassportDAO;

    @Autowired
    document_terminalDAO documentTerminalDAO;


    @ShellMethod
    public void validate_profile(){
        Iterable<profile_terminal> p = profileTerminalDAO.findAll();
        p.forEach(o->System.out.println(o.getId()));
    }

    @ShellMethod
    public String validate_personKey(String tdNo, String natCd, String dob){
        Log.info("*** Validating personKey **** ");

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
            Log.info(" **** test case passed ***");
            Log.info("person is found. person id is "+personId+" person key is "+pKey1);
        }else{
            Log.info("person is not found. person key ingested is ");
        }
        return personId;
    }
    @ShellMethod
    public void validate_itineraryId (String idNo){
        Log.info("*** Validating Itinerary Id **** ");
        boolean found=false;
        Iterable<itinerary> itineraries= itineraryDAO.findAll();

        for ( itinerary i: itineraries){
            System.out.println("itinerary id is "+i.getID());
            if (i.getID().equals(idNo)){
                found=true;
                break;
            }
        }
        if (found){
            Log.info(" **** test case passed ***");
            Log.info("Itinerary Id is found in ITINERARY table. Itineray Id is ");
        }else{
            Log.info("Itinerary Id is not found in ITINERARY table. Itineray Id is ");
        }
    }
    @ShellMethod
    public void validate_terminal_personId(String personId) {
        Log.info("*** Validating Profile's person Id **** ");
        boolean found =false;

        Iterable<profile_terminal> profile = profileTerminalDAO.findAll();
        if (profileTerminalDAO.existsById(personId)){
            found=true;
        }
        /*
        for (profile_terminal profile1: profile)
        {
            if (profile1.getId().equals(personId)){
                found=true;
                break;
            }
        }*/
        if (found){
            Log.info(" **** test case passed ***");
            Log.info("person Id"+personId+ "is found in PROFILE table.");
        }else{
            Log.info("person Id"+personId+ "is not found in PROFILE table.");
        }
    }

    /*@ShellMethod
    public void count_bcbp(){
        Log.info("*** Counting No.of DOCUMENT_BCBP **** ");

        boolean found=false;
        long num= documentDAO.count();
        Log.info("count is"+num);
    }*/
    public void validate_doc_passport(String dob, String natCd, String pptNum, String nric){
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
            Log.info(" **** test case passed ***");
            Log.info("person is found - dob is "+dob + "natcd is "+natCd+"pptNum is "+pptNum+ "nric is "+nric);
        }else{
            Log.info("person is not found in passport table.");
        }

    }

    public void validate_doc_bcbp(String direction, String terminal, String flt_arrvl_date){
        Log.info("*** Validating Profile Terminal's DOCUMENT_BCBP **** ");

        boolean found=false;
        Iterable<document_terminal> documentTerminalIterable= documentTerminalDAO.findAll();

        for (document_terminal doc :documentTerminalIterable)
        {
            if (doc.getDirection().equals(direction) && doc.getTerminal().equals(terminal)
                    && doc.getFlt_arrvl_date_time().equals(flt_arrvl_date))
            {
                found=true;
                break;
            }
        }
        if (found){
            Log.info(" **** test case passed ***");
            Log.info("person is found - direction is "+direction + "terminal is "+terminal+"flt_arrvl_datetime is "+flt_arrvl_date);
        }else {
            Log.info("person is not found in bcbp table.");
        }

    }

}

