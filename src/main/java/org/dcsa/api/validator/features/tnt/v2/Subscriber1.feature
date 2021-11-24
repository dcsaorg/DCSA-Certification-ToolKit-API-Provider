Feature:
  Notifications test check list

  Background: Start Spark Webhook server
    Given Start Webhook server

  @HeadRequest
  Scenario Outline:Test receiving head request
    Given API End point "/event-subscriptions" for "EventSubscription"
    And A valid Callback Url
    When Set request for POST with test case "<testcase>"
    And Send a POST http request
    Then Receive Head request for CallBackURL
    Then Receive valid response for POST
    Examples: List of test cases
      | testcase         |
      | TransportEventSubscription |


  @HeadRequest
  Scenario Outline:Testing failure of subscription on head request rejection
    Given API End point "/event-subscriptions" for "EventSubscription"
    And An invalid Callback Url
    When Set request for POST with test case "<testcase>"
    And Send a POST http request
    Then Receive Head request for CallBackURL
    Then Receive invalid response for POST
    Examples: List of test cases
      | testcase         |
      | TransportEventSubscription |

  @Notification
  Scenario Outline:Test receiving notification for eventType Transport
    Given API End point "/event-subscriptions" for "EventSubscription"
    And A valid Callback Url
    When Set request for POST with test case "<testcase>"
    And Send a POST http request
    Then Receive Head request for CallBackURL
    Then Receive valid response for POST
    Then Receive a valid notification
    Examples: List of test cases
      | testcase         |
      | TransportEventSubscription |

  @Notification
  Scenario Outline:Test receiving notification after secret rotation
    Given API End point "/event-subscriptions/{subscriptionID}" for "EventSubscription"
    And A valid Callback Url
    When Set request for POST with test case "EventTypeSHIPMENT"
    And Send a POST http request
    Given API End point "/event-subscriptions/{subscriptionID}/secret" for "EventSubscription"
    When Set request for PUT with test case "<testcase>"
    And Send a PUT http request
    Then Receive Head request for CallBackURL
    Then Receive valid response for POST
    Then Receive a valid notification
    Examples: List of test cases
      | testcase         |
      | TransportEventSubscription |


