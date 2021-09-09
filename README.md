Maven toolchains CLI
====================


# Features

* 管理 ~/.m2/toolchains.xml
* 自动安装openjdk:  https://adoptopenjdk.net/index.html   和  https://github.com/graalvm/graalvm-ce-builds/releases

# commands

* list: mt list
* add：mt --vendor=oracle 1.8
* delete: mt delete 1.8

# JDK Install Directories

* system: /Library/Java/JavaVirtualMachines
* IntelliJ: ~/Library/Java/JavaVirtualMachines/
* sdkman: ~/.sdkman/candidates/java/
* gradle: ~/.gradle/jdks
* jenv: ~/.jenv/candidates/java/
* maven: ~/.m2/jdks

# download

AdoptOpenJDK提供了对应的API，地址为  https://api.adoptopenjdk.net/v3/assets/latest/16/hotspot?release=latest&jvm_impl=hotspot&vendor=adoptopenjdk
在HTTP URL中替换一下对应的版本号就可以。

### OS

* mac
* windows
* linux

### architecture

* x64
* x32
* aarch64(arm64)

# GraalVM releases

https://github.com/graalvm/graalvm-ce-builds/releases

# References

* Apache Maven Toolchains Plugin: https://maven.apache.org/plugins/maven-toolchains-plugin/index.html
* Maven toolchains.xml generator: https://gist.github.com/mathieucarbou/d7ce8fdf3e807e67ae07a9e79c66d82c
* DiscoClient: foojay Disco API https://github.com/foojayio/discoapi
