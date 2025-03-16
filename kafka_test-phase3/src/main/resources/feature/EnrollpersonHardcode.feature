Feature: Enrolling person
#  Scenario: Test Case 1 : enroll person in PERSIST mode
#    When Enroll person
#
#
#  Scenario: Test Case 2  : Enroll person with biom S1003D in PERSIST mode
#      When Enroll person with Biom S1003D
#      Then Validate person Key table
###
#  Scenario: Test Case 3 : Enroll person with IdNo is NULL
#        When Enroll person with IdNo is NULL
#
#  Scenario Outline: Test Case 4 : Enroll person with 2 same tdNo, diff DOB
#    When Ingest person into queue with tdNo=<tdNo>,natCd=<natCd>,nric=<nric>,dob=<dob>,direction=<direction>
#    Then Validate person Key table
#    Then Validate itineraryId
#    Then Validate doc_bcbp table
#    Then Validate kafka queue oneid terminal
###          Then validate person 1
##          And Ingest person into queue with tdNo="tdNo2"
##          #Then validate person 2
#
#    Examples:
#      | tdNo | natCd | nric | dob     |direction |
#      | new  | SG    | S1002D|20010901| I        |


  Scenario: Test Case 2 : Enroll Singapore citizen into oneId Terminal

    When I ingest person into ics kafka topic
    Then I validate personKey in ecp's person Key table
    Then I validate itinId,idNo,tdNo,natCd,dob,terminal, direction,VDET,personId in ecp's ics_itinerary table
    Then I validate central's kafka topic consists of correct tdNo
    Then I validate oneid terminal doc exist in doc_bcbp table
    Then I validate oneid terminal person exist in person table
    Then I make an Identify API call to identify 1:N person
    Then I make an Authenticate API call to verify 1:1 person
    Then I make a GET Biometrics call to MBSS to verify corrrect bio types are present

 # i want to compare first then send the update value.
  #after comparing value is diff, mark test case as fail.
#  Scenario: Test Case 3: Update natCd of Singapore Citizen
#
#    When I ingest person into ics kafka topic
    #Then I update natCd = "SG"
    #Then I send to queue



#  Scenario: Test case 2: Enroll person by sending json data below
#    When Ingest person into queue with json string
#
#      """
#      {
#        "travellerInfo": {
#          "idNo": "S1003D",
#          "tdNo": "JsonTxt",
#          "natCd": "SG",
#          "dobTxt": 20010901,
#          "eligibleForContactless": true
#        },
#        "itineraryInfo": {
#          "chkptCd": "C",
#          "statInOut": "I",
#          "validityEndDateTime": "2025-10-08T11:15:20+08:00",
#          "itineraryId": "json001"
#        },
#        "mmbsRefInfo": {
#          "dataSrc": "TRANSIENT",
#          "primaryExternalId": "B190013212",
#          "secondaryExternalId": "S7128971J"
#        }
#      }
#
#     """
#    Then Validate itineraryId
