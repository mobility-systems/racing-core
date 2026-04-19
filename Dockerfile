FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /workspace

COPY mobility-common ./mobility-common
RUN mvn -f mobility-common/pom.xml -B -Dmaven.test.skip=true clean install

COPY racing-core ./racing-core
RUN mvn -f racing-core/pom.xml -B -Dmaven.test.skip=true clean package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /workspace/racing-core/target/*.jar app.jar
EXPOSE 8086
ENTRYPOINT ["java","-jar","/app/app.jar"]