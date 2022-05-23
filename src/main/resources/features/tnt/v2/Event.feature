Feature:
  TNT Self Certification Check List


  Scenario:TNT.2.2.DPY.PRV.2_Major Version number must be present in URL_GET /events Major Version check
    Given API End point "/events" for "Event"
    When Send GET http request


  Scenario:TNT.2.2.API.PRV.1_Full Version number present in response headers_GET /events Full Version check
    Given API End point "/events" for "Event"
    When Send GET http request
    Then Receive headers in response
      | API-Version |


  Scenario:TNT.2.2.API.PRV.3_Content type headers must be application/json_GET /events Content type check
    Given API End point "/events" for "Event"
    When Send GET http request
    Then Receive headers in response
      | Content-Type |


  Scenario:TNT.2.2.API.PRV.4_Links to current, previous, next, first and last page SHOULD be available in the response headers_GET /events pagination check
    Given API End point "/events" for "Event"
    When Send GET http request
    Then Receive headers in response
      | Current-Page |


  Scenario Outline:TNT.2.2.ERR.PRV.400_Test case validating "bad request" - HTTP 400_GET /events with invalid parameter name
    Given API End point "/events" for "Event"
    And Query parameter "<parameter>" with value "<value>"
    When Send GET http request
    Then Receive invalid response for GET
    Examples: List of query parameters
      | parameter  | value    |
      | eventType1 | SHIPMENT |

  Scenario:TNT.2.2.EVN.PRV.1_GET/Event Returns all events_GET /events without any query parameter
    Given API End point "/events" for "Event"
    When Send GET http request
    Then Receive valid response for GET all
    Then Validated against schema


  Scenario:TNT.2.2.EVN.PRV.1_GET/Event Returns all events_GET /events with limit parameter
    Given API End point "/events" for "Event"
    And Query parameters with values
      | parameter | value |
      | limit     | 10    |
    When Send GET http request
    Then Receive valid response for GET all
    Then Validated against schema


  Scenario Outline:TNT.2.2.EVN.PRV.1_GET/Event Returns all events_GET /events with filter parameters
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



  Scenario Outline:TNT.2.2.ERR.PRV.400_Test case validating "bad request" - HTTP 400_GET /events with invalid values of filter parameters
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

  Scenario Outline:TNT.2.2.ERR.PRV.400_Test case validating "bad request" - HTTP 400_GET /events with invalid parameter values
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



