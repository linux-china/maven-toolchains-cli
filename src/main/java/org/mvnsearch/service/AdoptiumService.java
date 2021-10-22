package org.mvnsearch.service;


import org.jetbrains.annotations.Nullable;

import java.io.File;

public interface AdoptiumService {

    @Nullable
    JdkDownloadLink findRelease(String version) throws Exception;

    File downloadAndExtract(String link, String fileName, String destDir) throws Exception;
}
