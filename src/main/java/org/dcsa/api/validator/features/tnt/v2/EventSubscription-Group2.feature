Feature:
  TNT Self Certification Check List

  Background: Create a default subscription
    Given API End point "/event-subscriptions" for "EventSubscription"
    And A valid Callback Url
    When Set request for POST with test case "EventTypeSHIPMENT"
    And Send a POST http request


  @HappyPath
  Scenario:Deletes a specific subscription with ID
    Given API End point "/event-subscriptions/{subscriptionID}" for "EventSubscription"
    When Set request for DELETE
    And Send a DELETE http request
    Then Receive valid response for DELETE

  @HappyPath
  Scenario:Updates a specific subscription with ID
    Given API End point "/event-subscriptions/{subscriptionID}" for "EventSubscription"
    And Placeholders with values
      | placeholder            | value       |
      | vesselIMONumber        | 9074729     |
      | equipmentEventTypeCode | [DISC,GTIN] |
    When Set request for PUT
    And Send a PUT http request
    Then Receive valid response for PUT

  @HappyPath
  Scenario Outline:Updates a specific subscription with ID
    Given API End point "/event-subscriptions/{subscriptionID}" for "EventSubscription"
    And Placeholder "<placeholder>" with value "<value>"
    When Set request for PUT
    And Send a PUT http request
    Then Receive invalid response for PUT
    Examples:
      | placeholder            | value       |
      | vesselIMONumber        | 9074729     |
      | secret | OG1wOWFaRW1HTTF1Y2NuaUN0RlAtaU9JMjM5N25vMWtWd25rS2Vkc2ktZms0c01zaTJQOElZRVNQN29MYUkzcg== |


  @HappyPath
  Scenario Outline:Resets the secret an existing subscription
    Given API End point "/event-subscriptions/{subscriptionID}/secret" for "EventSubscription"
    When Set request for PUT with test case "<testcase>"
    And Send a PUT http request
    Then Receive response code “204”
    Examples: List of test cases
      | testcase           |
      | UpdateSecret  |


  @NegativeCase
  Scenario:Deletes with invalid ID
    Given API End point "/event-subscriptions/{subscriptionID}" for "EventSubscription"
    And Path parameters with values
      | pathVariable   | value                                 |
      | subscriptionID | f9597deb-95cc-4382-8d1d-a66fc76c0b711 |
    When Send a DELETE http request
    Then Receive invalid response for DELETE

  @NegativeCase
  Scenario:Updates a specific subscription with ID
    Given API End point "/event-subscriptions/{subscriptionID}" for "EventSubscription"
    And Placeholders with values
      | placeholder            | value       |
      | vesselIMONumber        | 9074724     |
      | equipmentEventTypeCode | [DISC,GTIN] |
    When Set request for PUT
    And Send a PUT http request
    Then Receive invalid response for PUT

  @NegativeCase
  Scenario:Updates a specific subscription with ID
    Given API End point "/event-subscriptions/{subscriptionID}" for "EventSubscription"
    And Placeholders with values
      | placeholder    | value                                |
      | subscriptionID | ef7edb78-a902-4e4a-8183-8cc258465257 |
    When Set request for PUT
    And Send a PUT http request
    Then Receive response code “400”



