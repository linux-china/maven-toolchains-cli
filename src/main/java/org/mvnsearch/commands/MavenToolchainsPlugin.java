package org.mvnsearch.commands;

import org.mvnsearch.service.ToolchainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.util.concurrent.Callable;

/**
 * xml code for maven-toolchains-plugin
 *
 * @author linux_china
 */
@Component
@CommandLine.Command(name = "plugin", mixinStandardHelpOptions = true, description = "Print maven-toolchains-plugin setting for pom.xml")
public class MavenToolchainsPlugin implements Callable<Integer> {
    @CommandLine.Option(names = "--vendor", description = "Vendor name")
    private String vendor;
    @CommandLine.Parameters(index = "0", description = "The file whose checksum to calculate.")
    private String version;
    @Autowired
    private ToolchainService toolchainService;

    @Override
    public Integer call() throws Exception {
        String pluginXml;
        if (vendor != null) {
            //language=xml
            pluginXml = """
                    <plugin>
                        <groupId>org.mvnsearch</groupId>
                        <artifactId>toolchains-maven-plugin</artifactId>
                        <version>4.4.0</version>
                        <executions>
                            <execution>
                                <goals>
                                    <goal>toolchain</goal>
                                </goals>
                            </execution>
                        </executions>
                        <configuration>
                            <toolchains>
                                <jdk>
                                    <version>%s</version>
                                    <vendor>%s</vendor>
                                </jdk>
                            </toolchains>
                        </configuration>
                    </plugin>
                    """.formatted(version, vendor);
        } else {
            //language=xml
            pluginXml = """
                    <plugin>
                        <groupId>org.mvnsearch</groupId>
                        <artifactId>toolchains-maven-plugin</artifactId>
                        <version>4.4.0</version>
                        <executions>
                            <execution>
                                <goals>
                                    <goal>toolchain</goal>
                                </goals>
                            </execution>
                        </executions>
                        <configuration>
                            <toolchains>
                                <jdk>
                                    <version>%s</version>
                                </jdk>
                            </toolchains>
                        </configuration>
                    </plugin>
                    """.formatted(version);
        }
        System.out.print(pluginXml);
        return 0;
    }
}
