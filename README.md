# DCSA-Certification-ToolKit-API-Provider
Concept version of a new certification toolkit. 

The DCSA certification toolkit can be used to test a given implementation of
DCSA's OpenAPI against the reference test suite to ensure that all
endpoints are behaving as expected.


Testing an API
==============

To test an API, the implementor will need the following:

 * A running instance of the API that will be tested.
 * The API should be loaded with prerequisites reference data.
 * The certification toolkit should be configured with test data provided mentioned in the API testing instructions
 * The certification toolkit SHOULD only be run on test systems(production like)
 

Executing the certification toolkit
-----------------------------------

To test an implementation running v2 of the API, run
```shell
# This is where the API validator can find the implementation of the API
export API_ROOT_URI=http://localhost:9090/v2

# Run the relevant test suite (here TnT version 2)
mvn -Dtest.suite=TnTV2.xml test
```



