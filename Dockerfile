FROM openjdk:17
RUN mkdir -p /ctk
RUN mkdir -p /ctk/config/tnt/v2/
WORKDIR /ctk
COPY suitexmls/ /ctk/suitexmls/
COPY config/tnt/v2/EventSubscription.json /ctk/config/tnt/v2/
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline
COPY src ./src
RUN ./mvnw clean package spring-boot:repackage
CMD ["./mvnw", "spring-boot:run"]