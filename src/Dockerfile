FROM openjdk

WORKDIR /app

EXPOSE 8080/tcp

COPY back/gc-bank/target/gc-bank-back-0.0.1-SNAPSHOT.jar /app/gc-bank.jar

ENTRYPOINT ["java", "-jar", "gc-bank.jar"]