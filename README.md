[![Build Status](https://travis-ci.org/BooksterTeam/Bookster2.svg?branch=master)](https://travis-ci.org/BooksterTeam/Bookster2)
[![Quality Gate](http://193.196.7.25/api/badges/gate?key=io.bookster:bookster-2)](http://193.196.7.25/overview?id=io.bookster%3Abookster-2)
[![codecov](https://codecov.io/gh/BooksterTeam/Bookster2/branch/master/graph/badge.svg)](https://codecov.io/gh/BooksterTeam/Bookster2)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/bcfbc186a6254da2b9f7b91e84223b70)](https://www.codacy.com/app/dan-brown/Bookster2?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=BooksterTeam/Bookster2&amp;utm_campaign=Badge_Grade)

# Bookster2

This application was generated using JHipster, you can find documentation and help at [https://jhipster.github.io](https://jhipster.github.io).

A simple instruction for installation can be found in the following blog article [Installation](http://trustmeiaman.engineer/2016/06/15/week-2-10-installation/)

## Development

Before you can build this project, you must install and configure the following dependencies on your machine:

1. [Node.js][]: We use Node to run a development web server and build the project.
   Depending on your system, you can install Node either from source or as a pre-packaged bundle.

After installing Node, you should be able to run the following command to install development tools (like
[Bower][] and [BrowserSync][]). You will only need to run this command when dependencies change in package.json.

    npm install

We use [Gulp][] as our build system. Install the Gulp command-line tool globally with:

    npm install -g gulp

Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

    ./gradlew
    gulp

Bower is used to manage CSS and JavaScript dependencies used in this application. You can upgrade dependencies by
specifying a newer version in `bower.json`. You can also run `bower update` and `bower install` to manage dependencies.
Add the `-h` flag on any command to see how you can use it. For example, `bower update -h`.


## Building for production

To optimize the Bookster2 client for production, run:

    ./gradlew -Pprod clean bootRepackage

This will concatenate and minify CSS and JavaScript files. It will also modify `index.html` so it references
these new files.

To ensure everything worked, run:

    java -jar build/libs/*.war --spring.profiles.active=prod

Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.
