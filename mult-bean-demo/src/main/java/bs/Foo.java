package bs;

public class Foo {

    private Bar bar;

    public Foo(Bar bar) {
        this.bar = bar;
    }

    @Override
    public String toString() {
        return super.toString() + "(" + bar + ")";
    }
}
