build:
   mvn -DskipTests clean package

help: build
  java -jar target/mt.jar --help

list: build
  java -jar target/mt.jar list