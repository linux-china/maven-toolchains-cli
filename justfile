export MT_VERSION := "0.5.0"

build:
  mvn -q -DskipTests package

help: build
  java -jar target/mt.jar --help

list: build
  java -jar target/mt.jar list

vendors: build
  java -jar target/mt.jar vendors

add: build
  java -jar target/mt.jar add 17

add_loom: build
  java -jar target/mt.jar add 18 /Users/linux_china/.jenv/candidates/java/loom

delete_loom:
  java -jar target/mt.jar delete 18

all: build
  java -jar target/mt.jar all

plugin: build
  java -jar target/mt.jar plugin 8

run_with_agent:
   mkdir -p target/classes/META-INF/native-image
   java -agentlib:native-image-agent=config-output-dir=target/classes/META-INF/native-image -jar target/mt.jar list

native-build:
  mvn -Pnative -DskipTests native:compile

native-tar:
  rm -rf mt-{{MT_VERSION}}-mac-x64.tar
  rm -rf target/mt-{{MT_VERSION}}
  mkdir -p target/mt-{{MT_VERSION}}/bin
  cp target/mt target/mt-{{MT_VERSION}}/bin
  (cd target ; tar cf mt-{{MT_VERSION}}-mac-x64.tar mt-{{MT_VERSION}})
  shasum -a 256 target/mt-{{MT_VERSION}}-mac-x64.tar