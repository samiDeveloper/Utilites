package bs;

public class Config {
    private int spacingBetweenValues = 0;
    private int lineWidth = 120;
    private int indent = 4;

    public int getSpacingBetweenValues() {
        return spacingBetweenValues;
    }

    public Config setSpacingBetweenValues(int spacingBetweenValues) {
        this.spacingBetweenValues = spacingBetweenValues;
        return this;
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public Config setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
        return this;
    }

    public int getIndent() {
        return indent;
    }

    public Config setIndent(int indent) {
        this.indent = indent;
        return this;
    }
}
