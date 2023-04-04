package org.mvnsearch;

import org.mvnsearch.aot.ProjectRuntimeHints;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportRuntimeHints;

@SpringBootApplication(proxyBeanMethods = false)
@ImportRuntimeHints(ProjectRuntimeHints.class)
public class MavenToolchainsCli {
    public static void main(String[] args) {
        ConfigurableApplicationContext app = new SpringApplicationBuilder(MavenToolchainsCli.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
        System.exit(SpringApplication.exit(app));
    }
}
