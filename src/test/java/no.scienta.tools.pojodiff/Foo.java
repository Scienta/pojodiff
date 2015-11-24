package no.scienta.tools.pojodiff;

import java.util.ArrayList;
import java.util.List;

public class Foo extends Bar {

    private final String fooName;

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                "[topName=" + getTopName() +
                " barName=" + getBarName() +
                " fooName=" + fooName +
                " strings=" + strings +
                " foos=" + foos +
                "]";
    }

    protected Foo(String topName, String barName, String fooName, Foo nestedFoo) {
        super(topName, barName, nestedFoo);
        this.fooName = fooName;
    }

    public String getFooName() {
        return fooName;
    }

    public List<String> strings = new ArrayList<>();

    public List<Object> foos = new ArrayList<>();
}
