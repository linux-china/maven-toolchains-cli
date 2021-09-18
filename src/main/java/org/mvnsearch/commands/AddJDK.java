package org.mvnsearch.commands;

import org.jetbrains.annotations.Nullable;
import org.mvnsearch.model.Toolchain;
import org.mvnsearch.model.jdk.JdkBinary;
import org.mvnsearch.model.jdk.JdkPackage;
import org.mvnsearch.model.jdk.JdkRelease;
import org.mvnsearch.service.AdoptOpenJDKService;
import org.mvnsearch.service.ToolchainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.io.File;
import java.util.concurrent.Callable;

/**
 * Add JDK to toolchains.xml
 *
 * @author linux_china
 */
@Component
@CommandLine.Command(name = "add", mixinStandardHelpOptions = true, description = "Add JDK")
public class AddJDK implements Callable<Integer>, BaseCommand {
    @CommandLine.Option(names = "--vendor", description = "Java Vendor name: openjdk(default), graalvm")
    private String vendor;
    @CommandLine.Parameters(index = "0", description = "Java version: 8, 11, 17")
    private String version;
    @CommandLine.Parameters(index = "1", arity = "0..1", description = "Local Java Home with absolute path")
    private String javaHome;
    @Autowired
    private AdoptOpenJDKService adoptOpenJDKService;
    @Autowired
    private ToolchainService toolchainService;

    @Override
    public Integer call() {
        Toolchain toolchain = toolchainService.findToolchain(version, vendor);
        if (toolchain == null) {
            String arch = getArchName();
            // link local jdk to toolchains.xml
            if (javaHome != null) {
                // check home for Mac
                if (new File(javaHome, "Contents/Home").exists()) {
                    javaHome = new File(javaHome, "Contents/Home").getAbsolutePath();
                }
                return installFromLocal();
            }
            try {
                String os = getOsName();
                String[] download;
                if ("graalvm".equalsIgnoreCase(vendor)) {
                    download = getGraalVMDownload(version, os);
                } else {
                    if (version.startsWith("17")) {
                        download = getOracleJDKDownload(version, os, arch);
                    } else {
                        download = getAdoptJDKDownload(version, os, arch);
                    }
                }
                if (download != null) {
                    String link = download[0];
                    String fileName = download[1];
                    File jdksDir = new File(System.getProperty("user.home") + "/.m2/jdks");
                    File jdkHome = adoptOpenJDKService.downloadAndExtract(link, fileName, jdksDir.getAbsolutePath());
                    if (new File(jdkHome, "Contents/Home").exists()) {  // mac tgz
                        jdkHome = new File(jdkHome, "Contents/Home");
                    }
                    toolchainService.addToolChain(version, vendor, jdkHome.getAbsolutePath());
                    System.out.println("Succeed to add JDK " + version + " in toolchains.xml");
                } else {
                    System.out.println("JDK not found: " + version);
                }
            } catch (Exception e) {
                System.out.println("Failed to fetch information from AdoptOpenJDK!");
            }
        } else {
            System.out.println("Added already!");
        }
        return 0;
    }


    private int installFromLocal() {
        File javaBin = new File(javaHome, "bin/java");
        if (javaBin.exists()) {
            toolchainService.addToolChain(version, vendor, javaHome);
            System.out.println("Succeed to add JDK " + version + " in toolchains.xml");
            return 0;
        } else {
            System.out.println("Java Home is not correct: " + javaHome);
            return 1;
        }
    }

    @Nullable
    private String[] getAdoptJDKDownload(String version, String os, String arch) throws Exception {
        JdkRelease[] releases = adoptOpenJDKService.findReleases(version);
        for (JdkRelease release : releases) {
            JdkBinary binary = release.getBinary();
            if (binary.getImageType().equals("jdk") && os.contains(binary.getOs()) && arch.contains(binary.getArchitecture())) {
                JdkPackage jdkPackage = binary.getJdkPackage();
                String link = jdkPackage.getLink();
                return new String[]{link, jdkPackage.getName()};
            }
        }
        return null;
    }

    private String[] getOracleJDKDownload(String version, String os, String arch) {
        String fileName = null;
        if (os.equals("windows")) {
            fileName = "jdk-17_windows-" + arch + "_bin.zip";
        } else if (os.equals("mac")) {
            fileName = "jdk-17_macos-" + arch + "_bin.tar.gz";
        } else {
            fileName = "jdk-17_linux-" + arch + "_bin.tar.gz";
        }
        return new String[]{"https://download.oracle.com/java/17/latest/" + fileName, fileName};
    }

    @Nullable
    private String[] getGraalVMDownload(String version, String os) {
        String graalVersion = "21.2.0";
        if (version.startsWith("11")) {
            version = "11";
        } else if (version.startsWith("8") || version.startsWith("1.8")) {
            version = "8";
        } else {
            return null;
        }
        String extension = os.equals("windows") ? "zip" : "tar.gz";
        return new String[]{String.format("https://github.com/graalvm/graalvm-ce-builds/releases/download/vm-%s/graalvm-ce-java%s-%s-amd64-%s.%s",
                graalVersion, version, os, graalVersion, extension),
                String.format("graalvm-ce-java%s-%s-amd64-%s.%s", graalVersion, os, graalVersion, extension)};
    }
}