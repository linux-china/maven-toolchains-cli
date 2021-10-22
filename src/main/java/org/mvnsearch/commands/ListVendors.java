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
        table.add(new String[]{"Full Name", "name"});
        table.add(new String[]{"Adoptium", "temurin"});
        table.add(new String[]{"AOJ", "aoj"});
        table.add(new String[]{"AOJ OpenJ9", "corretto"});
        table.add(new String[]{"Corretto", "corretto"});
        table.add(new String[]{"Dragonwell", "dragonwell"});
        table.add(new String[]{"Graal VM CE 8", "graalvm_ce8"});
        table.add(new String[]{"Graal VM CE 11", "graalvm_ce11"});
        table.add(new String[]{"Graal VM CE 17", "graalvm_ce17"});
        table.add(new String[]{"Liberica", "liberica"});
        table.add(new String[]{"Liberica Native", "liberica_native"});
        table.add(new String[]{"Mandrel", "mandrel"});
        table.add(new String[]{"OJDKBuild", "ojdk_build"});
        table.add(new String[]{"Oracle", "oracle"});
        table.add(new String[]{"Oracle OpenJDK", "oracle_openjdk"});
        table.add(new String[]{"Red Hat", "redhat"});
        table.add(new String[]{"SAP Machine", "sapmachine"});
        table.add(new String[]{"Trava", "trava"});
        table.add(new String[]{"Zulu", "zulu"});
        FormatUtil.tableWithLines(table);
        return 0;
    }
}