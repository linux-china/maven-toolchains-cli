package org.mvnsearch.commands;

import org.mvnsearch.model.Toolchain;
import org.mvnsearch.service.*;
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
    @CommandLine.Option(names = "--vendor", description = "Java Vendor name: temurin(default), graalvm")
    private String vendor = "temurin";
    @CommandLine.Parameters(index = "0", description = "Java version: 8, 11, 17")
    private String version;
    @CommandLine.Parameters(index = "1", arity = "0..1", description = "Local Java Home with absolute path")
    private String javaHome;
    @Autowired
    private AdoptiumService adoptiumService;
    private FoojayService foojayService;
    @Autowired
    private GraalVMService graalVMService;
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
                JdkDownloadLink download;
                if (vendor.contains("graalvm")) {
                    download = graalVMService.findRelease(version, vendor);
                } else if (vendor.equals("temurin")) {
                    download = adoptiumService.findRelease(version);
                } else {
                    download = foojayService.findRelease(version, vendor);
                }
                if (download != null) {
                    File jdksDir = new File(System.getProperty("user.home") + "/.m2/jdks");
                    File jdkHome = adoptiumService.downloadAndExtract(download.getUrl(), download.getFileName(), jdksDir.getAbsolutePath());
                    if (new File(jdkHome, "Contents/Home").exists()) {  // mac tgz
                        jdkHome = new File(jdkHome, "Contents/Home");
                    }
                    toolchainService.addToolChain(version, vendor, jdkHome.getAbsolutePath());
                    System.out.println("Succeed to add JDK " + version + " in toolchains.xml");
                } else {
                    System.out.println("JDK not found: " + version);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Failed to fetch information from Adoptium https://adoptium.net/!");
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

}