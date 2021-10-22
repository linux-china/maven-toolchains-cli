package org.mvnsearch.service;

import java.io.File;


public interface FoojayService {
    JdkDownloadLink findRelease(String version, String vendor) throws Exception;

    File downloadAndExtract(String link, String fileName, String destDir) throws Exception;

}
