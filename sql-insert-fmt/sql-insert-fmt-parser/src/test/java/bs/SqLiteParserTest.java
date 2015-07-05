package bs;

import java.io.IOException;
import java.io.InputStream;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.junit.Assert;
import org.junit.Test;

import bs.sqlitegrammar.SQLiteBaseListener;
import bs.sqlitegrammar.SQLiteLexer;
import bs.sqlitegrammar.SQLiteParser;
import bs.sqlitegrammar.SQLiteParser.Any_nameContext;
import bs.sqlitegrammar.SQLiteParser.Column_defContext;
import bs.sqlitegrammar.SQLiteParser.Column_nameContext;
import bs.sqlitegrammar.SQLiteParser.Insert_stmtContext;
import bs.sqlitegrammar.SQLiteParser.NameContext;
import bs.sqlitegrammar.SQLiteParser.Sql_stmtContext;
import bs.sqlitegrammar.SQLiteParser.Sql_stmt_listContext;
import bs.sqlitegrammar.SQLiteParser.Table_nameContext;

/** Actually this tests the generated code but it is nice to see it working. **/
public class SqLiteParserTest {
    @Test
    public void testParseSingle() throws IOException {

        InputStream is = getClass().getResourceAsStream("/sqLiteParserTest.insert.sql");

        SQLiteLexer lexer = new SQLiteLexer(new ANTLRInputStream(is));

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        SQLiteParser parser = new SQLiteParser(tokens);
        Sql_stmtContext sqlStmt = parser.sql_stmt();

        ParseTreeWalker walker = new ParseTreeWalker();
        SQLiteBaseListener listener = new SQLiteBaseListener() {
            @Override
            public void enterInsert_stmt(Insert_stmtContext ctx) {
                Assert.assertTrue(ctx.getText().startsWith("INSERT"));
            }
        };
        walker.walk(listener, sqlStmt);
    }

    @Test
    public void testParseMulti() throws IOException {

        InputStream is = getClass().getResourceAsStream("/sqLiteParserTest.insert-multi.sql");

        SQLiteLexer lexer = new SQLiteLexer(new ANTLRInputStream(is));

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        SQLiteParser parser = new SQLiteParser(tokens);
        Sql_stmt_listContext sqlStmtList = parser.sql_stmt_list();

        ParseTreeWalker walker = new ParseTreeWalker();
        SQLiteBaseListener listener = new SQLiteBaseListener() {
            @Override
            public void enterInsert_stmt(Insert_stmtContext ctx) {
                Assert.assertTrue(ctx.getText().startsWith("INSERT"));
            }
        };
        walker.walk(listener, sqlStmtList);
    }

    @Test
    public void testWalkAndListen() throws IOException {

        InputStream is = getClass().getResourceAsStream("/sqLiteParserTest.insert.sql");

        SQLiteLexer lexer = new SQLiteLexer(new ANTLRInputStream(is));

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        SQLiteParser parser = new SQLiteParser(tokens);
        Sql_stmtContext sqlStmt = parser.sql_stmt();

        ParseTreeWalker walker = new ParseTreeWalker();
        SQLiteBaseListener listener = new SQLiteBaseListener() {
            @Override
            public void enterInsert_stmt(Insert_stmtContext ctx) {
                Assert.assertTrue(ctx.getText().startsWith("INSERT"));
            }

            @Override
            public void enterTable_name(Table_nameContext ctx) {
                System.out.println(ctx.getText());
            }

            @Override
            public void enterColumn_name(Column_nameContext ctx) {
//                System.out.println(ctx.getText());
            }
            
            @Override
            public void visitTerminal(TerminalNode node) {
              System.out.println(node);
            }


        };
        walker.walk(listener, sqlStmt);
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}

