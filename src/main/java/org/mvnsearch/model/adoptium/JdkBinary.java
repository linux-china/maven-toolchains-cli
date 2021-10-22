package org.mvnsearch.model.adoptium;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JdkBinary {
    @JsonProperty("architecture")
    private String architecture;
    @JsonProperty("image_type")
    private String imageType;
    @JsonProperty("os")
    private String os;
    @JsonProperty("package")
    private JdkPackage jdkPackage;

    public String getArchitecture() {
        return architecture;
    }

    public void setArchitecture(String architecture) {
        this.architecture = architecture;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public JdkPackage getJdkPackage() {
        return jdkPackage;
    }

    public void setJdkPackage(JdkPackage jdkPackage) {
        this.jdkPackage = jdkPackage;
    }
}
