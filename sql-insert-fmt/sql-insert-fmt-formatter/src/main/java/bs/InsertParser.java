package bs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import bs.sqlitegrammar.SQLiteBaseListener;
import bs.sqlitegrammar.SQLiteParser.Column_nameContext;
import bs.sqlitegrammar.SQLiteParser.ErrorContext;
import bs.sqlitegrammar.SQLiteParser.ExprContext;
import bs.sqlitegrammar.SQLiteParser.Insert_stmtContext;
import bs.sqlitegrammar.SQLiteParser.Table_nameContext;

/** Build an InsertStatement */
public class InsertParser extends SQLiteBaseListener {

    private Element currentElement = Element.NONE;

    /** We need to ignore all events while parsing a column value */
    private Optional<ExprContext> currentColumnValueContext = Optional.empty();

    private final List<InsertStatement> statements = new ArrayList<>();
    private InsertStatement statement;

    @Override
    public void enterInsert_stmt(Insert_stmtContext ctx) {
        currentElement = Element.INSERT_INTO_TABLENAME;
    }

    @Override
    public void enterTable_name(Table_nameContext ctx) {
        statement.withTable(ctx.getText());
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
            statements.add(statement);

        } else if (nodeValue.equalsIgnoreCase("into")) {
            statement.withInto(nodeValue);

        } else if (nodeValue.equalsIgnoreCase("values")) {
            statement.withValues(nodeValue);

        } else if (currentElement == Element.INSERT_INTO_TABLENAME && nodeValue.equals(InsertStatement.PAREN_OPEN)) {
            currentElement = Element.COLUMNNAMES;

        } else if (currentElement == Element.COLUMNNAMES && nodeValue.equals(InsertStatement.PAREN_CLOSE)) {
            currentElement = Element.VALUES;

        } else if (currentElement == Element.VALUES && nodeValue.equals(InsertStatement.PAREN_OPEN)) {
            currentElement = Element.COLUMNVALUES;

        } else if (currentElement == Element.COLUMNVALUES && !currentColumnValueContext.isPresent()
                && nodeValue.equals(InsertStatement.PAREN_CLOSE)) {

        } else if (currentElement == Element.COLUMNVALUES && !currentColumnValueContext.isPresent()
                && nodeValue.equals(InsertStatement.SEMICOLON)) {
            statement.terminatedBySemicolon();
        }
    }

    /** Column values events */
    @Override
    public void enterExpr(ExprContext ctx) {

        // we already have the text of the column value, ignore the column value event if we're inside one,
        // for example while parsing a date function
        if (!currentColumnValueContext.isPresent()) {

            // we just need the text of this column value
            statement.withColumnValue(ctx.getText());
            currentColumnValueContext = Optional.of(ctx);
        }
    };

    public void exitExpr(ExprContext ctx) {
        if (this.currentColumnValueContext.isPresent() && currentColumnValueContext.get() == ctx) {
            currentColumnValueContext = Optional.empty();
        }
    };

    @Override
    public String toString() {
        return statements.toString();
    }

    public InsertStatement getStatement() {
        return statement;
    }

    public List<InsertStatement> getStatements() {
        return Collections.unmodifiableList(statements);
    }

}
