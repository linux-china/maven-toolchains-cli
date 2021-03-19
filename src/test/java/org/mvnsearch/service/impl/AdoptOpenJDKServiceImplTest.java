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
        String fileName = "OpenJDK11U-jdk_x64_mac_hotspot_11.0.10_9.tar.gz";
        String link = "https://github.com/AdoptOpenJDK/openjdk11-binaries/releases/download/jdk-11.0.10%2B9/OpenJDK11U-jdk_x64_mac_hotspot_11.0.10_9.tar.gz";
        File file = adoptOpenJDKService.downloadAndExtract(link, fileName, System.getProperty("user.home") + "/.m2/jdks");
        System.out.println(file.getAbsolutePath());
    }


    @Test
    public void testGetRootName() throws Exception {
        File tarGzFile = new File(System.getProperty("user.home"), ".m2/jdks/OpenJDK11U-jdk_x64_mac_hotspot_11.0.10_9.tar.gz");
        System.out.println(adoptOpenJDKService.getRootNameInArchive(tarGzFile));
    }
}
