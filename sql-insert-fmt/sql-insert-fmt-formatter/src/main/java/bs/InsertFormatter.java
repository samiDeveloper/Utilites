package bs;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import bs.sqlitegrammar.SQLiteLexer;
import bs.sqlitegrammar.SQLiteParser;
import bs.sqlitegrammar.SQLiteParser.Sql_stmt_listContext;

public final class InsertFormatter {
    public static String format(String sql) {
        return format(sql, new Config());
    }

    private static Sql_stmt_listContext createSqlStmtListContext(String sql) {
        SQLiteLexer lexer = new SQLiteLexer(new ANTLRInputStream(sql));

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        SQLiteParser parser = new SQLiteParser(tokens);
        Sql_stmt_listContext sqlStmtList = parser.sql_stmt_list();
        return sqlStmtList;
    }

    public static String format(String sql, Config cfg) {
        Sql_stmt_listContext sqlStmtList = createSqlStmtListContext(sql);
        ParseTreeWalker walker = new ParseTreeWalker();
        InsertParser listener = new InsertParser();
        walker.walk(listener, sqlStmtList);

        String formatted = InsertStatement.format(listener.getStatements(), cfg);
        return formatted;
    }

}
