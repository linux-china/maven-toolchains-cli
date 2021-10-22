package org.mvnsearch.service;

import java.io.File;


public interface GraalVMService {
    
    JdkDownloadLink findRelease(String graalVersion, String vendor) throws Exception;

    File downloadAndExtract(String link, String fileName, String destDir) throws Exception;

}
