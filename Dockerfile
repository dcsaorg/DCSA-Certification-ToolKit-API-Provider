FROM eclipse-temurin:17-jre-alpine
EXPOSE $CALLBACK_PORT

RUN mkdir -p /ctk/reports
RUN mkdir -p /ctk/testdata
RUN mkdir -p /ctk/config/tnt/v2/
COPY target/DCSA-Validator-Toolkit-*.jar /ctk/DCSA-Validator-Toolkit.jar
COPY suitexmls/ /ctk/suitexmls/
COPY config/tnt/v2/EventSubscription.json /ctk/config/tnt/v2/
copy run_ctk.sh /ctk/
WORKDIR /ctk/

ENTRYPOINT ["/bin/sh", "./run_ctk.sh"]