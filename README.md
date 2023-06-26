<p>
  <a href="https://github.com/dcsaorg/DCSA-Certification-ToolKit-API-Provider"><img alt="GitHub Actions status" src="https://github.com/actions/setup-java/workflows/Main%20workflow/badge.svg"></a>
</p>

## Overview

### Purpose
The DCSA certification toolkit (CTK) for API providers checks the conformance of an API provider implementation with the implemented DCSA OpenAPI standard.

It consists of a generic conformance testing framework and of a set of test suites targeted at specific DCSA standards.

### Status
This certification toolkit and its standard-specific test suites are currently in a "pre-alpha" development stage and must not be used at this time to establish the conformance of an API provider implementation with a DCSA standard.

The CTK and its current test suites are made available by DCSA in order to allow the organizations that adopt DCSA standards to provide early feedback on the CTK.

### Technology Stack
The DCSA CTK for API providers is a Spring Boot application based on Java 17 and Maven.

The test suites are built using Postman and executed using Newman.

## Running conformance test suites
To run one or more conformance test suites:
1. Install the prerequisites.
2. Start the target API provider.
3. Start the CTK server.
4. Run the test suite.

### Installing prerequisites
* Install OpenJDK 17: https://openjdk.org/projects/jdk/17/
* Install Maven 3: https://maven.apache.org/download.cgi
* Install newman: https://github.com/postmanlabs/newman#installation

### Starting the API provider
To run conformance tests against the DCSA reference implementation of a supported standard, start the reference implementation as API provider using its own documentation:
* Track and Trace (version 2.2): https://github.com/dcsaorg/DCSA-TNT
* Operational Vessel Schedules: https://github.com/dcsaorg/DCSA-OVS
* Booking and eBL: https://github.com/dcsaorg/DCSA-Edocumentation

To run conformance tests against another target implementation of a supported standard, which is the primary intended use of the CTK and conformance test suites, use the documentation of that particular API implementation.

### Starting the CTK server
After cloning this repository, adjust if needed the default configuration: `config/uploaded/application.properties`.

To run the CTK server directly as a process on the local machine using Maven, in the project's root directory execute:
```shell
mvn clean install spring-boot:run 
```

Alternatively, to run the CTK server in a Docker container, in the project's root directory execute:
```shell
docker-compose up --build --remove-orphans
```

### Running a conformance test suite
By default, the CTK server listens on port 9000; if you have changed the port, adjust the URLs below accordingly.

A conformance test suite is launched by sending an HTTP GET request to a certain URL served by the CTK server. This is typically done by accessing the URL in a browser, in which case the test execution report is displayed as an HTML page when the test completes.

The general structure of the URL is:
```
http://localhost:9000/STANDARD_API_NAME/OFFICIAL_OR_UNOFFICIAL
```
...where:
* `STANDARD_API_NAME` identifies one of the supported standards (`ebl`, `ovs`, `tnt`)
* `OFFICIAL_OR_UNOFFICIAL` is either:
  * `official` to run the official test suite that verifies conformance with the DCSA standard
  * `unofficial` to run the DCSA internal test suite that verifies the unofficial APIs of a reference implementation

Example:
```
http://localhost:9000/tnt/official
```
