#!/bin/bash
ssh-keyscan bookster.bee.pw >> ~/.ssh/known_hosts
git remote add foobar git+ssh://dokku@bookster.bee.pw:foobar.bookster.bee.pw
git push -u foobar master