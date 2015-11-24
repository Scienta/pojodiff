package no.scienta.tools.pojodiff;

public class Bar extends Top {

    private final String barName;

    private final Bar nestedBar;

    protected Bar(String topName, String barName, Bar nestedBar) {
        super(topName);
        this.barName = barName;
        this.nestedBar = nestedBar;
    }

    public Bar getNestedBar() {
        return nestedBar;
    }

    public String getBarName() {
        return barName;
    }
}
