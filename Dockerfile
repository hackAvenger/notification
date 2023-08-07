FROM openjdk:8-jdk-stretch

WORKDIR /app

COPY build/libs/notification-0.0.1-SNAPSHOT.jar notification-0.0.1-SNAPSHOT.jar

RUN wget https://download.newrelic.com/newrelic/java-agent/newrelic-agent/current/newrelic-java.zip

RUN unzip newrelic-java.zip -d /app

EXPOSE 8080

ENTRYPOINT ["java","-javaagent:newrelic/newrelic.jar","-jar","notification-0.0.1-SNAPSHOT.jar"]