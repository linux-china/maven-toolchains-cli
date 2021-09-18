package org.mvnsearch.service.impl;

import org.junit.jupiter.api.Test;
import org.mvnsearch.model.jdk.JdkRelease;

import java.io.File;
import java.util.stream.Stream;

public class AdoptOpenJDKServiceImplTest {
    private AdoptOpenJDKServiceImpl adoptOpenJDKService = new AdoptOpenJDKServiceImpl();

    @Test
    public void testFindJDKReleases() throws Exception {
        Stream<JdkRelease> releases = Stream.of(adoptOpenJDKService.findReleases("11"));
        releases.filter(jdkRelease -> jdkRelease.isJDKAvailable("mac", "x64"))
                .forEach(jdkRelease -> {
                    System.out.println(jdkRelease.getBinary().getJdkPackage().getName());
                    System.out.println(jdkRelease.getBinary().getJdkPackage().getLink());
                });

    }


    @Test
    public void testDownloadAndExtract() throws Exception {
        String fileName = "jdk-17_macos-x64_bin.tar.gz";
        String link = "https://download.oracle.com/java/17/latest/jdk-17_macos-x64_bin.tar.gz";
        File file = adoptOpenJDKService.downloadAndExtract(link, fileName, System.getProperty("user.home") + "/.m2/jdks");
        System.out.println(file.getAbsolutePath());
    }


    @Test
    public void testGetRootName() throws Exception {
        File tarGzFile = new File(System.getProperty("user.home"), ".m2/jdks/graalvm-ce-java21.0.0.2-darwin-amd64-21.0.0.2.tar.gz");
        System.out.println(adoptOpenJDKService.getRootNameInArchive(tarGzFile));
    }
}
