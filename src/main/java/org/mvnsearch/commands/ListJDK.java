package org.mvnsearch.commands;

import org.mvnsearch.service.ToolchainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

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
        List<String[]> table = new ArrayList<>();
        table.add(new String[]{"Vendor", "Version", "Path"});
        final List<String[]> jdkList = toolchainService.findAllToolchains().stream()
                .filter(toolchain -> toolchain.getType().equalsIgnoreCase("jdk"))
                .map(toolchain -> {
                    String jdkHome = toolchain.findJdkHome();
                    String vendor = toolchain.findVendor();
                    if (vendor == null) {
                        vendor = "";
                    }
                    String version = toolchain.findVersion();
                    if (new File(jdkHome).exists()) {
                        return new String[]{vendor, version, jdkHome};
                    } else {
                        return new String[]{vendor, version, FormatUtil.ERROR + jdkHome};
                    }
                }).collect(Collectors.toList());
        table.addAll(jdkList);
        FormatUtil.tableWithLines(table);
        return 0;
    }
}