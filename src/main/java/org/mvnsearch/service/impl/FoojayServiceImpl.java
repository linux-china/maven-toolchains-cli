package org.mvnsearch.service.impl;

import io.foojay.api.discoclient.DiscoClient;
import io.foojay.api.discoclient.pkg.*;
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
import org.mvnsearch.OsUtils;
import org.mvnsearch.service.FoojayService;
import org.mvnsearch.service.JdkDownloadLink;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;


@Service
public class FoojayServiceImpl implements FoojayService {
    private final DiscoClient discoClient = new DiscoClient();

    @Override
    public JdkDownloadLink findRelease(String version, String vendor) throws Exception {
        VersionNumber majorVersion;
        if (version.startsWith("1.8")) {
            majorVersion = new VersionNumber(8);
        } else if (version.contains(".")) {
            final String[] parts = version.split("\\.", 3);
            majorVersion = new VersionNumber(Integer.parseInt(parts[0]), Integer.valueOf(parts[1]));
        } else {
            majorVersion = new VersionNumber(Integer.valueOf(version));
        }
        final List<Distribution> distributions = List.of(new Distribution(null, null, vendor));
        OperatingSystem os;
        String osName = OsUtils.getOsName();
        ArchiveType archiveType = ArchiveType.TAR_GZ;
        if (osName.equals("mac")) {
            os = OperatingSystem.MACOS;
        } else if (osName.equals("windows")) {
            os = OperatingSystem.WINDOWS;
            archiveType = ArchiveType.ZIP;
        } else {
            os = OperatingSystem.LINUX;
        }
        Architecture arch = Architecture.X64;
        Bitness bitness = Bitness.BIT_64;
        String archName = OsUtils.getArchName();
        if (archName.equals("x32")) {
            arch = Architecture.X86;
            bitness = Bitness.BIT_32;
        } else if (archName.equals("aarch64")) {
            arch = Architecture.AARCH64;
        }
        final List<Pkg> pkgs = discoClient.getPkgs(
                distributions,
                majorVersion,
                Latest.OVERALL,
                os,
                LibCType.LIBC,
                arch,
                bitness,
                archiveType,
                PackageType.JDK,
                false,
                true,
                List.of(ReleaseStatus.GA),  //GA or EA
                TermOfSupport.NONE,  // LTS, MTS, STS
                List.of(Scope.DIRECTLY_DOWNLOADABLE),
                Match.ANY);
        if (pkgs != null && !pkgs.isEmpty()) {
            String pkgId = pkgs.get(0).getId();
            final Pkg pkg = discoClient.getPkg(pkgId);
            final String pkgDirectDownloadUri = discoClient.getPkgDirectDownloadUri(pkg.getEphemeralId());
            return new JdkDownloadLink(pkgDirectDownloadUri, pkg.getFileName());
        }
        return null;
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

    public File downloadAndExtract(String link, String fileName, String destDir) throws Exception {
        File destFile = new File(destDir, fileName);
        if (!destFile.exists()) {
            System.out.println("Begin to download: " + link);
            FileUtils.copyURLToFile(new URL(link), destFile);
        }
        System.out.println("Begin to extract: " + fileName);
        String extractDir = getRootNameInArchive(destFile);
        extractArchiveFile(destFile, new File(destDir));
        //noinspection ResultOfMethodCallIgnored
        destFile.delete();
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
