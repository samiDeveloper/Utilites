package bs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
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
    private final List<ColumnValue> columnValues = new ArrayList<>();
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
        columnValues.add(new ColumnValue(column));
    }

    public void withColumnValue(String value) {
        ColumnValue cv = columnValues.get(++lastColumnValueAddedIndex).withValue(value);
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

    public List<ColumnValue> getColumnValues() {
        return Collections.unmodifiableList(columnValues);
    }

    public static String format(List<InsertStatement> statements) {
        return statements.stream().map(InsertStatement::format).collect(Collectors.joining(NEWLINE));
    }
    
    public String format() {
        return format(new Config());
    }
    
    public String format(Config config) {
        StringBuilder sb = new StringBuilder(insert).append(SPACE).append(into).append(SPACE).append(tablename)
                .append(SPACE).append(PAREN_OPEN).append(NEWLINE);

        formatColumnNames(sb, config);

        sb.append(PAREN_CLOSE).append(SPACE).append(values).append(SPACE).append(PAREN_OPEN).append(NEWLINE);

        formatColumnValues(sb, config);

        sb.append(PAREN_CLOSE);

        if (terminatedBySemicolon) {
            sb.append(SEMICOLON);
        }

        return sb.toString();
    }

    private void formatColumnNames(StringBuilder sb, Config config) {
        final String indent = buildSpaces(config.getIndent());
        final String spacing = buildSpaces(config.getSpacingBetweenValues());

        Iterator<ColumnValue> columnIter = columnValues.iterator();
        while (columnIter.hasNext()) {
            ColumnValue firstColOfLine = columnIter.next();
            StringBuilder columnLine = new StringBuilder(indent).append(firstColOfLine.getColumn());
            int lineLength = indent.length() + firstColOfLine.length();
            ColumnValue lastCol = firstColOfLine;

            while (columnIter.hasNext() && lineLength < config.getLineWidth()) {
                // %1$-5 right-pads with 5 spaces
                columnLine.append(COMMA).append(spacing).append(buildColumnPaddingString(lastCol));
                ColumnValue currentCol = columnIter.next();
                lineLength = columnLine.length() + currentCol.length();
                columnLine.append(currentCol.getColumn());

                lastCol = currentCol;
            }
            sb.append(columnLine).append(NEWLINE);
        }
    }

    private void formatColumnValues(StringBuilder sb, Config config) {
        final String indent = buildSpaces(config.getIndent());
        final String spacing = buildSpaces(config.getSpacingBetweenValues());

        Iterator<ColumnValue> columnIter = columnValues.iterator();
        while (columnIter.hasNext()) {
            ColumnValue firstColOfLine = columnIter.next();
            StringBuilder columnLine = new StringBuilder(indent).append(firstColOfLine.getValue());
            int lineLength = indent.length() + firstColOfLine.length();
            ColumnValue lastCol = firstColOfLine;

            while (columnIter.hasNext() && lineLength < config.getLineWidth()) {
                // %1$-5 right-pads with 5 spaces
                columnLine.append(COMMA).append(spacing).append(buildValuePaddingString(lastCol));
                ColumnValue currentCol = columnIter.next();
                columnLine.append(currentCol.getValue());
                lineLength += currentCol.length();

                lastCol = currentCol;
            }
            sb.append(columnLine).append(NEWLINE);
        }
    }

    private static String buildColumnPaddingString(ColumnValue lastCol) {
        int padding = calcColumnPadSpaces(lastCol);
        if (padding == 0) {
            return "";
        }
        return String.format("%1$-" + padding + "s", "");
    }

    private static String buildValuePaddingString(ColumnValue lastCol) {
        int padding = calcValuePadSpaces(lastCol);
        return buildSpaces(padding);
    }

    private static String buildSpaces(int spaces) {
        if (spaces == 0) {
            return "";
        }
        return String.format("%1$-" + spaces + "s", "");
    }

    public static int calcColumnPadSpaces(ColumnValue cv) {
        return cv.length() - cv.getColumn().length();
    }

    public static int calcValuePadSpaces(ColumnValue cv) {
        return cv.length() - cv.getValue().length();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(insert).append(SPACE).append(into).append(SPACE).append(tablename)
                .append(SPACE).append(PAREN_OPEN);
        for (ColumnValue cv : columnValues) {
            sb.append(cv.getColumn()).append(COMMA);
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(PAREN_CLOSE).append(SPACE).append(values).append(SPACE).append(PAREN_OPEN);
        for (ColumnValue cv : columnValues) {
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
