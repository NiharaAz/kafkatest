package com.example.kafka_test.database;

import com.example.kafka_test.Utilities;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jline.utils.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ShellComponent
@Component
public class APIs {

    private final Mapping mapping;
    private final Utilities utilities;
    private final String url_phase3="http://172.16.24.15:31201" ; //23-10 changed
    private final String urlMMbs = "http://172.16.24.15:31200";

    @Autowired
    public APIs(Mapping mapping, Utilities utilities) {
        this.mapping = mapping;
        this.utilities = utilities;
    }

    public void mmbsGETBIOMapi(String personid, String nric) throws URISyntaxException, IOException, InterruptedException {
        Map<String, List<String>> map = new HashMap<>();

        map.put("S1002D", new ArrayList<>(List.of("FACE", "LEFT_THUMB")));
        map.put("S1003D", new ArrayList<>(List.of("FACE", "LEFT_EYE")));
        map.put("S1004D",new ArrayList<>(List.of("FACE","LEFT_EYE","RIGHT_EYE")));

        List<String> values = null;
        if (map.containsKey(nric)) {
            values = map.get(nric);
        } else {
            System.out.println(nric + " is not a key in the map.");
        }

        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(new URI(urlMMbs +"/biometric/v1/person/get/" +personid))
                .GET()
                .build();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> getResponse = httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
        //Log.info("response body is"+getResponse.body());

        // Parse the JSON response
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(getResponse.body());
        String personidValue = rootNode.path("personId").textValue();
        JsonNode registrationNode = rootNode.path("registration");

        // Check if there are any registration nodes
        List<String> bioSubTypes = null;
        if (registrationNode.isEmpty()) {
            System.out.println("no reg nodes");
        } else {
            bioSubTypes = new ArrayList<>();
            for (JsonNode regNode : registrationNode) {
                JsonNode biometricsNode = regNode.path("biometrics");
                for (JsonNode bioNode : biometricsNode) {
                    bioSubTypes.add(bioNode.path("bioSubType").asText());
                }
            }
            //System.out.println("Extracted bioSubType values: " + bioSubTypes);
        }
        if (bioSubTypes.equals(values) && personidValue.equals(personid)) {
            System.out.println("**** Test case passed ******, Extracted subtype matches" + values);
        }
    }


    public void identify(String requestid,String tdNo,String natCd, String DOB,String Terminal,
                         String Direction,String VDET, String nric) throws URISyntaxException, IOException, InterruptedException {

        Log.info(" **** sending Identify request ****");

        //Utilities utilities = new Utilities();
        VDET = utilities.ChangeVDT_to_SGt_identifyAPI(VDET);
        Log.info("VDEt is " + VDET);
        String key = nric;

        Map<String, List<Biometric>> mapping_of_biometrics_to_nric = mapping.mapping_of_biom_to_nric();
        List<Biometric> b = mapping_of_biometrics_to_nric.get(key);
        BiometricsRequest biometricsRequest = new BiometricsRequest();
        biometricsRequest.setBiometric(b);
        biometricsRequest.setDirection(Direction);
        biometricsRequest.setReturnPpt(true);


        ObjectMapper objectMapper = new ObjectMapper();
        String biometrics_string = objectMapper.writeValueAsString(biometricsRequest);
        //Log.info(biometrics_string);


        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(new URI(url_phase3 + "/camel/ONE-ID/v1/identify-by-contactless?requestId=" + requestid))
                .POST(HttpRequest.BodyPublishers.ofString(biometrics_string)) // Send request body as a string
                .build();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> getResponse = httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString());

        Thread.sleep(20000);

        JsonNode rootNode = objectMapper.readTree(getResponse.body());
        Log.info("rootNode is " + rootNode);
        JsonNode candidates = rootNode.path("candidate");


        String decision = null;
        String natcd = null;
        String terminal = null;
        String dob = null;
        String optionalelement = null;
        String direction = null;
        String fltArrvlDateTime = null;
        String pptNum=null;

        if (candidates.size() > 0) {
            //Thread.sleep(5000);
            for (JsonNode candidate : candidates) {
                decision = candidate.path("decision").asText();
                // if decision is not hit. break out of loop and print test case failed
                String personId = candidate.path("personId").asText();
                JsonNode docs = candidate.path("docs");

                // Iterate through docs
                for (JsonNode doc : docs) {
                    if (doc.path("type").asText().equals("bcbp")) {
                        fltArrvlDateTime = doc.path("fltArrvlDateTime").asText();
                        terminal = doc.path("terminal").asText();
                        direction = doc.path("direction").asText();

                        // Print out the values
                        System.out.println("Decision: " + decision);
                        System.out.println("Person ID: " + personId);
                        System.out.println("Terminal: " + terminal);
                        System.out.println("Direction: " + direction);
                        System.out.println("Flight Arrival Date Time: " + fltArrvlDateTime);
                        System.out.println("VDET is " + VDET);
                    } else {
                        pptNum = doc.path("pptNum").asText();
                        natcd = doc.path("natcd").asText();
                        dob = doc.path("dob").asText();
                        optionalelement = doc.path("optionalElement").asText();
                        System.out.println("PPT Number: " + pptNum);
                        System.out.println("National Code: " + natcd);
                        System.out.println("Date of Birth: " + dob);
                        System.out.println("optional element" + optionalelement);
                    }

                }
            }
        } else {
            System.out.println("No candidates found.");
        }
        if (decision.equals("HIT") && pptNum.equals(tdNo) && natcd.equals(natCd) && terminal.equals(Terminal)
                && fltArrvlDateTime.equals(VDET) && dob.equals(DOB)
                && direction.equals(Direction) && optionalelement.equals(nric)) {
            Log.info("*** Identify passed ");

        } else {
            Log.info(" **** Identify failed ****");
        }

    }

    @ShellMethod
    public int DeleteAPI () throws IOException, InterruptedException, URISyntaxException { //23-10 changed
        HttpRequest deleteRequest = HttpRequest.newBuilder()
                .uri(new URI(urlMMbs + "/biometric/v1/maintenance/delete-all-persons"))
                .DELETE()
                .build();
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpResponse<String> response = httpClient.send(deleteRequest, HttpResponse.BodyHandlers.ofString());
        int statusCode = response.statusCode();
        return statusCode;

    }

}
