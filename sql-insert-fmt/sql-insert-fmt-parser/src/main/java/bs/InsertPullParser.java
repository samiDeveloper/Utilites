package bs;

import java.util.Optional;

public class InsertPullParser {
    public boolean nextInsertStatement() {
        return true;
    }

    public String nextInsert() {
        return null;
    }

    public String nextInto() {
        return null;
    }

    public String nextTablename() {
        return null;
    }

    public Optional<String> nextColumn() {
        return null;
    }

    public Optional<String> nextValue() {
        return null;
    }
}
