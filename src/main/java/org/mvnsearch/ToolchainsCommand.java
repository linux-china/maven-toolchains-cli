package org.mvnsearch;

import org.springframework.stereotype.Component;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

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
        @Override
        public Integer call() {
            return 0;
        }
    }

    @Component
    @Command(name = "delete", mixinStandardHelpOptions = true, description = "Delete JDK")
    static class DeleteJDK implements Callable<Integer> {
        @Option(names = "--vendor", description = "Vendor name")
        private String vendor;

        @Override
        public Integer call() {
            System.out.println("mycommand sub ce.service");
            return 43;
        }
    }

    @Component
    @Command(name = "add", mixinStandardHelpOptions = true, description = "Add JDK")
    static class AddJDK implements Callable<Integer> {
        @Option(names = "--vendor", description = "Vendor")
        private String vendor;

        @Override
        public Integer call() {
            System.out.println("mycommand sub ce.service");
            return 43;
        }
    }
}
