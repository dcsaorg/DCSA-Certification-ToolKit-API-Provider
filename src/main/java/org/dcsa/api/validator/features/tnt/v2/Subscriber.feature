Feature:
  Notifications test check list

  @HeadRequest
  Scenario:TNT.2.2.SUB.PRV.8_Head request must be received_Receipt of a head request and success response
    Given API End point "/event-subscriptions" for "EventSubscription"
    And A valid Callback Url
    When Set request for POST with test case "SubscriptionRequest_CallBackTest"
    And Send a POST http request
    Then Receive Head request for CallBackURL
    Then Receive valid response for POST


  @HeadRequest
  Scenario:TNT.2.2.SUB.PRV.10_Head request must be received_Receipt of a head request and rejection response
    Given API End point "/event-subscriptions" for "EventSubscription"
    And An invalid Callback Url
    When Set request for POST with test case "SubscriptionRequest_CallBackTest"
    And Send a POST http request
    Then Receive Head request for CallBackURL
    Then Receive invalid response for POST

  @Notification
  Scenario:TNT.2.2.SUB.PRV.7_Subscription requested must be rejected if the secrets are not adequate for the signature algorithm_Receipt an event with valid signature
    Given API End point "/event-subscriptions" for "EventSubscription"
    And A valid Callback Url
    When Set request for POST with test case "EventTypeTRANSPORT"
    And Send a POST http request
    Then Receive Head request for CallBackURL
    Then Receive valid response for POST
    Then Receive a valid notification


  @Notification
  Scenario:TNT.2.2.SUB.PRV.11_Notification must used rotated secret_Receipt an event after secret rotation
    Given API End point "/event-subscriptions" for "EventSubscription"
    And A valid Callback Url
    When Set request for POST with test case "EventTypeSHIPMENT"
    And Send a POST http request
    Given API End point "/event-subscriptions/{subscriptionID}/secret" for "EventSubscription"
    When Set request for PUT with test case "UpdateSecret"
    And Send a PUT http request
    Then Receive response code "204"
    Then Receive a valid notification


