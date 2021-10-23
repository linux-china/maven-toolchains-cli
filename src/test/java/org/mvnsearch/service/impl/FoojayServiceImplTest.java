package org.mvnsearch.service.impl;


import org.junit.jupiter.api.Test;
import org.mvnsearch.service.JdkDownloadLink;

public class FoojayServiceImplTest {
    private FoojayServiceImpl foojayService = new FoojayServiceImpl();

    @Test
    public void testFindJDKReleases() throws Exception {
        final JdkDownloadLink jdkLink = foojayService.findRelease("11", "zulu");
        System.out.println(jdkLink.getUrl() + ":  " + jdkLink.getFileName());
    }

    @Test
    public void testFindGraalVMReleases() throws Exception {
        final JdkDownloadLink jdkLink = foojayService.findRelease("21.2", "graalvm_ce11");
        System.out.println(jdkLink.getUrl() + ":  " + jdkLink.getFileName());
    }

    @Test
    public void testFindTemurinReleases() throws Exception {
        final JdkDownloadLink jdkLink = foojayService.findRelease("17", "temurin");
        System.out.println(jdkLink.getUrl() + ":  " + jdkLink.getFileName());
    }
}
