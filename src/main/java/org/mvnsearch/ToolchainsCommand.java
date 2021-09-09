package org.mvnsearch;

import org.jetbrains.annotations.Nullable;
import org.mvnsearch.model.Toolchain;
import org.mvnsearch.model.jdk.JdkBinary;
import org.mvnsearch.model.jdk.JdkPackage;
import org.mvnsearch.model.jdk.JdkRelease;
import org.mvnsearch.service.AdoptOpenJDKService;
import org.mvnsearch.service.ToolchainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.util.concurrent.Callable;

@Component
@Command(name = "mt", version = "0.1.0",
        mixinStandardHelpOptions = true,
        subcommands = {ToolchainsCommand.ListJDK.class,
                ToolchainsCommand.AddJDK.class,
                ToolchainsCommand.DeleteJDK.class}
)
public class ToolchainsCommand implements Callable<Integer> {
    @Override
    public Integer call() {
        return new CommandLine(this).execute("--help");
    }

    @Component
    @Command(name = "list", mixinStandardHelpOptions = true, description = "List JDK")
    static class ListJDK implements Callable<Integer> {
        @Autowired
        private ToolchainService toolchainService;

        @Override
        public Integer call() {
            toolchainService.findAllToolchains().stream()
                    .filter(toolchain -> toolchain.getType().equalsIgnoreCase("jdk"))
                    .forEach(toolchain -> {
                        String jdkHome = toolchain.findJdkHome();
                        if (new File(jdkHome).exists()) {
                            System.out.printf("%3s: %s%n", toolchain.findVersion(), jdkHome);
                        } else {
                            String output = String.format("%3s: %s", toolchain.findVersion(), jdkHome);
                            System.out.println(AnsiOutput.toString(AnsiColor.RED, output));
                        }
                    });
            return 0;
        }
    }

    @Component
    @Command(name = "delete", mixinStandardHelpOptions = true, description = "Delete JDK")
    static class DeleteJDK implements Callable<Integer> {
        @Option(names = "--vendor", description = "Vendor name")
        private String vendor;
        @CommandLine.Parameters(index = "0", description = "The file whose checksum to calculate.")
        private String version;
        @Autowired
        private ToolchainService toolchainService;

        @Override
        public Integer call() {
            toolchainService.deleteToolChain(version, vendor);
            return 0;
        }
    }

    @Component
    @Command(name = "add", mixinStandardHelpOptions = true, description = "Add JDK")
    static class AddJDK implements Callable<Integer> {
        @Option(names = "--vendor", description = "Vendor")
        private String vendor;
        @CommandLine.Parameters(index = "0", description = "The file whose checksum to calculate.")
        private String version;
        @Autowired
        private AdoptOpenJDKService adoptOpenJDKService;
        @Autowired
        private ToolchainService toolchainService;

        @Override
        public Integer call() {
            Toolchain toolchain = toolchainService.findToolchain(version, vendor);
            if (toolchain == null) {
                String arch = System.getProperty("os.arch");
                if (arch.equals("x86_64") || arch.equals("amd64")) {
                    arch = "x64";
                } else if (arch.equals("x86_32") || arch.equals("amd32")) {
                    arch = "x32";
                }
                String os = System.getProperty("os.name").toLowerCase();
                try {
                    String[] download;
                    if ("graalvm".equals(vendor)) {
                        download = getGraalVMDownload(version, os);
                    } else {
                        download = getAdoptJDKDownload(version, os, arch);
                    }
                    if (download != null) {
                        String link = download[0];
                        String fileName = download[1];
                        File jdksDir = new File(System.getProperty("user.home") + "/.m2/jdks");
                        File jdkHome = adoptOpenJDKService.downloadAndExtract(link, fileName, jdksDir.getAbsolutePath());
                        if (new File(jdkHome, "Contents").exists()) {  // mac tgz
                            jdkHome = new File(jdkHome, "Contents/Home");
                        }
                        toolchainService.addToolChain(version, vendor, jdkHome.getAbsolutePath());
                        System.out.println("Succeed to install JDK on " + jdkHome.getAbsolutePath());
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

        @Nullable
        private String[] getGraalVMDownload(String version, String os) {
            String graalVersion = "21.0.0.2";
            if (version.startsWith("11")) {
                version = "11";
            } else if (version.startsWith("8") || version.startsWith("1.8")) {
                version = "8";
            } else {
                return null;
            }
            if (os.contains("mac") || os.contains("darwin")) {
                os = "darwin";
            } else if (os.contains("windows")) {
                os = "windows";
            } else {
                os = "linux";
            }
            String extension = os.equals("windows") ? "zip" : "tar.gz";
            return new String[]{String.format("https://github.com/graalvm/graalvm-ce-builds/releases/download/vm-%s/graalvm-ce-java%s-%s-amd64-%s.%s",
                    graalVersion, version, os, graalVersion, extension),
                    String.format("graalvm-ce-java%s-%s-amd64-%s.%s", graalVersion, os, graalVersion, extension)};
        }
    }
}
