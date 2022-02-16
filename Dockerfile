FROM debian:buster

RUN apt-get update \
    && DEBIAN_FRONTEND=noninteractive apt-get -y upgrade \
    && DEBIAN_FRONTEND=noninteractive apt-get -y install --no-install-recommends \
        openjdk-11-jre-headless \
    && rm -rf /var/lib/apt/lists/*
EXPOSE $CALLBACK_PORT

RUN mkdir -p /ctk/reports
RUN mkdir -p /ctk/testdata
COPY target/DCSA-Validator-Toolkit-*-jar-with-dependencies.jar /ctk/DCSA-Validator-Toolkit.jar
copy suitexmls/TNT-TestSuite.xml /ctk/
copy suitexmls/TNT-Notification-TestSuite.xml /ctk/
copy src/main/resources/config/v2/testdata.json /ctk/testdata/
copy src/main/java/org/dcsa/api/validator/features/ /ctk/src/main/java/org/dcsa/api/validator/features
copy suitexmls/ /ctk/suitexmls/
copy run_ctk.sh /ctk/
WORKDIR /ctk/

ENTRYPOINT ["/bin/bash", "./run_ctk.sh"]