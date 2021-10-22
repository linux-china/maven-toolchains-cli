package org.mvnsearch.service;

import io.foojay.api.discoclient.pkg.Pkg;

import java.io.File;
import java.util.List;


public interface FoojayService {
    List<Pkg> findReleases(String version,String vendor) throws Exception;

    File downloadAndExtract(String link, String fileName, String destDir) throws Exception;

}
