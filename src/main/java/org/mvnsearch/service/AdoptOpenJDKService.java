package org.mvnsearch.service;


import org.mvnsearch.model.jdk.JdkRelease;

public interface AdoptOpenJDKService {

    JdkRelease[] findReleases(String version) throws Exception;
}
