package com.example.kafka_test.database;

import com.example.kafka_test.database.itinerary.model.itinerary;
import com.example.kafka_test.database.itinerary.repository.ProductDAO;
import com.example.kafka_test.database.profile.model.person;
import com.example.kafka_test.database.profile.repository.PersonDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.stereotype.Component;

@ShellComponent
@Component
public class validate_controller {

    @Autowired
    ProductDAO productDAO;

    @Autowired
    PersonDAO personDAO;
    @ShellMethod
    public void validate_tdNo(){

        person person_1 = new person();
        person_1.setId("3999");
        person_1.setName("sql");
        personDAO.save(person_1);

        //Iterable<itinerary> products = productDAO.findAll();
        itinerary itinerary= new itinerary();
        itinerary.setID("0908762024");
        itinerary.setBIRTH_DATE("342342");itinerary.setTD_NO("8999fromsqlhelp");itinerary.setAUDIT_DATETIME("auditfromsql");
        itinerary.setNAT_CD("SG");itinerary.setMESSAGE_DATETIME("34234");itinerary.setTRANSLATED_ID("3423");
        productDAO.save(itinerary);
        /*products.forEach((p) -> {
            System.out.println(p.getAUDIT_DATETIME());
        });*/



    }
}
