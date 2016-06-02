FROM beevelop/java

WORKDIR /opt

COPY ./ /opt/

RUN apt-get update && apt-get install -y curl ca-certificates git ssh && \
    curl -sL https://nodejs.org/dist/v${NODEJS_VERSION}/node-v${NODEJS_VERSION}-linux-x64.tar.gz | tar xz --strip-components=1 && \

    npm i -g bower
    npm i

CMD './gradlew'