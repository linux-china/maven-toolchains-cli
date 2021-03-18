package org.mvnsearch.model;

import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class Toolchain {
    private String type;
    private Map<String, Object> provides;
    private Map<String, Object> configuration;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> getProvides() {
        return provides;
    }

    public void setProvides(Map<String, Object> provides) {
        this.provides = provides;
    }

    public Map<String, Object> getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Map<String, Object> configuration) {
        this.configuration = configuration;
    }

    public String findVersion() {
        return (String) getProvides().get("version");
    }

    @Nullable
    public String findVendor() {
        return (String) getProvides().get("vendor");
    }

    public String findJdkHome() {
        return (String) getConfiguration().get("jdkHome");
    }
}
