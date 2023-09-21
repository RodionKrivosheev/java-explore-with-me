FROM amazoncorretto:11-alpine-jdk
ENV JAVA_TOOL_OPTIONS = ""
COPY target/*.jar stats-server.jar
ENTRYPOINT ["java","-jar","/stats-server.jar"]