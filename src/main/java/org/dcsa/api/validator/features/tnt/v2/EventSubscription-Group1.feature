Feature:
  TNT Self Certification Check List

  @HappyPath
  Scenario:GET-SUB-001_GET /event-subscriptions returns all subscriptions
    Given API End point "/event-subscriptions/" for "EventSubscription"
    When Set request for GET
    And Send GET http request
    Then Receive valid response for GET all
    Then Validated against schema

  @HappyPath
  Scenario:GET-SUB-002_GET /event-subscriptions-Paging check
    Given API End point "/event-subscriptions/" for "EventSubscription"
    When Set request for GET
    And Send GET http request
    Then Receive paging attribute in header
    Then Validated against schema

  @HappyPath
  Scenario:GET-SUB-003_GET /event-subscriptions-with limit parameter
    Given API End point "/event-subscriptions/" for "EventSubscription"
    And Query parameters with values
      | parameter | value |
      | limit     | 10    |
    When Set request for GET
    And Send GET http request
    #Then Receive valid response for GET all

  @HappyPath
  Scenario:GET-SUB-004_GET /event-subscriptions-Fetches a specific subscription with ID
    Given API End point "/event-subscriptions/{subscriptionID}" for "EventSubscription"
    And Path parameters
      | subscriptionID |
    When Set request for GET
    And Send GET http request
    Then Receive valid response for GET
    Then Validated against schema

  @HappyPath
  Scenario Outline:GET-SUB-005_GET /event-subscriptions-Fetches a specific subscription with ID
    Given API End point "/event-subscriptions/{subscriptionID}" for "EventSubscription"
    And Path parameters "<parameters>"
    When Set request for GET
    And Send GET http request
      Then Receive valid response for GET
    Examples: List of test cases
      | parameters     |
      | subscriptionID |

  @NegativeCase
  Scenario Outline:GET-SUB-006_GET /event-subscriptions-Fetches a specific subscription with invalid ID
    Given API End point "/event-subscriptions/{subscriptionID}" for "EventSubscription"
    And Path parameter "<parameter>" with value "<value>"
    When Send GET http request
    Then Receive invalid response for GET
    Examples: List of test cases
      | parameter      | value                                 |
      | subscriptionID | f9597deb-95cc-4382-8d1d-a66fc76c0b711 |

  @NegativeCase
  Scenario:GET-SUB-007_GET /event-subscriptions-Fetches a specific subscription with invalid ID
    Given API End point "/event-subscriptions/{subscriptionID}" for "EventSubscription"
    And Path parameters with values
      | pathVariable   | value                                 |
      | subscriptionID | f9597deb-95cc-4382-8d1d-a66fc76c0b711 |
    When Send GET http request
    Then Receive invalid response for GET



  @HappyPath
  Scenario Outline:POST-SUB-008_POST /event-subscriptions
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


  @NegativeCase
  Scenario Outline:POST-SUB-009_POST /event-subscriptions:with missing mandatory attributes
    Given API End point "/event-subscriptions" for "EventSubscription"
    And Attributes to be removed "<attributes>"
    When Set request for POST
    And Send a POST http request
    Then Receive invalid response for POST
    Examples:
      | attributes  |
      | callbackUrl |
      | secret      |

  @NegativeCase
  Scenario:POST-SUB-010_POST /event-subscriptions:with missing mandatory attributes
    Given API End point "/event-subscriptions" for "EventSubscription"
    And Attributes to be removed
      | callbackUrl |
      | secret      |
    When Set request for POST
    And Send a POST http request
    Then Receive invalid response for POST

  @NegativeCase
  Scenario:POST-SUB-011_POST /event-subscriptions:with invalid attribute value
    Given API End point "/event-subscriptions" for "EventSubscription"
    And Placeholders with values
      | placeholder            | value       |
      | vesselIMONumber        | 9321484     |
      | equipmentEventTypeCode | [DISC,GTIN] |
    When Set request for POST
    And Send a POST http request
    Then Receive invalid response for POST








