package com.example.kafka_test.database;

import com.example.kafka_test.database.itinerary.model.itinerary;
import com.example.kafka_test.database.itinerary.repository.ItineraryDAO;
import com.example.kafka_test.database.personDomain.model.personDomain;
import com.example.kafka_test.database.profile.model.profile;
import com.example.kafka_test.database.profile.repository.profileDAO;
import com.example.kafka_test.database.personDomain.repository.personDomainDAO;
import com.example.kafka_test.queueData.ICS_data;
import org.jline.utils.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.stereotype.Component;

@ShellComponent
@Component
public class validate_controller {

    @Autowired
    ItineraryDAO itineraryDAO;
    @Autowired
    profileDAO profileDAO;
    @Autowired
    personDomainDAO person_Domain_DAO;

    @ShellMethod
    public void validate_persondomain(){
        Iterable<personDomain> p = person_Domain_DAO.findAll();
        p.forEach(o->System.out.println(o.getPERSON_ID()));
    }

    @ShellMethod
    public String validate_personKey(String tdNo, String natCd, String dob){
        Log.info("*** Validating personKey **** ");

        String pKey1= tdNo+"_"+natCd+"_"+dob;
        Log.info("pkey is "+pKey1);


        Iterable<personDomain> p = person_Domain_DAO.findAll();
        String  personId = null,pKey = null;
        boolean found = false;

        for (personDomain pd : p){
             pKey = pd.getPKEY();
             if (pKey.equals(pKey1))
             {
                 personId = pd.getPERSON_ID();
                 found=true;
                 break;
            }
        }
        if (found){
            Log.info("person is found. person id is "+personId+" person key is "+pKey);
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
            if (i.getID().equals(idNo)){
                found=true;
                break;
            }
        }
        if (found){
            Log.info("Itinerary Id is found in ITINERARY table. Itineray Id is ");
        }else{
            Log.info("Itinerary Id is not found in ITINERARY table. Itineray Id is ");
        }
    }
    @ShellMethod
    public void validate_central_personId(String personId) {
        Log.info("*** Validating Profile's person Id **** ");
        boolean found =false;
        //profileDAO.findAll().forEach(p1->System.out.println(p1.getId()));
        Iterable<profile> profile = profileDAO.findAll();
        for (profile profile1: profile)
        {
            if (profile1.getId().equals(personId)){
                found=true;
                break;
            }
        }
        if (found){
            Log.info("person Id is found in PROFILE table. person id is ");
        }else{
            Log.info("person Id is not found in PROFILE table ");
        }
    }

    public void validate_terminal_personId(String personId)
    {
        Log.info("*** Validating Profile terminal's person Id **** ");
    }
    public void validate_images(){}
    public void validate_biom_profile(){}
}

