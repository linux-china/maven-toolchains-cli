package org.mvnsearch.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.mvnsearch.model.jdk.JdkRelease;
import org.mvnsearch.service.AdoptOpenJDKService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

@Service
public class AdoptOpenJDKServiceImpl implements AdoptOpenJDKService {
    private static ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Override
    public JdkRelease[] findReleases(String version) throws Exception {
        String majorVersion = version;
        if (majorVersion.startsWith("1.8")) {
            majorVersion = "8";
        }
        if (majorVersion.contains(".")) {
            majorVersion = majorVersion.substring(0, majorVersion.indexOf("."));
        }
        URL link = new URL("https://api.adoptopenjdk.net/v3/assets/latest/" + majorVersion + "/hotspot?release=latest&jvm_impl=hotspot&vendor=adoptopenjdk");
        return objectMapper.readValue(link, JdkRelease[].class);
    }

    @Override
    public File downloadAndExtract(String link, String fileName, String destDir) throws Exception {
        File destFile = new File(destDir, fileName);
        if (!destFile.exists()) {
            System.out.println("Begin to download: " + link);
            FileUtils.copyURLToFile(new URL(link), destFile);
        }
        String extractDir = getRootNameInArchive(destFile);
        File jdkDir = new File(destDir, extractDir);
        if (!jdkDir.exists()) {
            if (fileName.endsWith(".tgz") || fileName.endsWith(".tar.gz")) {
                new ProcessBuilder().directory(new File(destDir)).command("tar", "zxpf", fileName).start();
            } else {
                new ProcessBuilder().directory(new File(destDir)).command("unzip", fileName).start();
            }
            // decompress(destFile, new File(destDir));
        }
        return new File(destDir, extractDir);
    }

    public String getRootNameInArchive(File archiveFile) throws Exception {
        ArchiveInputStream archiveInputStream;
        if (archiveFile.getName().endsWith("tar.gz") || archiveFile.getName().endsWith("tgz")) {
            archiveInputStream = new TarArchiveInputStream(new GzipCompressorInputStream(new FileInputStream(archiveFile)));
        } else {
            archiveInputStream = new ZipArchiveInputStream((new FileInputStream(archiveFile)));
        }
        String name = archiveInputStream.getNextEntry().getName();
        archiveInputStream.close();
        return name.replace("/", "");
    }

    //with file permission reserved
    public void decompress(File in, File out) throws IOException {
        try (TarArchiveInputStream fin = new TarArchiveInputStream(new GzipCompressorInputStream(new FileInputStream(in)))) {
            TarArchiveEntry entry;
            while ((entry = fin.getNextTarEntry()) != null) {
                if (entry.isDirectory()) {
                    continue;
                }
                File curFile = new File(out, entry.getName());
                if (entry.getName().endsWith("javac")) {
                    System.out.println("javac mode: " + entry.getMode());
                    curFile.setExecutable(true);
                }
                File parent = curFile.getParentFile();
                if (!parent.exists()) {
                    parent.mkdirs();
                }
                IOUtils.copy(fin, new FileOutputStream(curFile));
            }
        }
    }
}
