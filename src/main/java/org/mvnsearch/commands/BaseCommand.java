package org.mvnsearch.commands;

/**
 * Base command
 *
 * @author linux_china
 */
public interface BaseCommand {
    default String getOsName() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("mac")) {
            return "mac";
        } else if (os.contains("windows")) {
            return "windows";
        } else {
            return "linux";
        }
    }

    default String getArchName() {
        String arch = System.getProperty("os.arch").toLowerCase();
        if (arch.contains("x86_32") || arch.contains("amd32")) {
            arch = "x32";
        } else if (arch.contains("aarch64") || arch.contains("arm64")) {
            arch = "aarch64";
        } else {
            arch = "x64";
        }
        return arch;
    }
}
