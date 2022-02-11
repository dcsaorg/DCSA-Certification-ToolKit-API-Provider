Feature:
  TNT Self Certification Check List

  Background: Create a default subscription
    Given API End point "/event-subscriptions" for "EventSubscription"
    And A valid Callback Url
    When Set request for POST with test case "EventTypeSHIPMENT"
    And Send a POST http request
    And Receive valid response for POST
    Then Validated against schema


  @HappyPath
  Scenario:DELETE-SUB-012_DELETE /event-subscriptions/{subscriptionID}:with subscription ID
    Given API End point "/event-subscriptions/{subscriptionID}" for "EventSubscription"
    When Set request for DELETE
    And Send a DELETE http request
    Then Receive valid response for DELETE

  @HappyPath
  Scenario:PUT-SUB-013_PUT /event-subscriptions/{subscriptionID}:with valid attribute values
    Given API End point "/event-subscriptions/{subscriptionID}" for "EventSubscription"
    And Placeholders with values
      | placeholder            | value       |
      | vesselIMONumber        | 9074729     |
      | equipmentEventTypeCode | [DISC,GTIN] |
    When Set request for PUT
    And Send a PUT http request
    Then Receive valid response for PUT
    Then Validated against schema

  @HappyPath
  Scenario Outline:PUT-SUB-014_PUT /event-subscriptions/{subscriptionID}:update secret
    Given API End point "/event-subscriptions/{subscriptionID}" for "EventSubscription"
    And Placeholder "<placeholder>" with value "<value>"
    When Set request for PUT
    And Send a PUT http request
    Then Receive invalid response for PUT
    Examples:
      | placeholder            | value       |
      | secret | OG1wOWFaRW1HTTF1Y2NuaUN0RlAtaU9JMjM5N25vMWtWd25rS2Vkc2ktZms0c01zaTJQOElZRVNQN29MYUkzcg== |


  @HappyPath
  Scenario Outline:PUT-SUB-015_PUT/ event-subscriptions/{subscriptionID}/secret
    Given API End point "/event-subscriptions/{subscriptionID}/secret" for "EventSubscription"
    When Set request for PUT with test case "<testcase>"
    And Send a PUT http request
    Then Receive response code "204"
    Examples: List of test cases
      | testcase           |
      | UpdateSecret  |


  @NegativeCase
  Scenario:DELETE-SUB-016_DELETE /event-subscriptions/{subscriptionID}:with invalid ID
    Given API End point "/event-subscriptions/{subscriptionID}" for "EventSubscription"
    And Path parameters with values
      | pathVariable   | value                                 |
      | subscriptionID | f9597deb-95cc-4382-8d1d-a66fc76c0b711 |
    When Send a DELETE http request
    #Then Receive response code "404"
    Then Receive response code "400"

  @NegativeCase
  Scenario:PUT-SUB-017_PUT /event-subscriptions/{subscriptionID}:with invalid attribute value
    Given API End point "/event-subscriptions/{subscriptionID}" for "EventSubscription"
    And Placeholders with values
      | placeholder            | value       |
      | vesselIMONumber        | 9074724     |
      | equipmentEventTypeCode | [DISC,GTIN] |
    When Set request for PUT
    And Send a PUT http request
    Then Receive invalid response for PUT

  @NegativeCase
  Scenario:PUT-SUB-018_PUT /event-subscriptions/{subscriptionID}:with mismatch of subscriptionID in body and URL
    Given API End point "/event-subscriptions/{subscriptionID}" for "EventSubscription"
    And Placeholders with values
      | placeholder    | value                                |
      | subscriptionID | ef7edb78-a902-4e4a-8183-8cc258465257 |
    When Set request for PUT
    And Send a PUT http request
    Then Receive response code "400"



