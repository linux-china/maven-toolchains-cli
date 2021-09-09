package org.mvnsearch.commands;

import org.mvnsearch.service.ToolchainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.io.File;
import java.util.concurrent.Callable;

/**
 * List JDK in toolchains.xml
 *
 * @author linux_china
 */
@Component
@CommandLine.Command(name = "list", mixinStandardHelpOptions = true, description = "List JDK")
public class ListJDK implements Callable<Integer> {
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