package org.mvnsearch;

import org.mvnsearch.commands.AddJDK;
import org.mvnsearch.commands.DeleteJDK;
import org.mvnsearch.commands.ListInstalledJDK;
import org.mvnsearch.commands.ListJDK;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Component
@Command(name = "mt", version = "0.1.0",
        mixinStandardHelpOptions = true,
        subcommands = {ListJDK.class, AddJDK.class, DeleteJDK.class, ListInstalledJDK.class}
)
public class ToolchainsCommand implements Callable<Integer> {
    @Override
    public Integer call() {
        return new CommandLine(this).execute("--help");
    }

}
