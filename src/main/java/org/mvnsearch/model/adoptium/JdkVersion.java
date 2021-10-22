package org.mvnsearch.model.adoptium;

import com.fasterxml.jackson.annotation.JsonProperty;


public class JdkVersion {
    private int build;
    private int major;
    private int minor;
    @JsonProperty("openjdk_version")
    private String openjdkVersion;
    private int security;
    private String semver;

    public int getBuild() {
        return build;
    }

    public void setBuild(int build) {
        this.build = build;
    }

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public String getOpenjdkVersion() {
        return openjdkVersion;
    }

    public void setOpenjdkVersion(String openjdkVersion) {
        this.openjdkVersion = openjdkVersion;
    }

    public int getSecurity() {
        return security;
    }

    public void setSecurity(int security) {
        this.security = security;
    }

    public String getSemver() {
        return semver;
    }

    public void setSemver(String semver) {
        this.semver = semver;
    }
}
