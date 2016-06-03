#!/bin/bash
git branch




#cp build/libs/bookster-2-0.0.1-SNAPSHOT.war ./app.war

cd ..
mkdir build
cd build
cp ../Bookster2/build/libs/bookster-2-0.0.1-SNAPSHOT.war ./app.war
mv ../Bookster2/Dockerfile ./
ls -l
pwd

ssh-keyscan bookster.bee.pw >> ~/.ssh/known_hosts

git init
git remote add foobar dokku@bookster.bee.pw:foobar.bookster.bee.pw
git config --global user.email "dokku@asdfsadf.com"
git config --global user.name "dokku"
git add -A
git commit -m "Foobat"
git push origin :master
git push -u foobar master

