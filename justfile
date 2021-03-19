build:
   mvn -DskipTests clean package

help: build
  java -jar target/mt.jar --help

list: build
  java -jar target/mt.jar list

add: build
  java -jar target/mt.jar add --vendor=graalvm 11

native-build:
  mvn -Pnative -DskipTests clean package
