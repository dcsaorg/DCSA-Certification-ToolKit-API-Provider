Feature:
  Notifications test check list

  @PostRequest
  Scenario:TNT.2.2.SUB.PRV_SUBSCRIPTION a new subscription request must be create new subscription_POST /event-subscriptions with a valid event type
    Given API end point "/event-subscriptions" for "EventSubscription"
    And A valid callback url
    And A valid secret
    Then Send HEAD request
    Then Send request for POST


  @HeadRequest
  Scenario:TNT.2.2.SUB.PRV.8_Head request must be failed if no valid secret_Receipt of a head request fails
    Given API End point "/event-subscriptions" for "EventSubscription"
    And A valid callback url
    And An invalid secret
    When Set request for POST with test case "SubscriptionRequest_CallBackTest"
    And Send a POST http request
    Then Receive Head request for CallBackURL
    Then Receive valid response for POST
    Then Receive a valid notification


  @HeadRequest
  Scenario:TNT.2.2.SUB.PRV.10_Head request must be received_Receipt of a head request and success response
    Given API End point "/event-subscriptions" for "EventSubscription"
    And A valid callback url
    When Set request for POST with test case "SubscriptionRequest_CallBackTest"
    And Send a POST http request
    Then Receive Head request for CallBackURL
    Then Receive valid response for POST


  @PostRequest
  Scenario:TNT.2.2.SUB.PRV.10_Post request must be received_Receipt of a post request and success response
    Given API End point "/event-subscriptions" for "EventSubscription"
    And A valid callback url
    When Set request for POST with test case "SubscriptionRequest_CallBackTest"
    And Send a POST http request
    Then Receive Head request for CallBackURL
    Then Receive valid response for POST


  @Notification
  Scenario:TNT.2.2.SUB.PRV.7_Subscription requested must be rejected if the secrets are not adequate for the signature algorithm_Receipt an event with valid signature
    Given API End point "/event-subscriptions" for "EventSubscription"
    And A valid callback url
    When Set request for POST with test case "EventTypeTRANSPORT"
    And Send a POST http request
    Then Receive Head request for CallBackURL
    Then Receive valid response for POST
    Then Receive a valid notification


  @Notification
  Scenario:TNT.2.2.SUB.PRV.11_Notification must used rotated secret_Receipt an event after secret rotation
    Given API End point "/event-subscriptions" for "EventSubscription"
    And A valid callback url
    When Set request for POST with test case "EventTypeSHIPMENT"
    And Send a POST http request
    Then Receive Head request for CallBackURL
    Then Receive valid response for POST
    Then Receive a valid notification
    Given API End point "/event-subscriptions/{subscriptionID}/secret" for "EventSubscription"
    When Set request for PUT with test case "UpdateSecret"
    And Send a PUT http request
    Then Receive response code "204"
    Then Receive a valid notification


