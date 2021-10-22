package org.mvnsearch.model.adoptium;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JdkRelease {
    @JsonProperty("release_name")
    private String releaseName;
    private JdkVersion version;
    private JdkBinary binary;

    public String getReleaseName() {
        return releaseName;
    }

    public void setReleaseName(String releaseName) {
        this.releaseName = releaseName;
    }

    public JdkVersion getVersion() {
        return version;
    }

    public void setVersion(JdkVersion version) {
        this.version = version;
    }

    public JdkBinary getBinary() {
        return binary;
    }

    public void setBinary(JdkBinary binary) {
        this.binary = binary;
    }

    public boolean isJDKAvailable(String os, String arch) {
      return binary.getOs().equals(os) && binary.getImageType().equals("jdk") && binary.getArchitecture().equals(arch);
    }

}
