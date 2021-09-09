package org.mvnsearch.commands;

import org.springframework.stereotype.Component;
import org.zeroturnaround.exec.ProcessExecutor;
import picocli.CommandLine;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

/**
 * List all installed JDK on host
 *
 * @author linux_china
 */
@Component
@CommandLine.Command(name = "all", mixinStandardHelpOptions = true, description = "List all installed JDK")
public class ListInstalledJDK implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        final String userHome = System.getProperty("user.home");
        List<File> installedDirs = Arrays.asList(
                new File("/Library/Java/JavaVirtualMachines"),
                new File(userHome, "Library/Java/JavaVirtualMachines"),
                new File(userHome, ".sdkman/candidates/java/"),
                new File(userHome, ".gradle/jdks"),
                new File(userHome, ".jenv/candidates/java"),
                new File(userHome, "..m2/jdks")
        );
        for (File dest : installedDirs) {
            if (dest.exists() && dest.listFiles() != null) {
                Stream.of(Objects.requireNonNull(dest.listFiles()))
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
                        .forEach(javaHome -> {
                            try {
                                File javaBin = new File(javaHome, "bin/java");
                                String javaVersionText = new ProcessExecutor().command(javaBin.getAbsolutePath(), "-version")
                                        .readOutput(true).execute().outputUTF8().split("\n")[0];
                                final String[] parts = javaVersionText.split("[\\s\"]+");
                                String vendor = parts[0];
                                String version = parts[2];
                                System.out.println(String.format("%20s", vendor + " " + version) + " : " + javaHome);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
            }
        }
        return 0;
    }
}
