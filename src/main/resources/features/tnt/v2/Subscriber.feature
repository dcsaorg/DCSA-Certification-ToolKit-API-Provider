Feature:
  Notifications test check list

  @HeadRequest
  Scenario:TNT.2.2.PUB.SUB.1_Http code 204 for notification HEAD must be accepted as the only valid response_Callback URL HEAD response successful
    Given Callback url given in config event subscription
    And A valid Callback Url
    And Send HEAD http request for "EventSubscription"
    Then Receive Head request for CallBack URL

  @HeadRequest
  Scenario:TNT.2.2.PUB.SUB.2_Notification HEAD request must be received_Receipt of a HEAD request and success response
    When End point "/notification-endpoints/receive/{uuid}" for "EventSubscription"
    And A valid Callback Url
    And Send HEAD http request
    Then Receive Head request for CallBack URL

  @PostRequest
  Scenario:TNT.2.2.PUB.SUB.6_Notification POST request must be received_Receipt of a POST request and success response
    When End point "/notification-endpoints/receive/{uuid}" for "EventSubscription"
    And A valid Callback Url
    And Send a POST http request
    Then Receive a valid notification

  @HeadRequest
  Scenario:TNT.2.2.PUB.SUB.3_Invalid callback URL Head request must be rejected_Receipt of a head request and rejection response
    When End point "/event-subscriptions" for "EventSubscription"
    And An invalid Callback Url
    When Set request for POST with test case "SubscriptionRequest_CallBackTest"
    And Send a POST http request
    Then Not receive Head request for invalid callBack URL
    Then Receive invalid response for POST


  @Notification
  Scenario:TNT.2.2.PUB.SUB.4_Subscription requested must be rejected if the secrets are not adequate for the signature algorithm_Receipt an event with valid signature
    Given API End point "/event-subscriptions" for "EventSubscription"
    And A valid Callback Url
    When Set request for POST with test case "EventTypeTRANSPORT"
    And Send a POST http request
    Then Receive Head request for CallBack URL
    Then Receive valid response for POST
    Then Receive a valid notification

  @Notification
  Scenario:TNT.2.2.PUB.SUB.5_Notification must used rotated secret_Receipt an event after secret rotation
    Given API End point "/event-subscriptions" for "EventSubscription"
    And A valid Callback Url
    When Set request for POST with test case "EventTypeSHIPMENT"
    And Send a POST http request
    When End point "/notification-endpoints/receive/{uuid}" for "EventSubscription"
    And A valid Callback Url
    And Send HEAD http request
    Then Receive Head request for CallBack URL
    Given API End point "/event-subscriptions/{subscriptionID}/secret" for "EventSubscription"
    When Set request for PUT with test case "UpdateSecret"
    And Send a PUT http request
    Then Receive response code "204"
