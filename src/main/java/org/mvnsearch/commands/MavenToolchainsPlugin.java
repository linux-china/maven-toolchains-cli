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
@CommandLine.Command(name = "plugin", mixinStandardHelpOptions = true, description = "Xml code for maven-toolchains-plugin")
public class MavenToolchainsPlugin implements Callable<Integer> {
    @CommandLine.Option(names = "--vendor", description = "Vendor name")
    private String vendor;
    @CommandLine.Parameters(index = "0", description = "The file whose checksum to calculate.")
    private String version;
    @Autowired
    private ToolchainService toolchainService;

    @Override
    public Integer call() throws Exception {
        String pluginXml = null;
        if (vendor != null) {
            //language=xml
            pluginXml = "<plugin>\n" +
                    "    <groupId>org.apache.maven.plugins</groupId>\n" +
                    "    <artifactId>maven-toolchains-plugin</artifactId>\n" +
                    "    <version>3.0.0</version>\n" +
                    "    <executions>\n" +
                    "        <execution>\n" +
                    "            <goals>\n" +
                    "                <goal>toolchain</goal>\n" +
                    "            </goals>\n" +
                    "        </execution>\n" +
                    "    </executions>\n" +
                    "    <configuration>\n" +
                    "        <toolchains>\n" +
                    "            <jdk>\n" +
                    "                <version>" + version + "</version>\n" +
                    "                <vendor>" + vendor + "</vendor>\n" +
                    "            </jdk>\n" +
                    "        </toolchains>\n" +
                    "    </configuration>\n" +
                    "</plugin>";
        } else {
            //language=xml
            pluginXml = "<plugin>\n" +
                    "    <groupId>org.apache.maven.plugins</groupId>\n" +
                    "    <artifactId>maven-toolchains-plugin</artifactId>\n" +
                    "    <version>3.0.0</version>\n" +
                    "    <executions>\n" +
                    "        <execution>\n" +
                    "            <goals>\n" +
                    "                <goal>toolchain</goal>\n" +
                    "            </goals>\n" +
                    "        </execution>\n" +
                    "    </executions>\n" +
                    "    <configuration>\n" +
                    "        <toolchains>\n" +
                    "            <jdk>\n" +
                    "                <version>" + version + "</version>\n" +
                    "            </jdk>\n" +
                    "        </toolchains>\n" +
                    "    </configuration>\n" +
                    "</plugin>";
        }
        System.out.println(pluginXml);
        return 0;
    }
}
