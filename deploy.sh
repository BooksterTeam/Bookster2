#!/bin/bash
git branch
rm -rf .gitignore
git add .
gcmsg "deployed"
#ssh-keyscan bookster.bee.pw >> ~/.ssh/known_hosts
git remote add foobar dokku@bookster.bee.pw:foobar.bookster.bee.pw
git checkout -b master
git push origin :master
git push -u foobar master
