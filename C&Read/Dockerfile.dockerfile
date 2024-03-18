#Create the .jar
FROM maven:3.8.7-openjdk-18-slim AS builder

WORKDIR /project

COPY pom.xml .
COPY src ./src

RUN mvn package -DskipTests

#Execute the .jar
FROM openjdk:18-jdk-slim

WORKDIR /app

COPY --from=builder /project/target/*.jar /app/
EXPOSE 8443

CMD ["java", "-jar", "candread-0.0.1-SNAPSHOT.jar"]