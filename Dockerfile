FROM openjdk:17
EXPOSE $CALLBACK_PORT

RUN mkdir -p /ctk/reports
RUN mkdir -p /ctk/testdata
COPY target/DCSA-Validator-Toolkit-*.jar /ctk/DCSA-Validator-Toolkit.jar
copy suitexmls/TNT-TestSuite.xml /ctk/
copy suitexmls/TNT-Notification-TestSuite.xml /ctk/
copy src/main/resources/config/v2/testdata.json /ctk/testdata/
copy src/main/resources/config/v2/config.json /ctk/testdata/
copy src/main/java/org/dcsa/api/validator/features/ /ctk/src/main/java/org/dcsa/api/validator/features
copy run_ctk.sh /ctk/
WORKDIR /ctk/

ENTRYPOINT ["/bin/bash", "./run_ctk.sh"]