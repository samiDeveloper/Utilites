package bs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/** Formattable representation of the statement */
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
        final String indent = "    ";

        StringBuilder sb = new StringBuilder(insert).append(SPACE).append(into).append(SPACE).append(tablename)
                .append(SPACE).append(PAREN_OPEN).append(NEWLINE);

        formatColumnNames(indent, sb);

        sb.append(PAREN_CLOSE).append(SPACE).append(values).append(SPACE).append(PAREN_OPEN).append(NEWLINE);

        formatColumnValues(indent, sb);

        sb.append(PAREN_CLOSE);

        if (terminatedBySemicolon) {
            sb.append(SEMICOLON);
        }

        return sb.toString();
    }

    private void formatColumnNames(final String indent, StringBuilder sb) {
        Iterator<ColumnValue> columnIter = columnValues.iterator();
        while (columnIter.hasNext()) {
            ColumnValue firstColOfLine = columnIter.next();
            StringBuilder columnLine = new StringBuilder(indent).append(firstColOfLine.getColumn());
            int lineLength = indent.length() + firstColOfLine.length();
            ColumnValue lastCol = firstColOfLine;

            while (columnIter.hasNext() && lineLength < 60) {
                // %1$-5 right-pads with 5 spaces
                columnLine.append(COMMA).append(buildColumnPaddingString(lastCol));
                ColumnValue currentCol = columnIter.next();
                columnLine.append(currentCol.getColumn());
                lineLength += currentCol.length();

                lastCol = currentCol;
            }
            sb.append(columnLine).append(NEWLINE);
        }
    }

    private void formatColumnValues(final String indent, StringBuilder sb) {
        Iterator<ColumnValue> columnIter = columnValues.iterator();
        while (columnIter.hasNext()) {
            ColumnValue firstColOfLine = columnIter.next();
            StringBuilder columnLine = new StringBuilder(indent).append(firstColOfLine.getValue());
            int lineLength = indent.length() + firstColOfLine.length();
            ColumnValue lastCol = firstColOfLine;

            while (columnIter.hasNext() && lineLength < 60) {
                // %1$-5 right-pads with 5 spaces
                columnLine.append(COMMA).append(buildValuePaddingString(lastCol));
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
        if (padding == 0) {
            return "";
        }
        return String.format("%1$-" + padding + "s", "");
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
