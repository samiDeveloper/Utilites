package bs;

import java.util.Optional;

import org.antlr.v4.runtime.tree.TerminalNode;

import bs.sqlitegrammar.SQLiteBaseListener;
import bs.sqlitegrammar.SQLiteParser.Column_nameContext;
import bs.sqlitegrammar.SQLiteParser.ExprContext;
import bs.sqlitegrammar.SQLiteParser.Insert_stmtContext;
import bs.sqlitegrammar.SQLiteParser.Table_nameContext;

/** Build an InsertStatement */
public class InsertParser extends SQLiteBaseListener {

    private Element currentElement = Element.NONE;
    private Optional<ExprContext> currentColumnValueContext = Optional.empty();
    private InsertStatement statement;

    @Override
    public void enterInsert_stmt(Insert_stmtContext ctx) {
        currentElement = Element.INSERT_INTO_TABLENAME;
    }

    @Override
    public void enterTable_name(Table_nameContext ctx) {
        statement = statement.withTable(ctx.getText());
    }

    @Override
    public void enterColumn_name(Column_nameContext ctx) {
        statement.withColumn(ctx.getText());
    }

    @Override
    public void visitTerminal(TerminalNode node) {
        String nodeValue = node.getText();

        if (nodeValue.equalsIgnoreCase("insert")) {
            statement = new InsertStatement(nodeValue);

        } else if (nodeValue.equalsIgnoreCase("into")) {
            statement = statement.withInto(nodeValue);

        } else if (nodeValue.equalsIgnoreCase("values")) {
            statement = statement.withValues(nodeValue);

        } else if (currentElement == Element.INSERT_INTO_TABLENAME && nodeValue.equals(InsertStatement.PAREN_OPEN)) {
            currentElement = Element.COLUMNNAMES;

        } else if (currentElement == Element.INSERT_INTO_TABLENAME && nodeValue.equals(InsertStatement.PAREN_CLOSE)) {
            currentElement = Element.VALUES;

        } else if (currentElement == Element.VALUES && nodeValue.equals(InsertStatement.PAREN_OPEN)) {
            currentElement = Element.COLUMNVALUES;

        } else if (currentElement == Element.COLUMNVALUES && nodeValue.equals(InsertStatement.PAREN_CLOSE)) {
            currentElement = Element.NONE;
        }
    }

    @Override
    public void enterExpr(ExprContext ctx) {
        if (!currentColumnValueContext.isPresent()) {
            statement.withColumnValue(ctx.getText());
            currentColumnValueContext = Optional.of(ctx);
        }
    };

    @Override
    public void exitExpr(ExprContext ctx) {
        if (this.currentColumnValueContext.isPresent() && currentColumnValueContext.get() == ctx) {
            currentColumnValueContext = Optional.empty();
        }
    };

    @Override
    public String toString() {
        return statement.toString();
    }
    
    public InsertStatement getStatement() {return statement;}
}
