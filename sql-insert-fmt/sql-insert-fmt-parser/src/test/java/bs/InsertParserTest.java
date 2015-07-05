package bs;

import java.io.IOException;
import java.io.InputStream;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Assert;
import org.junit.Test;

import bs.sqlitegrammar.SQLiteLexer;
import bs.sqlitegrammar.SQLiteParser;
import bs.sqlitegrammar.SQLiteParser.Sql_stmtContext;

public class InsertParserTest {
    @Test
    public void testParse() throws IOException {

        InputStream is = getClass().getResourceAsStream("/sqLiteParserTest.insert.sql");

        SQLiteLexer lexer = new SQLiteLexer(new ANTLRInputStream(is));

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        SQLiteParser parser = new SQLiteParser(tokens);
        Sql_stmtContext sqlStmt = parser.sql_stmt();

        ParseTreeWalker walker = new ParseTreeWalker();
        InsertParser listener = new InsertParser() {
        };
        walker.walk(listener, sqlStmt);
        System.out.println(listener.getStatement().format());
        Assert.fail("NYI");
    }


}
