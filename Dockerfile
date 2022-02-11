FROM debian:buster

RUN apt-get update \
    && DEBIAN_FRONTEND=noninteractive apt-get -y upgrade \
    && DEBIAN_FRONTEND=noninteractive apt-get -y install --no-install-recommends \
        openjdk-11-jre-headless \
    && rm -rf /var/lib/apt/lists/*
EXPOSE $CALLBACK_PORT
COPY  /ctk/ /ctk/
RUN mkdir -p /ctk/reports
WORKDIR /ctk/
ENTRYPOINT ["/bin/bash", "./run_ctk.sh"]