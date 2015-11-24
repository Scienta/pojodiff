package no.scienta.tools.pojodiff;

import java.util.HashMap;
import java.util.Map;

public class Top {

    private final String topName;

    protected Top(String topName) {
        this.topName = topName;
    }

    public String getTopName() {
        return topName;
    }

    public final Map<String, Integer> countMap = new HashMap<>();
}
