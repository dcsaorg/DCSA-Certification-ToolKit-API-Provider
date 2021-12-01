FROM debian:buster

RUN apt-get update \
    && DEBIAN_FRONTEND=noninteractive apt-get -y upgrade \
    && DEBIAN_FRONTEND=noninteractive apt-get -y install --no-install-recommends \
        openjdk-11-jre-headless \
    && rm -rf /var/lib/apt/lists/*

COPY  / /ctk/
WORKDIR /ctk/

ENTRYPOINT java -jar DCSA-Validator-Toolkit.jar  suitxmls/$TEST_SUITE