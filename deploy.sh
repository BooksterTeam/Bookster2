#!/bin/bash
git branch

ssh-keyscan bookster.bee.pw >> ~/.ssh/known_hosts


cp build/libs/bookster-2-0.0.1-SNAPSHOT.war ./app.war

cp build/libs/bookster-2-0.0.1-SNAPSHOT.war ../app.war
pwd
ls -l
cd ..
git remote add foobar dokku@bookster.bee.pw:foobar.bookster.bee.pw
git add app.war
git commit app.war
git push foobar master

#git commit app.war
#git status
#ls -l
#pwd
#git push foobar master