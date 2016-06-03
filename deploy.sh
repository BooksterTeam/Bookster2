#!/bin/bash
git branch

ssh-keyscan bookster.bee.pw >> ~/.ssh/known_hosts
git remote add foobar dokku@bookster.bee.pw:foobar.bookster.bee.pw


cp build/libs/bookster-2-0.0.1-SNAPSHOT.war ./app.war

git status

git pull
git checkout -b master
ls -l
git push -u foobar master