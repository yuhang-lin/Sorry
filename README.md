# Sorry
A board game Sorry! implemented in Java.

| CI Server | OS      | Status |
| --------- | ------- | ------ |
| Travis CI | Linux   | [![Build Status](https://travis-ci.org/yuhang-lin/Sorry.svg?branch=master)](https://travis-ci.org/yuhang-lin/Sorry)| 

Game rules can be found on [Wikipedia](https://en.wikipedia.org/wiki/Sorry!_(game) "Sorry! game")

To build this project on Ubuntu (tested on Ubuntu 16.04):
```bash
sudo apt-get install default-jdk
chmod +x ./gradlew 
sudo apt-get install openjfx
./gradlew build
./gradlew run
```

To build this project on Windows (tested on Windows 10 and make sure you installed the latest JDK version):
```
gradlew build
gradlew run
```