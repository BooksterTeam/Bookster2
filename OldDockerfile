FROM beevelop/java

COPY ./ /opt/

ENV NODEJS_VERSION=5.11.0 \
    PATH=$PATH:/opt/node/bin:/opt/bin

WORKDIR /opt

RUN apt-get update && apt-get install -y curl ca-certificates git ssh tar && \
    curl -sL https://nodejs.org/dist/v${NODEJS_VERSION}/node-v${NODEJS_VERSION}-linux-x64.tar.gz | tar xz --strip-components=1 && \
    npm i -g bower && \
    cd .. && npm i

CMD './gradlew'