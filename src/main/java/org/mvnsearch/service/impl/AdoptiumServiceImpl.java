package org.mvnsearch.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.io.FileUtils;
import org.codehaus.plexus.archiver.AbstractUnArchiver;
import org.codehaus.plexus.archiver.tar.TarGZipUnArchiver;
import org.codehaus.plexus.archiver.zip.ZipUnArchiver;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.mvnsearch.model.adoptium.JdkRelease;
import org.mvnsearch.service.AdoptiumService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

@Service
public class AdoptiumServiceImpl implements AdoptiumService {
    private static final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Override
    public JdkRelease[] findReleases(String version) throws Exception {
        String majorVersion = version;
        if (majorVersion.startsWith("1.8")) {
            majorVersion = "8";
        }
        if (majorVersion.contains(".")) {
            majorVersion = majorVersion.substring(0, majorVersion.indexOf("."));
        }
        URL link = new URL("https://api.adoptium.net/v3/assets/latest/" + majorVersion + "/hotspot?release=latest&jvm_impl=hotspot&vendor=adoptium");
        return objectMapper.readValue(link, JdkRelease[].class);
    }

    public String getRootNameInArchive(File archiveFile) throws Exception {
        ArchiveInputStream archiveInputStream;
        if (archiveFile.getName().endsWith("tar.gz") || archiveFile.getName().endsWith("tgz")) {
            archiveInputStream = new TarArchiveInputStream(new GzipCompressorInputStream(new FileInputStream(archiveFile)));
        } else {
            archiveInputStream = new ZipArchiveInputStream((new FileInputStream(archiveFile)));
        }
        String name = archiveInputStream.getNextEntry().getName();
        while (name.startsWith(".") && name.length() < 4) {  // fix '.._' bug
            name = archiveInputStream.getNextEntry().getName();
        }
        archiveInputStream.close();
        if (name.startsWith("./")) {
            name = name.substring(2);
        }
        if (name.contains("/")) {
            name = name.substring(0, name.indexOf("/"));
        }
        return name;
    }

    @Override
    public File downloadAndExtract(String link, String fileName, String destDir) throws Exception {
        File destFile = new File(destDir, fileName);
        if (!destFile.exists()) {
            System.out.println("Begin to download: " + link);
            FileUtils.copyURLToFile(new URL(link), destFile);
        }
        System.out.println("Begin to extract: " + fileName);
        String extractDir = getRootNameInArchive(destFile);
        extractArchiveFile(destFile, new File(destDir));
        return new File(destDir, extractDir);
    }

    public void extractArchiveFile(File sourceFile, File destDir) throws IOException {
        String fileName = sourceFile.getName();
        final AbstractUnArchiver unArchiver;
        if (fileName.endsWith(".tgz") || fileName.endsWith(".tar.gz")) {
            unArchiver = new TarGZipUnArchiver();
        } else {
            unArchiver = new ZipUnArchiver();
        }
        unArchiver.enableLogging(new ConsoleLogger(Logger.LEVEL_ERROR, "console"));
        unArchiver.setSourceFile(sourceFile);
        unArchiver.setDestDirectory(destDir);
        unArchiver.setOverwrite(true);
        unArchiver.extract();
    }
}
