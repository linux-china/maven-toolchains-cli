Maven toolchains CLI
====================

CLI for Maven toolchains.xml

# Features

* Manage ~/.m2/toolchains.xml
* OpenJDK auto install:  https://adoptopenjdk.net/index.html  and  https://github.com/graalvm/graalvm-ce-builds/releases

# How to install?

```
brew install linux-china/tap/maven-toolchains-cli
```

# commands

* all  - List all JDK on host
* list - List tools in toolchains.xml
* add  - Add JDK into toolchains.xml `mt add 1.8 /Library/Java/JavaVirtualMachines/jdk1.8.0_291.jdk`  or `mt add 16`
* delete - Delete JDK in toolchains.xml

# JDK Install Directories

* system:

   * mac: /Library/Java/JavaVirtualMachines
   * windows: C:\Program Files\Java\
   * linux:  /usr/lib/jvm

* IntelliJ: ~/Library/Java/JavaVirtualMachines/
* sdkman: ~/.sdkman/candidates/java/
* gradle: ~/.gradle/jdks
* jenv: ~/.jenv/candidates/java/
* maven: ~/.m2/jdks
* jbang: ~/.jbang/cache/jdks

### OS

* mac
* windows
* linux

### architecture

* x64
* x32
* aarch64(arm64)


# GitHub Actions configuration

Please add following code in your flow YAML file.

```yaml
- uses: actions/setup-java@v2
  with:
    distribution: 'temurin'
    java-version: '11'
- name: Set up Toolchain
  shell: bash
  run: |
    mkdir -p $HOME/.m2 \
    && cat << EOF > $HOME/.m2/toolchains.xml
    <?xml version="1.0" encoding="UTF8"?>
    <toolchains>
      <toolchain>
        <type>jdk</type>
          <provides>
            <version>11</version>
          </provides>
          <configuration>
            <jdkHome>${{ env.JAVA_HOME }}</jdkHome>
          </configuration>
      </toolchain>
    </toolchains>
    EOF
```

# References

* Apache Maven Toolchains Plugin: https://maven.apache.org/plugins/maven-toolchains-plugin/index.html
* Maven toolchains.xml generator: https://gist.github.com/mathieucarbou/d7ce8fdf3e807e67ae07a9e79c66d82c
* Adoptium: Eclipse Temurin https://adoptium.net/
* Adoptium API: https://api.adoptium.net/q/swagger-ui/
* foojay DiscoClient:  https://github.com/foojayio/discoapi
* foojay Disco API: https://api.foojay.io/swagger-ui/
* GraalVM releases: https://github.com/graalvm/graalvm-ce-builds/releases
