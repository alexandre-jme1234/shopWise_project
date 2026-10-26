FROM eclipse-temurin:21 as builder

WORKDIR /usr/src/app

COPY . /usr/src/app
RUN ./mvnw package

FROM eclipse-temurin:21

COPY --from=builder /usr/src/app/target/*.jar /app.jar

EXPOSE 8080

ENTRYPOINT ["java"]
CMD ["-jar", "/app.jar"]
