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
            pluginXml = "<profiles>\n" +
                    "    <profile>\n" +
                    "        <id>toolchains</id>\n" +
                    "        <activation>\n" +
                    "            <file>\n" +
                    "                <exists>${env.HOME}/.m2/toolchains.xml</exists>\n" +
                    "            </file>\n" +
                    "        </activation>\n" +
                    "        <build>\n" +
                    "            <plugins>\n" +
                    "                <plugin>\n" +
                    "                    <groupId>org.apache.maven.plugins</groupId>\n" +
                    "                    <artifactId>maven-toolchains-plugin</artifactId>\n" +
                    "                    <version>3.0.0</version>\n" +
                    "                    <executions>\n" +
                    "                        <execution>\n" +
                    "                            <goals>\n" +
                    "                                <goal>toolchain</goal>\n" +
                    "                            </goals>\n" +
                    "                        </execution>\n" +
                    "                    </executions>\n" +
                    "                    <configuration>\n" +
                    "                        <toolchains>\n" +
                    "                            <jdk>\n" +
                    "                                <version>" + version + "</version>\n" +
                    "                                <version>" + vendor + "</version>\n" +
                    "                            </jdk>\n" +
                    "                        </toolchains>\n" +
                    "                    </configuration>\n" +
                    "                </plugin>\n" +
                    "            </plugins>\n" +
                    "        </build>\n" +
                    "    </profile>\n" +
                    "</profiles>";
        } else {
            //language=xml
            pluginXml = "<profiles>\n" +
                    "    <profile>\n" +
                    "        <id>toolchains</id>\n" +
                    "        <activation>\n" +
                    "            <file>\n" +
                    "                <exists>${env.HOME}/.m2/toolchains.xml</exists>\n" +
                    "            </file>\n" +
                    "        </activation>\n" +
                    "        <build>\n" +
                    "            <plugins>\n" +
                    "                <plugin>\n" +
                    "                    <groupId>org.apache.maven.plugins</groupId>\n" +
                    "                    <artifactId>maven-toolchains-plugin</artifactId>\n" +
                    "                    <version>3.0.0</version>\n" +
                    "                    <executions>\n" +
                    "                        <execution>\n" +
                    "                            <goals>\n" +
                    "                                <goal>toolchain</goal>\n" +
                    "                            </goals>\n" +
                    "                        </execution>\n" +
                    "                    </executions>\n" +
                    "                    <configuration>\n" +
                    "                        <toolchains>\n" +
                    "                            <jdk>\n" +
                    "                                <version>" + version + "</version>\n" +
                    "                            </jdk>\n" +
                    "                        </toolchains>\n" +
                    "                    </configuration>\n" +
                    "                </plugin>\n" +
                    "            </plugins>\n" +
                    "        </build>\n" +
                    "    </profile>\n" +
                    "</profiles>";
        }
        System.out.println(pluginXml);
        return 0;
    }
}
