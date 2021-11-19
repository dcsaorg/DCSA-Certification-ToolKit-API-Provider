Feature:
  TNT Self Certification Check List

  @HappyPath
  Scenario:Get all events
    Given API End point "/events" for "Event"
    When Send GET http request
    Then Receive valid response for GET all

  @HappyPath
  Scenario:Get all events
    Given API End point "/events" for "Event"
    And Query parameters with values
      | parameter | value |
      | limit     | 10    |
    When Send GET http request
    Then Receive valid response for GET all

  @HappyPath
  Scenario Outline:Get all events with Query Parameters
    Given API End point "/events" for "Event"
    And Query parameters "<parameters>"
    When Send GET http request
    Then Receive valid response for GET all
    Examples: List of query parameters
      | parameters                 |
      | eventType                  |
      | documentTypeCode           |
      | shipmentEventTypeCode      |
      | carrierBookingReference    |
      | transportDocumentReference |
      | transportEventTypeCode     |
      | transportCallID            |
      | vesselIMONumber            |
      | exportVoyageNumber         |
      | carrierServiceCode         |
      | UNLocationCode             |
      | equipmentEventTypeCode     |
      | equipmentReference         |
      | eventCreatedDateTime:gte   |

  @HappyPath
  Scenario:Get all events with Query Parameters
    Given API End point "/events" for "Event"
    And Query parameters
      | limit      |
    When Send GET http request
    Then Receive valid response for GET all

  @NegativeCase
  Scenario Outline:Get all events with invalid query Parameters
    Given API End point "/events" for "Event"
    And Query parameter "<parameter>" with value "<value>"
    When Send GET http request
    Then Receive invalid response for GET
    Examples: List of query parameters
      | parameter                  | value                                                                                                 |
      | eventType                  | SHIPMENT1                                                                                             |
      | documentTypeCode           | ABCDES                                                                                                |
      | shipmentEventTypeCode      | ABCFGGHH                                                                                              |
      | carrierBookingReference    | ABC709951ABC709951ABC709951ABC709951564                                                               |
      | transportDocumentReference | ABC709951ABC709951ABC709951ABC709951564                                                               |
      | transportEventTypeCode     | ABC7099                                                                                               |
      | transportCallID            | IGAcb79ijzjpLT4eicqw70C5X2lN591BhprgTwAkMeaRehoZ6OVcvMYGl0Hyb35jR2tWrWafyUBo89dIXRd1MVJkYkdqleU6XbBxY |
      | vesselIMONumber            | ABsC70sss99                                                                                           |
      | exportVoyageNumber         | pZwVm6KNaM6VWTty7yqNEQvB5pf8ElUCzdlu2kWWy4QCMpOnXU3                                                   |
      | carrierServiceCode         | ABsC70sss99                                                                                           |
      | UNLocationCode             | UNLocationCode                                                                                        |
      | equipmentEventTypeCode     | ABCDES                                                                                                |
      | equipmentReference         | dsfdsAPZU4812090APZU4812090APZU4812090                                                                |
      | eventCreatedDateTime       | 2021-11-31T17:18:46.160973Z                                                                           |



