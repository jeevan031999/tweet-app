FROM openjdk:11
EXPOSE 8080
ADD target/tweet-app.jar tweet-app.jar
ENTRYPOINT ["java","-jar","/tweet-app.jar"]

