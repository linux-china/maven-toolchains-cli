package org.mvnsearch.service;


import org.mvnsearch.model.jdk.JdkRelease;

import java.io.File;

public interface AdoptiumService {

    JdkRelease[] findReleases(String version) throws Exception;

    File downloadAndExtract(String link, String fileName, String destDir) throws Exception;
}
