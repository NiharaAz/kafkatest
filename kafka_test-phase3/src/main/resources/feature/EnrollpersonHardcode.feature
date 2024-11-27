Feature: Enrolling person
  Scenario: Test Case 1 : enroll person in PERSIST mode
    When Enroll person

    Scenario: Test Case 2  : Enroll person with biom S1003D in PERSIST mode
      When Enroll person with Biom S1003D

      Scenario: Test Case 3 : Enroll person with IdNo is NULL
        When Enroll person with IdNo is NULL

        Scenario: Test Case 4 : Enroll person with 2 same tdNo, diff DOB
          When Ingest person into queue with tdNo="tdNo1"
#          Then validate person 1
          And Ingest person into queue with tdNo="tdNo2"
          #Then validate person 2
