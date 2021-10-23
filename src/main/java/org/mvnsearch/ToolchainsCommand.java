package org.mvnsearch;

import org.mvnsearch.commands.*;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Component
@Command(name = "mt", version = "0.3.0",
        mixinStandardHelpOptions = true,
        subcommands = {ListJDK.class, AddJDK.class, DeleteJDK.class, ListInstalledJDK.class, MavenToolchainsPlugin.class, ListVendors.class}
)
public class ToolchainsCommand implements Callable<Integer> {
    @Override
    public Integer call() {
        return new CommandLine(this).execute("--help");
    }

}
