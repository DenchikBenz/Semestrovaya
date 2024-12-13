FROM maven:3.9.6-amazoncorretto-17 as build

COPY src /home/app/src
COPY pom.xml /home/app/
RUN mvn -f /home/app/pom.xml clean package

FROM tomcat:9.0.96-jdk17-corretto

WORKDIR /usr/local/tomcat
COPY --from=build /home/app/target/Semestrovaya-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8070
