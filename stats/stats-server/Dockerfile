FROM amazoncorretto:11-alpine-jdk
ENV JAVA_TOOL_OPTIONS = ""
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]