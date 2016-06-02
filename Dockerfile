FROM java:openjdk-8-jdk-alpine

# add directly the war
#ADD ./build/libs/bookster-2-0.0.1-SNAPSHOT.war /app2.war

RUN sh -c 'touch /app.war'
VOLUME /tmp
CMD ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.war"]

#EXPOSE 8080
