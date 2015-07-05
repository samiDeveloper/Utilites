package bs;

public class ColumnValue {
    private final String column;
    private final String value;

    public ColumnValue(String column) {
        super();
        this.column = column;
        this.value = null;
    }
    
    private ColumnValue(String column, String value) {
        super();
        this.column = column;
        this.value = value;
    }

    public ColumnValue withValue(String value) {
        return new ColumnValue(column, value);
    }

    public String getColumn() {
        return column;
    }

    public String getValue() {
        return value;
    }
    
    /** Returns the largest of the columnname length and the value length */
    public int length() {
        return Integer.max(column.length(), value.length());
    }
}
