package bs;

/** Combination of column name and its value for a row */
public class ColumnNameAndValue {
    private final String name;
    private final String value;

    public ColumnNameAndValue(String name) {
        super();
        this.name = name;
        this.value = null;
    }
    
    private ColumnNameAndValue(String name, String value) {
        super();
        this.name = name;
        this.value = value;
    }

    public ColumnNameAndValue withValue(String value) {
        return new ColumnNameAndValue(name, value);
    }

    /** Returns the column name */
    public String getName() {
        return name;
    }

    /** Returns the column value */
    public String getValue() {
        return value;
    }
    
    /** Returns the largest of the columnname length and the value length */
    public int length() {
        return Integer.max(name.length(), value.length());
    }

    @Override
    public String toString() {
        return "ColumnNameAndValue [name=" + name + ", value=" + value + "]";
    }
}
