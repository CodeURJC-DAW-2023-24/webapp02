FROM openjdk:18-jdk-slim

WORKDIR /app

COPY --from=build /app/target/candread-0.0.1-SNAPSHOT.jar.original /app/candread.jar
EXPOSE 8443

CMD ["java", "-jar", "/app/candread.jar"]
