package org.mvnsearch.service.impl;

import org.junit.jupiter.api.Test;
import org.mvnsearch.model.jdk.JdkRelease;

import java.io.File;
import java.util.stream.Stream;

public class AdoptOpenJDKServiceImplTest {
    private AdoptOpenJDKServiceImpl adoptOpenJDKService = new AdoptOpenJDKServiceImpl();

    @Test
    public void testFindJDKReleases() throws Exception {
        Stream<JdkRelease> releases = Stream.of(adoptOpenJDKService.findReleases("1.8"));
        releases.filter(jdkRelease -> jdkRelease.isJDKAvailable("windows", "x64"))
                .forEach(jdkRelease -> {
                    System.out.println(jdkRelease.getBinary().getJdkPackage().getName());
                    System.out.println(jdkRelease.getBinary().getJdkPackage().getLink());
                });

    }

    @Test
    public void testDownloadAndExtract() throws Exception {
        String fileName = "OpenJDK8U-jdk_x64_mac_hotspot_8u282b08.tar.gz";
        String link = "https://github.com/AdoptOpenJDK/openjdk8-binaries/releases/download/jdk8u282-b08/OpenJDK8U-jdk_x64_mac_hotspot_8u282b08.tar.gz";
        adoptOpenJDKService.downloadAndExtract(link, fileName, System.getProperty("user.home") + "/.m2/jdks");
    }


    @Test
    public void testGetRootName() throws Exception {
        File tarGzFile = new File(System.getProperty("user.home"), ".m2/jdks/OpenJDK8U-jdk_x64_mac_hotspot_8u282b08.tar.gz");
        System.out.println(adoptOpenJDKService.getRootNameInArchive(tarGzFile));
    }
}
