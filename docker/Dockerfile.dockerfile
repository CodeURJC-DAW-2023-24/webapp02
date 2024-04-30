FROM node:20.12.2

WORKDIR /angular

# Copy the dependencies
COPY frontend/package.json /angular/package.json
COPY frontend/package-lock.json /angular/package-lock.json
COPY frontend/angular.json /angular/angular.json
COPY frontend/tsconfig.json /angular/tsconfig.json
COPY frontend/tsconfig.app.json /angular/tsconfig.app.json
COPY frontend/tsconfig.spec.json /angular/tsconfig.spec.json

# Install the dependencies
RUN npm install

COPY frontend/src  /angular/src

#We run this command because we want to generate the angular archives and we can't use the 'ng' command
RUN npm run prod && mv /angular/dist/browser /angular/dist/new

#Create the .jar
FROM maven:3.8.5-openjdk-17-slim AS builder

WORKDIR /project

COPY backend/pom.xml .

RUN mvn dependency:go-offline

COPY backend/src ./src

RUN mvn clean package -DskipTests

#Execute the .jar
#openjdk:17-oracle
FROM openjdk:22-ea-17-slim

WORKDIR /app

COPY --from=builder /project/target/*.jar /app/candread.jar
EXPOSE 8443

ENTRYPOINT ["java", "-jar", "candread.jar"]