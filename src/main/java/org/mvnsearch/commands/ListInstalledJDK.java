package org.mvnsearch.commands;

import org.springframework.stereotype.Component;
import org.zeroturnaround.exec.ProcessExecutor;
import picocli.CommandLine;

import java.io.File;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * List all installed JDK on host
 *
 * @author linux_china
 */
@Component
@CommandLine.Command(name = "all", mixinStandardHelpOptions = true, description = "List all installed JDK")
public class ListInstalledJDK implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        final String userHome = System.getProperty("user.home");
        List<File> installedDirs = Arrays.asList(
                new File("/Library/Java/JavaVirtualMachines"),
                new File(userHome, "Library/Java/JavaVirtualMachines"),
                new File(userHome, ".sdkman/candidates/java/"),
                new File(userHome, ".gradle/jdks"),
                new File(userHome, ".jenv/candidates/java"),
                new File(userHome, "..m2/jdks")
        );
        List<String[]> table = new ArrayList<>();
        table.add(new String[]{"Vendor", "Version", "Path"});
        System.out.println("Scanning the JDK...");
        for (File dest : installedDirs) {
            if (dest.exists() && dest.listFiles() != null) {
                final List<String[]> rows = Stream.of(Objects.requireNonNull(dest.listFiles()))
                        .filter(File::isDirectory)
                        .filter(dir -> {
                            final File javaBin1 = new File(dir, "bin/java");
                            final File javaBin2 = new File(dir, "Contents/Home/bin/java");
                            return javaBin1.exists() || javaBin2.exists();
                        })
                        .map(javaHome -> {
                            final File javaHome2 = new File(javaHome, "Contents/Home");
                            return javaHome2.exists() ? javaHome2 : javaHome;
                        })
                        .map(javaHome -> {
                            try {
                                File javaBin = new File(javaHome, "bin/java");
                                String javaVersionText = new ProcessExecutor().command(javaBin.getAbsolutePath(), "-version")
                                        .readOutput(true).execute().outputUTF8().split("\n")[0];
                                final String[] parts = javaVersionText.split("[\\s\"]+");
                                String vendor = parts[0];
                                String version = parts[2];
                                return new String[]{vendor, version, javaHome.getAbsolutePath()};
                            } catch (Exception e) {
                                return new String[]{"error", "error", javaHome.getAbsolutePath()};
                            }
                        }).collect(Collectors.toList());
                table.addAll(rows);
            }
        }
        tableWithLines(table);
        return 0;
    }

    public static void tableWithLines(List<String[]> table) {
        /*
         * leftJustifiedRows - If true, it will add "-" as a flag to format string to
         * make it left justified. Otherwise right justified.
         */
        boolean leftJustifiedRows = false;
        /*
         * Calculate appropriate Length of each column by looking at width of data in
         * each column.
         *
         * Map columnLengths is <column_number, column_length>
         */
        Map<Integer, Integer> columnLengths = new HashMap<>();
        table.forEach(row -> Stream.iterate(0, (i -> i < row.length), (i -> ++i)).forEach(i -> {
            columnLengths.putIfAbsent(i, 0);
            if (columnLengths.get(i) < row[i].length()) {
                columnLengths.put(i, row[i].length());
            }
        }));
        //Prepare format String
        final StringBuilder formatString = new StringBuilder("");
        String flag = leftJustifiedRows ? "-" : "";
        columnLengths.entrySet().stream().forEach(e -> formatString.append("| %" + flag + e.getValue() + "s "));
        formatString.append("|\n");

        //Prepare line for top, bottom & below header row.
        String line = columnLengths.entrySet().stream().reduce("", (ln, b) -> {
            String templn = "+-";
            templn = templn + Stream.iterate(0, (i -> i < b.getValue()), (i -> ++i)).reduce("", (ln1, b1) -> ln1 + "-",
                    (a1, b1) -> a1 + b1);
            templn = templn + "-";
            return ln + templn;
        }, (a, b) -> a + b);
        line = line + "+\n";
        // print table
        System.out.print(line);
        table.stream().limit(1).forEach(row -> System.out.printf(formatString.toString(), row));
        System.out.print(line);
        Stream.iterate(1, (i -> i < table.size()), (i -> ++i))
                .forEach(rowNum -> System.out.printf(formatString.toString(), (Object[]) table.get(rowNum)));
        System.out.print(line);
    }
}
