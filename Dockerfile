FROM adoptopenjdk:11-jre-hotspot
COPY target/posibles.clientes-1.0.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]