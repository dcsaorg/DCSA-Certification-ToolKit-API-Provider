Feature:
  OVS Self Certification Check List


  @HappyPath
  Scenario:POST /timestamps with mandatory fields
    Given API End point "/timestamps" for "Timestamp"
    When Set request for POST
    And Send a POST http request
    Then Receive valid response for POST

  @HappyPath
  Scenario Outline:POST /timestamps with Optional Fields
    Given API End point "/timestamps" for "Timestamp"
    When Set request for POST with test case "<testcase>"
    And Send a POST http request
    Then Receive valid response for POST
    Examples: List of test cases
      | testcase                                 |
      | TimeStampWithEventLocation               |
      | TimeStampWithVesselPosition              |
      | TimeStampWithModeOfTransport             |
      | TimeStampWithPortCallServiceTypeCode     |
      | TimeStampWithCombinationOfOptionalFields |

  @NegativePath
  Scenario:POST /timestamps with empty body
    Given API End point "/timestamps" for "Timestamp"
    When Set request empty body
    And Send a POST http request
    Then Receive response code "400"

  @NegativePath
  Scenario Outline:POST /timestamps with invalid field values
    Given API End point "/timestamps" for "Timestamp"
    And Placeholder "<placeholder>" with value "<value>"
    When Set request for POST
    And Send a POST http request
    Then Receive invalid response for POST
    Examples:
      | placeholder             | value      |
      | vesselPosition          | aBCDaFGHaE |
      | vesselPosition          |            |
      | eventLocation           | aBCDaFGHaE |
      | facilitySMDGCode        | aBCDaFGHaE |
      | modeOfTransport         | VES        |
      | modeOfTransport         |            |
      | portCallServiceTypeCode | VES        |
      | portCallServiceTypeCode |            |
      | publisher               | {sasds}    |
      | publisher               | {}         |
      | publisher               |            |
      | publisherRole           |            |
      | vesselIMONumber         | abcdfght   |
      | vesselIMONumber         |            |
      | UNLocationCode          | abcdfght   |
      | UNLocationCode          |            |
      | facilityTypeCode        | abcdfght   |
      | facilityTypeCode        |            |
      | eventClassifierCode     |            |
      | operationsEventTypeCode |            |
      | eventDateTime           | sdf        |
      | eventDateTime           |            |

  @NegativePath
  Scenario Outline:POST /timestamps with missing mandatory attributes
    Given API End point "/timestamps" for "Timestamp"
    And Attributes to be removed "<attributes>"
    When Set request for POST
    And Send a POST http request
    Then Receive invalid response for POST
    Examples: List of test cases
      | attributes              |
      | publisher               |
      | publisherRole           |
      | vesselIMONumber         |
      | UNLocationCode          |
      | facilityTypeCode        |
      | eventClassifierCode     |
      | operationsEventTypeCode |
      | eventDateTime           |