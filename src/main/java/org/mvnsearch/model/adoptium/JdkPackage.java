package org.mvnsearch.model.adoptium;

public class JdkPackage {
    private String checksum;
    private String link;
    private String name;
    private int size;

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getInstallDir() {
        return name.substring(0,name.indexOf("."));
    }
}
