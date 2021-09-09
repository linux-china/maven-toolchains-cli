package org.mvnsearch.commands;

import org.mvnsearch.service.ToolchainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.util.concurrent.Callable;

/**
 * Delete jdk in toolchains.xml
 *
 * @author linux_china
 */
@Component
@CommandLine.Command(name = "delete", mixinStandardHelpOptions = true, description = "Delete JDK")
public class DeleteJDK implements Callable<Integer> {
    @CommandLine.Option(names = "--vendor", description = "Vendor name")
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