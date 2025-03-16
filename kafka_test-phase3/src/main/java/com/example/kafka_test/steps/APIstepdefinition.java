package com.example.kafka_test.steps;

import com.example.kafka_test.database.APIs;
import io.cucumber.java.en.Then;
import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class APIstepdefinition {
    @Autowired
    private APIs apIs;
    public static final Logger Log = LoggerFactory.getLogger(APIstepdefinition.class);
    private Map<String, Object> extractedFields= stepsclass.getExtractedFields();



    @Then("I make an Authenticate API call to verify 1:1 person")
    public void AuthenticateAPI() throws IOException, URISyntaxException, InterruptedException {
        String nric= (String) extractedFields.get("idNo");
        String tdNo= (String) extractedFields.get("tdNo");
        String natcdLocal= (String) extractedFields.get("natCd");
        String dobLocal = (String) extractedFields.get("dob");

        apIs.authenticateAPI(nric,tdNo,natcdLocal,dobLocal);
    }

    @Then("I make an Identify API call to identify 1:N person")
    public void IdentifyAPICall() throws URISyntaxException, IOException, InterruptedException {
        Log.info("*** stating identify test case *** ");

        String tdnoLocal= (String) extractedFields.get("tdNo");
        String natcdLocal= (String) extractedFields.get("natCd");
        Log.info("tdno is "+tdnoLocal+"natcd is "+natcdLocal);
        String dobLocal = (String) extractedFields.get("dob");
        Log.info("dob is "+dobLocal);

        String direction= (String) extractedFields.get("direction");
        String VDET = (String) extractedFields.get("VDET");
        String nric= (String) extractedFields.get("idNo");
        Log.info("VDET is "+VDET+"nric is "+nric);

        apIs.identify(tdnoLocal,natcdLocal, dobLocal,"SIN1",direction,VDET,nric);
    }
}
