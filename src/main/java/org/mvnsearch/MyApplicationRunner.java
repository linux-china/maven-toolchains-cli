package org.mvnsearch;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import picocli.CommandLine.IFactory;

@Component
public class MyApplicationRunner implements CommandLineRunner, ExitCodeGenerator {
    private final ToolchainsCommand toolchainsCommand;
    private final IFactory factory; // auto-configured to inject PicocliSpringFactory
    private int exitCode;

    public MyApplicationRunner(ToolchainsCommand toolchainsCommand, IFactory factory) {
        this.toolchainsCommand = toolchainsCommand;
        this.factory = factory;
    }

    @Override
    public void run(String... args) throws Exception {
        exitCode = new CommandLine(toolchainsCommand, factory).execute(args);
    }

    @Override
    public int getExitCode() {
        return exitCode;
    }
}