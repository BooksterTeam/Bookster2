#!/bin/bash
curl -L "https://cli.run.pivotal.io/stable?release=linux64-binary&source=github" | tar -zx

./cf login --u ${cf.email} -p ${cf.password}
./cf push -f ./deploy/cloudfoundry/manifest.yml -p build/libs/bookster-2-0.0.1-SNAPSHOT.war
