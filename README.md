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

# Todo 

maven-toolchains-plugin的调整： 能够从 https://api.foojay.io/swagger-ui/ 自动下载和安装JDK

```
AOJ("AOJ", "aoj"),
    AOJ_OPENJ9("AOJ OpenJ9", "aoj_openj9"),
    CORRETTO("Corretto", "corretto"),
    DRAGONWELL("Dragonwell", "dragonwell"),
    GRAALVM_CE8("Graal VM CE 8", "graalvm_ce8"),
    GRAALVM_CE11("Graal VM CE 11", "graalvm_ce11"),
    LIBERICA("Liberica", "liberica"),
    LIBERICA_NATIVE("Liberica Native", "liberica_native"),
    MANDREL("Mandrel", "mandrel"),
    OJDK_BUILD("OJDKBuild", "ojdk_build"),
    ORACLE("Oracle", "oracle"),
    ORACLE_OPEN_JDK("Oracle OpenJDK", "oracle_openjdk"),
    RED_HAT("Red Hat", "redhat"),
    SAP_MACHINE("SAP Machine", "sapmachine"),
    TRAVA("Trava", "trava"),
    ZULU("Zulu", "zulu"),

```

https://api.foojay.io/disco/v1.0/packages?distro=oracle&version=17.0.0.0&latest=overall&operating_system=macos&libc_type=libc&architecture=amd64&bitness=64&archive_type=tar.gz&package_type=jdk&discovery_scope_id=directly_downloadable&javafx_bundled=false&directly_downloadable=true&release_status=ga
       
* archive type: tar.gz, zip
* os: linux, macos, windows
* architecture: x64, x32, arm64
      
支持跳过toolchain的支持，如在CI/CD中，Java的环境已经设置正确啦，如Github Actions中，Java的版本都提前设置好啦，这个时候可以使用
`-Dmaven.toolchains.skip=true` 跳过toolchains的生效。


http client:  http://maven.apache.org/wagon/

* 下载: wagon-http wagon-http-3.4.3-shaded.jar
* 解压: Codehaus Plexus archiver
        
```
protected boolean install() throws IOException {
  HttpURLConnection.setFollowRedirects(true);
  final URL url = new URL(DYNDB_URL + DYNDB_TAR);

  final File downloadFile = new File(dynLocalDir, DYNDB_TAR);
  if (!downloadFile.exists()) {
    try (FileOutputStream fos = new FileOutputStream(downloadFile)) {
      IOUtils.copyLarge(url.openStream(), fos);
      fos.flush();
    }
  }

  final TarGZipUnArchiver unarchiver = new TarGZipUnArchiver();
  unarchiver.enableLogging(new ConsoleLogger(Logger.WARN, "DynamoDB Local Unarchive"));
  unarchiver.setSourceFile(downloadFile);
  unarchiver.setDestDirectory(dynLocalDir);
  unarchiver.extract();

  if (!downloadFile.delete()) {
    LOGGER.warn("cannot delete " + downloadFile.getAbsolutePath());
  }

  // Check the install
  if (!isInstalled()) {
    LOGGER.error("DynamoDB Local install failed");
    return false;
  }

  return true;
}

```

# References

* Apache Maven Toolchains Plugin: https://maven.apache.org/plugins/maven-toolchains-plugin/index.html
* Maven toolchains.xml generator: https://gist.github.com/mathieucarbou/d7ce8fdf3e807e67ae07a9e79c66d82c
* Adoptium: Eclipse Temurin https://adoptium.net/
* Adoptium API: https://api.adoptium.net/q/swagger-ui/
* foojay DiscoClient:  https://github.com/foojayio/discoapi
* foojay Disco API: https://api.foojay.io/swagger-ui/
* GraalVM releases: https://github.com/graalvm/graalvm-ce-builds/releases
