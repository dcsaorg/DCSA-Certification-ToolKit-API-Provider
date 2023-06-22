<p align="left">
  <a href="https://github.com/dcsaorg/DCSA-Certification-ToolKit-API-Provider"><img alt="GitHub Actions status" src="https://github.com/actions/setup-java/workflows/Main%20workflow/badge.svg"></a>
</p>

Status
-------------------------------------
This certification toolkit is targeted at the DCSA standard for Track and Trace v2.2.

This certification toolkit is currently in a "pre-alpha" development stage. It must not be used to establish conformance of implementations of the standard.

It is made available in order to allow early feedback on the toolkit itself.

# DCSA-Certification-ToolKit-API-Provider
Concept version of a new certification toolkit.

The DCSA conformance toolkit will be used to test a given implementation of
DCSA's OpenAPI against the reference implementation of API to ensure that all
endpoints are behaving as expected.

### Technology Stack
* OpenJDK Java 17
* Spring boot 
* Nodejs
* Postman/Newman
* Maven
* JSON

### Project Setup
#### 1. Clone the repository
`git clone https://github.com/dcsaorg/DCSA-Certification-ToolKit-API-Provider.git`

#### 1. Setup the environment
Export following environment variables

1. Install JDK
2. Install maven
3. Install Nodejs
4. Install newman by npm command ```npm install newman -g```

```shell
#There are few configuration is required. This configuration is can be given config/uploaded/application.properties.
# if not configuration provided, it will use standard spring boot resources       
server.port=9000
app.upload_config_path=config/uploaded // location for the upload dirtory
app.booking_delay=5000 // booking delay
app.event_path=config/uploaded/EventSubscription.json // path to event subscription 
```


#### 2. API under test

To check conformance of an API follow that API README 
* For TNT follow instruction @ https://github.com/dcsaorg/DCSA-TNT
* For OVS follow instruction @ https://github.com/dcsaorg/DCSA-OVS
* For booking and eBL follow instruction https://github.com/dcsaorg/DCSA-Edocumentation


#### 2. Run Compatibility Kit
Run the relevant test suite (here TnT APIs) with following options
```shell
mvn clean install
#Before following command keep given port free. 
mvn spring-boot:run 
```
Also run it by docker-compose:
```shell
// build and run
docker-compose up --build --remove-orphans
// run
docker-compose up
```
When CTK is running it runs on port 9000.   
We can start CTK conformance testing by the endpoint  
GET http://localhost:9000/{type of api}/{official or unofficial}  

To run TNT official conformance test  
GET http://localhost:9000/tnt/official  
To run eBL unofficial conformance test  
GET http://localhost:9000/ebl/unofficial  
To run ovs official conformance test  
GET http://localhost:9000/ovs/official


<mark>It is recommended to run the above GET request by any web browser. Because as soon as the conformance test is done, it will return an HTML report. </mark>
