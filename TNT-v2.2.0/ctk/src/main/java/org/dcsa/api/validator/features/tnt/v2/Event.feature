Feature:
  TNT Self Certification Check List

  @HappyPath
  Scenario:GET-EVENT-001_GET/Event Returns all events
    Given API End point "/events" for "Event"
    When Send GET http request
    Then Receive valid response for GET all
    Then Validated against schema

  @HappyPath
  Scenario:GET-EVENT-001_GET/Event Returns all events
    Given API End point "/events" for "Event"
    When Send GET http request
    Then Receive valid response for GET all
    Then Validated against schema

  @HappyPath
  Scenario:GET-EVENT-002_GET/Event_With limit parameter
    Given API End point "/events" for "Event"
    And Query parameters with values
      | parameter | value |
      | limit     | 10    |
    When Send GET http request
    Then Receive valid response for GET all
    Then Validated against schema

  @HappyPath
  Scenario Outline:GET-EVENT-003_GET/Event_With filter parameters
    Given API End point "/events" for "Event"
    And Query parameters "<parameters>"
    When Send GET http request
    Then Receive valid response for GET all
    Then Validated against schema
    Examples: List of filter parameters
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
      | eventCreatedDateTime       |


  @NegativeCase
  Scenario Outline:GET-EVENT-004_GET/Event_With invalid values of filter parameters
    Given API End point "/events" for "Event"
    And Query parameter "<parameter>" with value "<value>"
    When Send GET http request
    Then Receive invalid response for GET
    Examples: List of query parameters
      | parameter                  | value                                                                                                 |
      | eventType                  | SHIPMENT1                                                                                             |
      | documentTypeCode           | ABCDESEFF                                                                                             |
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

  @NegativeCase
  Scenario Outline:GET-EVENT-005_GET/Event_With invalid filter parameters
    Given API End point "/events" for "Event"
    And Query parameter "<parameter>" with value "<value>"
    When Send GET http request
    Then Receive invalid response for GET
    Examples: List of query parameters
      | parameter  | value    |
      | eventType1 | SHIPMENT |



