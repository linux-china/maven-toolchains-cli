package org.mvnsearch.service;


public class JdkDownloadLink {
    private String url;
    private String fileName;

    public JdkDownloadLink() {
    }

    public JdkDownloadLink(String url, String fileName) {
        this.url = url;
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
