FROM eclipse-temurin:17-jre-alpine
EXPOSE $CALLBACK_PORT

RUN mkdir -p /ctk/reports
RUN mkdir -p /ctk/testdata
COPY target/DCSA-Validator-Toolkit-*.jar /ctk/DCSA-Validator-Toolkit.jar
COPY suitexmls/ /ctk/suitexmls/
copy run_ctk.sh /ctk/
WORKDIR /ctk/

ENTRYPOINT ["/bin/sh", "./run_ctk.sh"]