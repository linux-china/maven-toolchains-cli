build:
   mvn -DskipTests package

help: build
  java -jar target/mt.jar --help

list: build
  java -jar target/mt.jar list

add: build
  java -jar target/mt.jar add --vendor=graalvm 11

all: build
  java -jar target/mt.jar all

native-build:
  mvn -Pnative -DskipTests clean package
