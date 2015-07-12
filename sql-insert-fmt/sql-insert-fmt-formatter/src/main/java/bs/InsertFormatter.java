package bs;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import bs.sqlitegrammar.SQLiteLexer;
import bs.sqlitegrammar.SQLiteParser;
import bs.sqlitegrammar.SQLiteParser.Sql_stmt_listContext;

public final class InsertFormatter {
    public static InsertFormatterResponse format(String sql) {
        return format(sql, new Config());
    }

    private static Sql_stmt_listContext createSqlStmtListContext(String sql, InsertErrorListener errorListener) {
        SQLiteLexer lexer = new SQLiteLexer(new ANTLRInputStream(sql));

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        SQLiteParser parser = new SQLiteParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(errorListener);
        Sql_stmt_listContext sqlStmtList = parser.sql_stmt_list();
        return sqlStmtList;
    }

    public static InsertFormatterResponse format(String sql, Config cfg) {
        InsertErrorListener errorListener = new InsertErrorListener();
        Sql_stmt_listContext sqlStmtList = createSqlStmtListContext(sql, errorListener);

        final InsertFormatterResponse rs;
        if (errorListener.isError()) {

            rs = InsertFormatterResponse.error(errorListener.getErrors().toString());

        } else {

            ParseTreeWalker walker = new ParseTreeWalker();
            InsertParser listener = new InsertParser();

            rs = walkTree(walker, listener, sqlStmtList, cfg);
        }

        return rs;
    }

    private static InsertFormatterResponse walkTree(ParseTreeWalker walker, InsertParser listener, Sql_stmt_listContext sqlStmtList,
            Config cfg) {
        
        try {
            walker.walk(listener, sqlStmtList);
            String formatted = InsertStatement.format(listener.getStatements(), cfg);
            return InsertFormatterResponse.ok(formatted);

        } catch (InsertParserException e) {

            return InsertFormatterResponse.error(e.getMessage());
        }

    }

}
