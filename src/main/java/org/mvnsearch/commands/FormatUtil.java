package org.mvnsearch.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Format util
 *
 * @author linux_china
 */
public class FormatUtil {
    public static final String ERROR = "\uD83D\uDC94";

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
