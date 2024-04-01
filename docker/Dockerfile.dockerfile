#Create the .jar
FROM maven:3.8.5-openjdk-17-slim AS builder

WORKDIR /project

COPY ../backend/pom.xml .

RUN mvn dependency:go-offline

COPY ../backend/src ./src

RUN mvn clean package -DskipTests

#Execute the .jar
#openjdk:17-oracle
FROM openjdk:22-ea-17-slim

WORKDIR /app

COPY --from=builder /project/target/*.jar /app/candread.jar
EXPOSE 8443

ENTRYPOINT ["java", "-jar", "candread.jar"]