build:
   mvn -DskipTests package

help: build
  java -jar target/mt.jar --help

list: build
  java -jar target/mt.jar list

add: build
  java -jar target/mt.jar add 16

add_loom: build
  java -jar target/mt.jar add 18 /Users/linux_china/.jenv/candidates/java/loom

delete_loom:
  java -jar target/mt.jar delete 18

all: build
  java -jar target/mt.jar all

plugin: build
  java -jar target/mt.jar plugin 8

native-build:
  mvn -Pnative -DskipTests clean package
