FROM java:openjdk-8-jdk-alpine

# add directly the war
# ADD build/libs/bookster-2-0.0.1-SNAPSHOT.war*.war /app.war

ADD ./ /

RUN sh -c 'touch /app.war'
VOLUME /tmp
CMD 'ls -l'
CMD
CMD ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/build/libs/bookster-2-0.0.1-SNAPSHOT.war"]

EXPOSE 8080
