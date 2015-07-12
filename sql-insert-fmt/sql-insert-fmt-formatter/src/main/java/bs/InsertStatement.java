package bs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/** Builder and formattable representation of the statement */
public class InsertStatement {
    public static final String SPACE = " ";
    public static final String PAREN_OPEN = "(";
    public static final String PAREN_CLOSE = ")";
    public static final String COMMA = ",";
    public static final String NEWLINE = "\n";
    public static final String SEMICOLON = ";";

    private String insert;
    private String into;
    private String tablename;
    private String values;
    private final List<ColumnNameAndValue> columnValues = new ArrayList<>();
    private boolean terminatedBySemicolon;

    private int lastColumnValueAddedIndex = -1;

    public InsertStatement(String insert) {
        this.insert = insert;
    }

    public void withInto(String into) {
        this.into = into;
    }

    public void withTable(String tablename) {
        this.tablename = tablename;
    }

    public void withValues(String values) {
        this.values = values;
    }

    public void withColumn(String column) {
        columnValues.add(new ColumnNameAndValue(column));
    }

    public void withColumnValue(String value) {
        ColumnNameAndValue cv = columnValues.get(++lastColumnValueAddedIndex).withValue(value);
        columnValues.set(lastColumnValueAddedIndex, cv);
    }

    public void terminatedBySemicolon() {
        this.terminatedBySemicolon = true;
    }

    public String getInsert() {
        return insert;
    }

    public String getInto() {
        return into;
    }

    public String getTablename() {
        return tablename;
    }

    public List<ColumnNameAndValue> getColumnValues() {
        return Collections.unmodifiableList(columnValues);
    }

    public static String format(List<InsertStatement> statements) {
        return statements.stream().map(InsertStatement::format).collect(Collectors.joining(NEWLINE));
    }

    public static String format(List<InsertStatement> statements, final Config config) {
        return statements.stream().map(i -> i.format(config)).collect(Collectors.joining(NEWLINE));
    }

    public String format() {
        return format(new Config());
    }

    public String format(Config config) {
        List<Integer> numColsForEachLine = calcNumColsForEachLine(config);

        StringBuilder sb = new StringBuilder(insert).append(SPACE).append(into).append(SPACE).append(tablename).append(SPACE)
                .append(PAREN_OPEN);

        formatColumns(sb, config, numColsForEachLine, (ColumnNameAndValue c) -> {
            return c.getName();
        }, (ColumnNameAndValue c) -> {
            return buildColumnPaddingString(c);
        });

        sb.append(NEWLINE).append(PAREN_CLOSE).append(SPACE).append(values).append(SPACE).append(PAREN_OPEN);

        formatColumns(sb, config, numColsForEachLine, (ColumnNameAndValue c) -> {
            return c.getValue();
        }, (ColumnNameAndValue c) -> {
            return buildValuePaddingString(c);
        });

        sb.append(NEWLINE).append(PAREN_CLOSE);

        if (terminatedBySemicolon) {
            sb.append(SEMICOLON);
        }

        return sb.toString();
    }

    private List<Integer> calcNumColsForEachLine(Config config) {
        List<Integer> result = new ArrayList<>();
        int spacing = config.getSpacingBetweenValues();

        int lineIndex = -1;
        int lineLen = config.getLineWidth();

        for (ColumnNameAndValue col : columnValues) {

            int nextLineLen = lineLen + col.length() + COMMA.length();

            if (nextLineLen >= config.getLineWidth()) {
                lineIndex++;
                lineLen = config.getIndent();
            } else {
                lineLen +=spacing;
            }

            incrementLineCols(result, lineIndex);
            lineLen += (col.length() + COMMA.length());
        }

        return result;
    }

    private static void incrementLineCols(List<Integer> result, int lineIndex) {
        if (lineIndex == result.size()) {
            result.add(1);
        } else if (lineIndex < result.size()) {
            Integer colCount = result.get(lineIndex);
            Integer newCount = colCount + 1;
            result.set(lineIndex, newCount);
        } else {
            throw new AssertionError();
        }
    }

    /** Formats the column names or values */
    private void formatColumns(StringBuilder sb, Config config, List<Integer> numColsForEachLine, Function<ColumnNameAndValue, String> nameOrValFetcher,
            Function<ColumnNameAndValue, String> nameOrValPaddingCreator) {

        final String indent = buildSpaces(config.getIndent());
        final String spacing = buildSpaces(config.getSpacingBetweenValues());

        Iterator<ColumnNameAndValue> columnIter = columnValues.iterator();

        for (Integer numColsForLine : numColsForEachLine) {

            sb.append(NEWLINE).append(indent);
            String padding = "";

            for (int i = 0; i < numColsForLine; i++) {
                ColumnNameAndValue columnAndValue = columnIter.next();
                sb.append(padding).append(nameOrValFetcher.apply(columnAndValue)).append(COMMA).append(spacing);
                padding = nameOrValPaddingCreator.apply(columnAndValue);
            }
        }

        sb.delete(sb.lastIndexOf(COMMA), sb.length()); // remove last comma and spacing
    }

    private static String buildColumnPaddingString(ColumnNameAndValue lastCol) {
        int padding = calcColumnPadSpaces(lastCol);
        return buildSpaces(padding);
    }

    private static String buildValuePaddingString(ColumnNameAndValue lastCol) {
        int padding = calcValuePadSpaces(lastCol);
        return buildSpaces(padding);
    }

    private static String buildSpaces(int spaces) {
        if (spaces == 0) {
            return "";
        }

        // %1$-5 right-pads with 5 spaces
        return String.format("%1$-" + spaces + "s", "");
    }

    /** Returns the number of spaces to prepend to the columnname to align it with the value */
    public static int calcColumnPadSpaces(ColumnNameAndValue cv) {
        return cv.length() - cv.getName().length();
    }

    /** Returns the number of spaces to prepend to the value to align it with the columnname */
    public static int calcValuePadSpaces(ColumnNameAndValue cv) {
        return cv.length() - cv.getValue().length();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(insert).append(SPACE).append(into).append(SPACE).append(tablename).append(SPACE)
                .append(PAREN_OPEN);
        for (ColumnNameAndValue cv : columnValues) {
            sb.append(cv.getName()).append(COMMA);
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(PAREN_CLOSE).append(SPACE).append(values).append(SPACE).append(PAREN_OPEN);
        for (ColumnNameAndValue cv : columnValues) {
            sb.append(cv.getValue()).append(COMMA);
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(PAREN_CLOSE);

        if (terminatedBySemicolon) {
            sb.append(SEMICOLON);
        }

        return sb.toString();
    }
}
