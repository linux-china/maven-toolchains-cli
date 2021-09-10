Maven toolchains CLI
====================

CLI for Maven toolchains.xml

# Features

* Manage ~/.m2/toolchains.xml
* OpenJDK auto install:  https://adoptopenjdk.net/index.html  and  https://github.com/graalvm/graalvm-ce-builds/releases

# commands

* all  - List all JDK on host
* list - List tools in toolchains.xml
* add  - Add JDK into toolchains.xml `mt add 1.8 /Library/Java/JavaVirtualMachines/jdk1.8.0_291.jdk`  or `mt add 16`
* delete - Delete JDK in toolchains.xml

# JDK Install Directories

* system: /Library/Java/JavaVirtualMachines
* IntelliJ: ~/Library/Java/JavaVirtualMachines/
* sdkman: ~/.sdkman/candidates/java/
* gradle: ~/.gradle/jdks
* jenv: ~/.jenv/candidates/java/
* maven: ~/.m2/jdks

### OS

* mac
* windows
* linux

### architecture

* x64
* x32
* aarch64(arm64)

# References

* Apache Maven Toolchains Plugin: https://maven.apache.org/plugins/maven-toolchains-plugin/index.html
* Maven toolchains.xml generator: https://gist.github.com/mathieucarbou/d7ce8fdf3e807e67ae07a9e79c66d82c
* AdoptOpenJDK API v3: https://api.adoptopenjdk.net/
* DiscoClient: foojay Disco API https://github.com/foojayio/discoapi
* GraalVM releases: https://github.com/graalvm/graalvm-ce-builds/releases
