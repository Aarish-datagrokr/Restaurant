#fetch basic image
FROM maven:3.8.6-openjdk-18 as build

#Application placed into app

WORKDIR /app

#Selectively add the pom xml file and install dependencies
COPY pom.xml /app

#rest of the project
COPY src/main /app/src/main

RUN mvn install 


FROM tomcat:10.0.20-jdk17-openjdk
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=build ./app/target/restaurant.war /usr/local/tomcat/webapps/ROOT.war 
CMD ["catalina.sh","run"]
#EXPOSE 8080