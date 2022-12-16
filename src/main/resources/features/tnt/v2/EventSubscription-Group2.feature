Feature:
  TNT Self Certification Check List

  Background: Create a default subscription
    Given API End point "/event-subscriptions" for "EventSubscription"
    And A valid Callback Url
    When Set request for POST with test case "EventTypeSHIPMENT"
    And Send a POST http request
    And Receive valid response for POST
    Then Validated against schema

  Scenario:TNT.2.2.SUB.PRV.5_PUT /event-subscriptions/{subscriptionID} Updates a specific subscription with ID={subscriptionID} with valid request_PUT /event-subscriptions with valid request
    Given API End point "/event-subscriptions/{subscriptionID}" for "EventSubscription"
    And Placeholders with values
      | placeholder            | value       |
      | vesselIMONumber        | 9074729     |
      | equipmentEventTypeCode | [DISC,GTIN] |
    When Set request for PUT
    And Send a PUT http request
    Then Receive valid response for PUT
    Then Validated against schema


  Scenario Outline:TNT.2.2.SUB.PRV.6_PUT /event-subscriptions/{subscriptionID} update secret receive invalid response_PUT /event-subscriptions/{subscriptionID} update secret receive invalid response
    Given API End point "/event-subscriptions/{subscriptionID}" for "EventSubscription"
    And Placeholder "<placeholder>" with value "<value>"
    When Set request for PUT
    And Send a PUT http request
    Then Receive invalid response for PUT
    Examples:
      | placeholder            | value       |
      | secret | OG1wOWFaRW1HTTF1Y2NuaUN0RlAtaU9JMjM5N25vMWtWd25rS2Vkc2ktZms0c01zaTJQOElZRVNQN29MYUkzcg== |


  Scenario Outline:TNT.2.2.SUB.PRV.7_PUT /event-subscriptions/{subscriptionID} update secret_PUT /event-subscriptions update secret
    Given API End point "/event-subscriptions/{subscriptionID}/secret" for "EventSubscription"
    When Set request for PUT with test case "<testcase>"
    And Send a PUT http request
    Then Receive response code "204"
    Examples: List of test cases
      | testcase           |
      | UpdateSecret  |

  Scenario:TNT.2.2.SUB.PRV.8_DELETE /event-subscriptions/{subscriptionID} with valid ID_DELETE /event-subscriptions with valid ID
    Given API End point "/event-subscriptions/{subscriptionID}" for "EventSubscription"
    When Set request for DELETE
    And Send a DELETE http request
    Then Receive valid response for DELETE


  Scenario:TNT.2.2.ERR.PRV.9_Test case DELETE /event-subscriptions with invalid ID validating "not found" request - HTTP 404_DELETE /event-subscriptions with invalid ID
    Given API End point "/event-subscriptions/{subscriptionID}" for "EventSubscription"
    And Path parameters with values
      | pathVariable   | value                                 |
      | subscriptionID | 550173fc-dd7f-403a-9b13-f022a4df99ba |
    When Send a DELETE http request
    Then Receive response code "404"

  Scenario:TNT.2.2.ERR.PRV.7_Test case DELETE /event-subscriptions with invalid ID validating "bad request" - HTTP 400_DELETE /event-subscriptions with invalid ID
    Given API End point "/event-subscriptions/{subscriptionID}" for "EventSubscription"
    And Path parameters with values
      | pathVariable   | value                                 |
      | subscriptionID | f9597deb-95cc-4382-8d1d-a66fc76c0b711 |
    When Send a DELETE http request
    Then Receive response code "400"

  Scenario:TNT.2.2.ERR.PRV.10_Test case PUT /event-subscriptions with invalid attribute value validating "bad request" - HTTP 400_PUT /event-subscriptions with invalid attribute value
    Given API End point "/event-subscriptions/{subscriptionID}" for "EventSubscription"
    And Placeholders with values
      | placeholder            | value       |
      | vesselIMONumber        | 9074724     |
      | equipmentEventTypeCode | [DISC,GTIN] |
    When Set request for PUT
    And Send a PUT http request
    Then Receive invalid response for PUT


  Scenario:TNT.2.2.ERR.PRV.8_Test case PUT /event-subscriptions with mismatch of subscriptionID in body and URL validating "bad request" - HTTP 400_PUT /event-subscriptions with mismatch of subscriptionID in body and URL
    Given API End point "/event-subscriptions/{subscriptionID}" for "EventSubscription"
    And Placeholders with values
      | placeholder    | value                                |
      | subscriptionID | ef7edb78-a902-4e4a-8183-8cc258465257 |
    When Set request for PUT
    And Send a PUT http request
    Then Receive response code "400"



