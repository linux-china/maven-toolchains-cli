package org.mvnsearch;

import org.mvnsearch.model.Toolchain;
import org.mvnsearch.model.jdk.JdkBinary;
import org.mvnsearch.model.jdk.JdkPackage;
import org.mvnsearch.model.jdk.JdkRelease;
import org.mvnsearch.service.AdoptOpenJDKService;
import org.mvnsearch.service.ToolchainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.util.concurrent.Callable;

@Component
@Command(name = "mt", mixinStandardHelpOptions = true,
        subcommands = {ToolchainsCommand.ListJDK.class, ToolchainsCommand.AddJDK.class, ToolchainsCommand.DeleteJDK.class,})
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
                        System.out.printf("%3s: %s%n", toolchain.findVersion(), toolchain.findJdkHome());
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
                    JdkRelease[] releases = adoptOpenJDKService.findReleases(version);
                    for (JdkRelease release : releases) {
                        JdkBinary binary = release.getBinary();
                        if (binary.getImageType().equals("jdk") && os.contains(binary.getOs()) && arch.contains(binary.getArchitecture())) {
                            JdkPackage jdkPackage = binary.getJdkPackage();
                            String link = jdkPackage.getLink();
                            File jdksDir = new File(System.getProperty("user.home") + "/.m2/jdks");
                            File jdkHome = adoptOpenJDKService.downloadAndExtract(link, jdkPackage.getName(), jdksDir.getAbsolutePath());
                            if (new File(jdkHome, "Contents").exists()) {  // mac tgz
                                jdkHome = new File(jdkHome, "Contents/Home");
                            }
                            toolchainService.addToolChain(version, vendor, jdkHome.getAbsolutePath());
                            System.out.println("Succeed to install JDK on " + jdkHome.getAbsolutePath());
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Failed to fetch information from AdoptOpenJDK!");
                }
            } else {
                System.out.println("Added already!");
            }
            return 0;
        }
    }
}
