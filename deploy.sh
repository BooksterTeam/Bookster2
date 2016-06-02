#!/bin/bash
git branch
ssh-keyscan bookster.bee.pw >> ~/.ssh/known_hosts
git remote add foobar dokku@bookster.bee.pw:foobar.bookster.bee.pw
ls -l
#cp build/libs/bookster-2-0.0.1-SNAPSHOT.war ./app.war

git checkout -b master
git push origin :master
git push foobar master
