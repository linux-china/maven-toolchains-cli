Maven toolchains CLI
====================

CLI for Maven toolchains.xml

# Features

* Manage ~/.m2/toolchains.xml
* OpenJDK auto install:  https://adoptopenjdk.net/index.html  and  https://github.com/graalvm/graalvm-ce-builds/releases

# How to install?

```
$ brew install linux-china/tap/maven-toolchains-cli
$ mt --version
```

# commands

* all  - List all JDK on host
* list - List JDK in toolchains.xml
* add  - Add JDK from local or remote into toolchains.xml `mt add 1.8 /Library/Java/JavaVirtualMachines/jdk1.8.0_291.jdk`  or `mt add 16`
* delete - Delete JDK in toolchains.xml
* vendors - List JDK Vendors

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

# How to use toolchains for development?

Please add toolchains profile in your pom.xml

```xml
   <profiles>
      <profile>
            <id>toolchains</id>
            <activation>
                <file>
                    <exists>${env.HOME}/.m2/toolchains.xml</exists>
                </file>
            </activation>
            <build>
                <plugins>
                    <plugin>
                        <groupId>org.apache.maven.plugins</groupId>
                        <artifactId>maven-toolchains-plugin</artifactId>
                        <version>3.0.0</version>
                        <executions>
                            <execution>
                                <goals>
                                    <goal>toolchain</goal>
                                </goals>
                            </execution>
                        </executions>
                        <configuration>
                            <toolchains>
                                <jdk>
                                    <version>17</version>
                                </jdk>
                            </toolchains>
                        </configuration>
                    </plugin>
                </plugins>
            </build>
        </profile>
      </profiles>
```

# References

* Apache Maven Toolchains Plugin: https://maven.apache.org/plugins/maven-toolchains-plugin/index.html
* Maven toolchains.xml generator: https://gist.github.com/mathieucarbou/d7ce8fdf3e807e67ae07a9e79c66d82c
* Adoptium: Eclipse Temurin https://adoptium.net/
* Adoptium API: https://api.adoptium.net/q/swagger-ui/
* foojay DiscoClient:  https://github.com/foojayio/discoapi
* foojay Disco API: https://api.foojay.io/swagger-ui/
* GraalVM releases: https://github.com/graalvm/graalvm-ce-builds/releases
