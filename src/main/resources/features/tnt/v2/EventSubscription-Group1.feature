Feature:
  TNT Self Certification Check List


  Scenario:TNT.2.2.SUB.PRV.1_GET /event-subscriptions returns all subscriptions_GET /event-subscriptions without any query parameter
    Given API End point "/event-subscriptions/" for "EventSubscription"
    When Set request for GET
    And Send GET http request
    Then Receive valid response for GET all
    Then Validated against schema


  Scenario:TNT.2.2.SUB.PRV.1_GET /event-subscriptions returns all subscriptions_GET /event-subscriptions with limit parameter
    Given API End point "/event-subscriptions/" for "EventSubscription"
    And Query parameters with values
      | parameter | value |
      | limit     | 10    |
    When Set request for GET
    And Send GET http request
    #Then Receive valid response for GET all


  Scenario Outline:TNT.2.2.SUB.PRV.2_POST /event-subscriptions Creates a new subscription_GET /event-subscriptions for different Event types
    Given API End point "/event-subscriptions" for "EventSubscription"
    When Set request for POST with test case "<testcase>"
    And Send a POST http request
    Then Receive valid response for POST
    Then Validated against schema
    Examples: List of test cases
      | testcase           |
      | EventTypeSHIPMENT  |
      | EventTypeTRANSPORT |
      | EventTypeEQUIPMENT |


  Scenario:TNT.2.2.SUB.PRV.3_GET /event-subscriptions/{subscriptionID} Fetches a specific subscription with ID={subscriptionID}_GET /event-subscriptions with valid Id
    Given API End point "/event-subscriptions/{subscriptionID}" for "EventSubscription"
    And Path parameters
      | subscriptionID |
    When Set request for GET
    And Send GET http request
    Then Receive valid response for GET
    Then Validated against schema


  Scenario:TNT.2.2.ERR.PRV.4_Test case validating "bad request" - HTTP 400_POST /event-subscriptions with missing mandatory attributes
    Given API End point "/event-subscriptions" for "EventSubscription"
    And Attributes to be removed
      | callbackUrl |
      | secret      |
    When Set request for POST
    And Send a POST http request
    Then Receive response code "400"
    #Then Receive invalid response for POST


  Scenario:TNT.2.2.ERR.PRV.4_Test case validating "bad request" - HTTP 400_POST /event-subscriptions with invalid attribute value
    Given API End point "/event-subscriptions" for "EventSubscription"
    And Placeholders with values
      | placeholder            | value       |
      | vesselIMONumber        | 9321484     |
      | equipmentEventTypeCode | [DISC,GTIN] |
    When Set request for POST
    And Send a POST http request
    Then Receive response code "400"
    #Then Receive invalid response for POST


  Scenario:TNT.2.2.API.PRV.4_Links to current, previous, next, first and last page SHOULD be available in the response headers_GET /event-subscriptions Pagination check
    Given API End point "/event-subscriptions/" for "EventSubscription"
    When Set request for GET
    And Send GET http request
    Then Receive headers in response
      | Current-Page |
    Then Validated against schema


  Scenario Outline:TNT.2.2.ERR.PRV.4_Test case validating "not found" request - HTTP 404_GET /event-subscriptions with invalid Id
    Given API End point "/event-subscriptions/{subscriptionID}" for "EventSubscription"
    And Path parameter "<parameter>" with value "<value>"
    When Send GET http request
   #Then Receive invalid response for GET
    Then Receive response code "404"
    Examples: List of test cases
      | parameter      | value                                 |
      | subscriptionID | 550173fc-dd7f-403a-9b13-f022a4df99ba |


  Scenario:TNT.2.2.ERR.PRV.4_Test case validating "bad request" - HTTP 400_GET /event-subscriptions with invalid ID
    Given API End point "/event-subscriptions/{subscriptionID}" for "EventSubscription"
    And Path parameters with values
      | pathVariable   | value                                 |
      | subscriptionID | f9597deb-95cc-4382-8d1d-a66fc76c0b711 |
    When Send GET http request
    Then Receive response code "400"
    #Then Receive invalid response for GET







