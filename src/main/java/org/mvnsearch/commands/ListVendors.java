package org.mvnsearch.commands;

import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * List JDK Vendors
 *
 * @author linux_china
 */
@Component
@CommandLine.Command(name = "vendors", mixinStandardHelpOptions = true, description = "List JDK Vendors")
public class ListVendors implements Callable<Integer> {

    @Override
    public Integer call() {
        List<String[]> table = new ArrayList<>();
        table.add(new String[]{"Full Name", "name", "JDK versions"});
        table.add(new String[]{"Adoptium", "temurin", "8, 11, 17"});
        table.add(new String[]{"AOJ", "aoj", "8, 9, 10, 11, 12, 13, 14, 15, 16"});
        table.add(new String[]{"AOJ OpenJ9", "aoj_openj9", "8, 9, 10, 11, 12, 13, 14, 15, 16"});
        table.add(new String[]{"Corretto", "corretto", "8, 11, 15, 16, 17"});
        table.add(new String[]{"Dragonwell", "dragonwell", "8, 11, 17"});
        table.add(new String[]{"Graal VM CE 8", "graalvm_ce8", "19.3, 20, 20.1, 20.2, 20.3, 21, 21.1, 21.2"});
        table.add(new String[]{"Graal VM CE 11", "graalvm_ce11", "19.3, 20, 20.1, 20.2, 20.3, 21, 21.1, 21.2, 21.3"});
        table.add(new String[]{"Graal VM CE 17", "graalvm_ce17", "21.3"});
        table.add(new String[]{"JetBrains", "jetbrains", "11, 17"});
        table.add(new String[]{"Liberica", "liberica", "8, 9, 10, 11, 12, 13, 14, 15, 16, 17"});
        table.add(new String[]{"Liberica Native", "liberica_native", "21.0, 21.1, 21.2"});
        table.add(new String[]{"Mandrel", "mandrel", "20.1, 20.2, 20.3, 21, 21.1, 21.2, 21.3"});
        table.add(new String[]{"Microsoft", "microsoft", "11, 16, 17"});
        table.add(new String[]{"OJDKBuild", "ojdk_build", "8, 9, 10, 11, 12, 13, 14, 15"});
        table.add(new String[]{"OpenLogic", "openlogic", "8, 11"});
        table.add(new String[]{"Oracle", "oracle", "17"});
        table.add(new String[]{"Oracle OpenJDK", "oracle_open_jdk", "8, 9, 10, 11, 12, 13, 14, 15, 16, 17"});
        table.add(new String[]{"Red Hat", "redhat", "8, 9, 10, 11, 12, 13, 14, 15"});
        table.add(new String[]{"SAP Machine", "sapmachine", "10, 11, 12, 13, 14, 15, 16, 17"});
        table.add(new String[]{"Semeru", "semeru", "8, 11, 16"});
        table.add(new String[]{"Trava", "trava", "8, 11"});
        table.add(new String[]{"Zulu", "zulu", "6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17"});
        FormatUtil.tableWithLines(table);
        return 0;
    }
}