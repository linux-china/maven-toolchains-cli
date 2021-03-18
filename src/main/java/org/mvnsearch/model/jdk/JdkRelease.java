package org.mvnsearch.model.jdk;

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
}
