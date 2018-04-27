# Sorry
A board game Sorry! implemented in Java.

| CI Server | OS      | Status |
| --------- | ------- | ------ |
| Travis CI | Linux   | [![Travis CI build status](https://travis-ci.com/yuhang-lin/Sorry.svg?token=dC6ix7pFVFefei5A1V7f&branch=master)](https://travis-ci.com/yuhang-lin/Sorry) | 

Game rules can be found on [Wikipedia](https://en.wikipedia.org/wiki/Sorry!_(game) "Sorry! game")

To build this project on Ubuntu (tested on Ubuntu 16.04):
```bash
sudo apt-get install default-jdk
chmod +x ./gradlew 
sudo apt-get install openjfx
./gradlew build
./gradlew run
```