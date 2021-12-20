package org.mvnsearch.commands;

import org.mvnsearch.model.Toolchain;
import org.mvnsearch.service.ToolchainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Sync JDKs from JBang
 *
 * @author linux_china
 */
@Component
@CommandLine.Command(name = "jbang", mixinStandardHelpOptions = true, description = "Sync JDKs from JBang")
public class SyncJbangJdks implements Callable<Integer>, BaseCommand {
    @Autowired
    private ToolchainService toolchainService;

    @Override
    public Integer call() {
        final List<String> jdkVersions = toolchainService.findAllToolchains().stream()
                .filter(toolchain -> toolchain.getType().equalsIgnoreCase("jdk"))
                .map(Toolchain::findVersion)
                .toList();
        File jbangHome = new File(System.getProperty("user.home"), ".jbang");
        if (jbangHome.exists()) {
            File jdksDir = new File(jbangHome, "cache/jdks");
            if (jdksDir.exists()) {
                final File[] jdkList = jdksDir.listFiles((dir, name) -> {
                    File jdkHome = new File(dir, name);
                    return jdkHome.isDirectory() && !jdkVersions.contains(name);
                });
                if (jdkList != null && jdkList.length > 0) {
                    for (File jdkHome : jdkList) {
                        toolchainService.addToolChain(jdkHome.getName(), null, jdkHome.getAbsolutePath());
                    }
                }
            }
        }
        return 0;
    }


}