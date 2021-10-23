package org.mvnsearch.service.impl;

import org.junit.jupiter.api.Test;
import org.mvnsearch.service.JdkDownloadLink;

import java.io.File;

public class AdoptiumServiceImplTest {
    private AdoptiumServiceImpl adoptOpenJDKService = new AdoptiumServiceImpl();

    @Test
    public void testFindJDKReleases() throws Exception {
        final JdkDownloadLink jdkLink = adoptOpenJDKService.findRelease("11");
        System.out.println(jdkLink.getUrl() + ":" + jdkLink.getFileName());
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
