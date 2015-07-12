package bs;

public final class InsertFormatterResponse {
    private boolean ok;
    private String error;
    private String formattedSql;

    private InsertFormatterResponse() {
    }

    public static InsertFormatterResponse error(String error) {
        InsertFormatterResponse rs = new InsertFormatterResponse();
        rs.error = error;
        rs.ok = false;
        return rs;
    }

    public static InsertFormatterResponse ok(String formattedSql) {
        InsertFormatterResponse rs = new InsertFormatterResponse();
        rs.formattedSql = formattedSql;
        rs.ok = true;
        return rs;
    }

    public boolean isOk() {
        return ok;
    }

    public String getError() {
        return error;
    }

    public String getFormattedSql() {
        return formattedSql;
    }
}
