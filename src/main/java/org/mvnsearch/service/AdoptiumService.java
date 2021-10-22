package org.mvnsearch.service;


import org.jetbrains.annotations.Nullable;
import org.mvnsearch.model.adoptium.JdkRelease;

import java.io.File;

public interface AdoptiumService {

    @Nullable
    JdkDownloadLink findReleases(String version) throws Exception;

    File downloadAndExtract(String link, String fileName, String destDir) throws Exception;
}
