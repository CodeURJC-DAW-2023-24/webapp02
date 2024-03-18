#Create the .jar
FROM maven:3.8.7-openjdk-18-slim AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

#Execute the .jar
FROM openjdk:18-jdk-slim

WORKDIR /app

COPY --from=build /app/target/candread-0.0.1-SNAPSHOT.jar candread.jar
EXPOSE 8443

CMD ["java", "-jar", "candread.jar"]