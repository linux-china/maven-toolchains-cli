package org.mvnsearch;

import org.mvnsearch.commands.ListVendors;
import org.mvnsearch.model.Toolchain;
import org.mvnsearch.model.Toolchains;
import org.mvnsearch.model.adoptium.JdkBinary;
import org.mvnsearch.model.adoptium.JdkPackage;
import org.mvnsearch.model.adoptium.JdkRelease;
import org.mvnsearch.model.adoptium.JdkVersion;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.nativex.hint.TypeHint;

@SpringBootApplication(proxyBeanMethods = false)
@TypeHint(types = {Toolchains.class, Toolchain.class,
        JdkRelease.class, JdkBinary.class, JdkPackage.class, JdkVersion.class, ListVendors.class})
public class MavenToolchainsCli {
    public static void main(String[] args) {
        ConfigurableApplicationContext app = new SpringApplicationBuilder(MavenToolchainsCli.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
        System.exit(SpringApplication.exit(app));
    }
}
