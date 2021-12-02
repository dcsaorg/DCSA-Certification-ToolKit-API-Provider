Feature:
  Notifications test check list

  Background: Start Spark Webhook server
    Given Start Webhook server


  @HeadRequest
  Scenario:Test receiving head request
    Given API End point "/event-subscriptions" for "EventSubscription"
    And A valid Callback Url
    When Set request for POST with test case "SubscriptionRequest_CallBackTest"
    And Send a POST http request
    Then Receive Head request for CallBackURL
    Then Receive valid response for POST


  @HeadRequest
  Scenario:Testing failure of subscription on head request rejection
    Given API End point "/event-subscriptions" for "EventSubscription"
    And An invalid Callback Url
    When Set request for POST with test case "SubscriptionRequest_CallBackTest"
    And Send a POST http request
    Then Receive Head request for CallBackURL
    Then Receive invalid response for POST

  @Notification
  Scenario:Test receiving notification for eventType Transport
    Given API End point "/event-subscriptions" for "EventSubscription"
    And A valid Callback Url
    When Set request for POST with test case "EventTypeTRANSPORT"
    And Send a POST http request
    Then Receive Head request for CallBackURL
    Then Receive valid response for POST
    Then Receive a valid notification


  @Notification
  Scenario:Test receiving notification after secret rotation
    Given API End point "/event-subscriptions" for "EventSubscription"
    And A valid Callback Url
    When Set request for POST with test case "EventTypeSHIPMENT"
    And Send a POST http request
    Given API End point "/event-subscriptions/{subscriptionID}/secret" for "EventSubscription"
    When Set request for PUT with test case "UpdateSecret"
    And Send a PUT http request
    Then Receive response code "204"
    Then Receive a valid notification


