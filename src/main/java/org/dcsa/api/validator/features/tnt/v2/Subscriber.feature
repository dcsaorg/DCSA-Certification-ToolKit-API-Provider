Feature:
  Notifications test check list

  Background: Start Spark Webhook server
    Given Start Webhook server


  @Notification
  Scenario:Test receiving notification after secret rotation
    Given API End point "/event-subscriptions" for "EventSubscription"
    And A valid Callback Url
    When Set request for POST with test case "EventTypeSHIPMENT"
    And Send a POST http request
    Given API End point "/event-subscriptions/{subscriptionID}/secret" for "EventSubscription"
    When Set request for PUT with test case "UpdateSecret"
    And Send a PUT http request
    Then Receive Head request for CallBackURL
    Then Receive valid response for POST
    Then Receive a valid notification


