package org.mvnsearch.commands;

import org.springframework.stereotype.Component;
import org.zeroturnaround.exec.ProcessExecutor;
import picocli.CommandLine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mvnsearch.commands.FormatUtil.tableWithLines;

/**
 * List all installed JDK on host
 *
 * @author linux_china
 */
@Component
@CommandLine.Command(name = "all", mixinStandardHelpOptions = true, description = "List all installed JDK in your laptop")
public class ListInstalledJDK implements Callable<Integer>, BaseCommand {
    @Override
    public Integer call() throws Exception {
        final String userHome = System.getProperty("user.home");
        List<File> installedDirs = new ArrayList<>();
        // jdk installed by third
        installedDirs.add(new File(userHome, ".sdkman/candidates/java/"));
        installedDirs.add(new File(userHome, ".gradle/jdks"));
        installedDirs.add(new File(userHome, ".jenv/candidates/java"));
        installedDirs.add(new File(userHome, ".m2/jdks"));
        installedDirs.add(new File(userHome, ".jbang/cache/jdks"));
        // os related directories
        String os = getOsName();
        if (os.equals("mac")) {
            installedDirs.add(new File("/Library/Java/JavaVirtualMachines"));
            installedDirs.add(new File(userHome, "Library/Java/JavaVirtualMachines"));
        } else if (os.equals("windows")) {
            installedDirs.add(new File("C:\\Program Files\\Java\\"));
        } else {
            installedDirs.add(new File("/usr/lib/jvm"));
        }
        List<String[]> table = new ArrayList<>();
        table.add(new String[]{"Vendor", "Version", "Path"});
        System.out.println("Scanning the JDK...");
        for (File dest : installedDirs) {
            if (dest.exists() && dest.listFiles() != null) {
                final List<String[]> rows = Stream.of(Objects.requireNonNull(dest.listFiles()))
                        .filter(File::isDirectory)
                        .filter(dir -> {
                            final File javaBin1 = new File(dir, "bin/java");
                            final File javaBin2 = new File(dir, "Contents/Home/bin/java");
                            return javaBin1.exists() || javaBin2.exists();
                        })
                        .map(javaHome -> {
                            final File javaHome2 = new File(javaHome, "Contents/Home");
                            return javaHome2.exists() ? javaHome2 : javaHome;
                        })
                        .map(javaHome -> {
                            try {
                                File javaBin = new File(javaHome, "bin/java");
                                String javaVersionText = new ProcessExecutor().command(javaBin.getAbsolutePath(), "-version")
                                        .readOutput(true).execute().outputUTF8().split("\n")[0];
                                final String[] parts = javaVersionText.split("[\\s\"]+");
                                String vendor = parts[0];
                                String version = parts[2];
                                if (vendor.equalsIgnoreCase("java")) {
                                    vendor = "oracle";
                                }
                                return new String[]{vendor, version, javaHome.getAbsolutePath()};
                            } catch (Exception e) {
                                return new String[]{"error", "error", FormatUtil.ERROR + javaHome.getAbsolutePath()};
                            }
                        }).collect(Collectors.toList());
                table.addAll(rows);
            }
        }
        tableWithLines(table);
        return 0;
    }

}
