package org.mvnsearch.service.impl;

import org.mvnsearch.service.GraalVMService;
import org.mvnsearch.service.JdkDownloadLink;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class GraalVMServiceImpl extends JdkDistributionSupport implements GraalVMService {
    @Override
    public JdkDownloadLink findRelease(String graalVersion, String vendor) throws Exception {
        String version;
        if (vendor.equals("graalvm_ce17")) {
            version = "17";
        } else if (vendor.equals("graalvm_ce8")) {
            version = "8";
        } else {
            version = "11";
        }
        String os = getOsName();
        if (os.equals("mac")) {
            os = "darwin";
        }
        if (!graalVersion.endsWith(".0")) {
            graalVersion = graalVersion + ".0";
        }
        String extension = os.equals("windows") ? "zip" : "tar.gz";
        final String url = String.format("https://github.com/graalvm/graalvm-ce-builds/releases/download/vm-%s/graalvm-ce-java%s-%s-amd64-%s.%s",
                graalVersion, version, os, graalVersion, extension);
        final String fileName = String.format("graalvm-ce-java%s-%s-amd64-%s.%s", graalVersion, os, graalVersion, extension);
        return new JdkDownloadLink(url, fileName);
    }

    @Override
    public File downloadAndExtract(String link, String fileName, String destDir) throws Exception {
        return null;
    }
}
