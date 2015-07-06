package bs;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Assert;
import org.junit.Test;

import bs.sqlitegrammar.SQLiteLexer;
import bs.sqlitegrammar.SQLiteParser;
import bs.sqlitegrammar.SQLiteParser.Sql_stmt_listContext;

public class InsertParserTest {
    @Test
    public void testParse() throws IOException {

        String input = "/sqLiteParserTest.insert.sql";
        Sql_stmt_listContext sqlStmtList = createSqlStmtListContext(input);

        ParseTreeWalker walker = new ParseTreeWalker();
        InsertParser listener = new InsertParser();
        walker.walk(listener, sqlStmtList);

        String expected = resourceToString("insertParserTest.testParse.expected.txt");
        Config config = new Config().setLineWidth(60).setIndent(4).setSpacingBetweenValues(0);
        String actual = listener.getStatements().iterator().next().format(config);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseMultiple() throws IOException {

        String input = "/sqLiteParserTest.insert-multi.sql";

        Sql_stmt_listContext sqlStmtList = createSqlStmtListContext(input);

        ParseTreeWalker walker = new ParseTreeWalker();
        InsertParser listener = new InsertParser();
        walker.walk(listener, sqlStmtList);

        String expected = resourceToString("insertParserTest.testParseMultiple.expected.txt");
        String actual = InsertStatement.format(listener.getStatements());
        Assert.assertEquals(expected, actual);
    }

    private static Sql_stmt_listContext createSqlStmtListContext(String resource) throws IOException {
        InputStream is = InsertParserTest.class.getResourceAsStream(resource);
        SQLiteLexer lexer = new SQLiteLexer(new ANTLRInputStream(is));

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        SQLiteParser parser = new SQLiteParser(tokens);
        Sql_stmt_listContext sqlStmtList = parser.sql_stmt_list();
        return sqlStmtList;
    }

    private static String resourceToString(String resource) {
        Scanner scanner = new Scanner(InsertParserTest.class.getResourceAsStream(resource), "UTF-8")
                .useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }

}
