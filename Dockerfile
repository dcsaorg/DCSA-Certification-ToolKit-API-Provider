FROM eclipse-temurin:17-jre-alpine
EXPOSE $CALLBACK_PORT

RUN mkdir -p /ctk/reports
RUN mkdir -p /ctk/testdata
COPY target/DCSA-Validator-Toolkit-*.jar /ctk/DCSA-Validator-Toolkit.jar
#copy suitexmls/TNT-TestSuite.xml /ctk/
#copy suitexmls/TNT-Notification-TestSuite.xml /ctk/
COPY suitexmls/ /ctk/suitexmls/
#copy src/main/resources/config/v2/testdata.json /ctk/testdata/
#copy src/main/resources/config/v2/config.json /ctk/testdata/
copy run_ctk.sh /ctk/
WORKDIR /ctk/

ENTRYPOINT ["/bin/sh", "./run_ctk.sh"]