FROM maven:3.9-eclipse-temurin-17-alpine AS MAVEN_BUILD
WORKDIR /usr/src/myapp
COPY ./ ./
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /usr/bin/myapp
COPY --from=MAVEN_BUILD "/usr/src/myapp/target/*.jar" "./"
# Set ports
EXPOSE "5432"
# Main function
CMD "java" "-jar" "fhh-bot-0.0.1-SNAPSHOT.jar"