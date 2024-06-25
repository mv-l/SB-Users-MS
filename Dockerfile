# Один образ и для сборки проекта и для генерации JRE.
FROM maven:3.9.5-eclipse-temurin-21-alpine as build

# Собираем проект.
COPY . /temp
WORKDIR /temp
RUN mvn clean package

# Генерируем JRE.
RUN ["jlink", "--compress=2", \
     "--module-path", "/opt/java/openjdk/jmods/", \
     "--add-modules", "java.base,java.compiler,java.desktop,java.instrument,java.net.http,java.prefs,java.rmi,java.scripting,java.security.jgss,java.security.sasl,java.sql.rowset,jdk.jfr,jdk.management,jdk.unsupported", \
     "--strip-debug", "--no-header-files", "--no-man-pages", \
     "--output", "/slim-jre"]

# Делаем финальный образ.
FROM alpine:3.20
COPY --from=build  /slim-jre /opt/jdk
COPY --from=build /temp/target/*.jar /opt/app.jar
ENV PATH=$PATH:/opt/jdk/bin
CMD ["sh", "-c", "java -jar /opt/app.jar"]
