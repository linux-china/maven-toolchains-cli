build:
   mvn -DskipTests clean package

help: build
  java -jar target/mt.jar --help
