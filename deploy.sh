#!/bin/bash
git branch
ssh-keyscan bookster.bee.pw >> ~/.ssh/known_hosts
git remote add foobar dokku@bookster.bee.pw:foobar.bookster.bee.pw
ls -l
gcmsg "deployed"
git checkout -b master
git push origin :master
git push foobar master
