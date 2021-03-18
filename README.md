Maven toolchains CLI
====================


# Features

* 管理 ~/.m2/toolchains.xml
* 自动安装openjdk:  https://adoptopenjdk.net/index.html   和  https://github.com/graalvm/graalvm-ce-builds/releases

# commands

* list: mt list
* add：mt --vendor=oracle 1.8
* delete: mt delete 1.8

# References

* Apache Maven Toolchains Plugin: https://maven.apache.org/plugins/maven-toolchains-plugin/index.html
* Maven toolchains.xml generator: https://gist.github.com/mathieucarbou/d7ce8fdf3e807e67ae07a9e79c66d82c
