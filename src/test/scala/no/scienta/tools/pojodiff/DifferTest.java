package no.scienta.tools.pojodiff;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DifferTest {

    @Test
    public void testMapped() {
        Foo foo1 = new Foo("top", "bar", "foo", null);
        foo1.countMap.put("kjetil", 2);
        Foo foo2 = new Foo("top", "barz", "fooz", null);
        foo2.countMap.put("kjetil", 3);
        Foo foo3 = new Foo("top", "bar3", "fooz", null);
        foo3.countMap.put("kjetil", 4);
        Foo foo4 = new Foo("top", "bar", "foo4", null);
        foo4.countMap.put("kjetil", 5);

        List<Map<String, Object>> diffs = new Differ().toDiffs(foo1, foo2, foo3, foo4);

        assertEquals(4, diffs.size());

        assertEquals(3, diffs.get(0).size());
        assertEquals(diffs.get(0).get("barName"), "bar");
        assertEquals(diffs.get(0).get("fooName"), "foo");

        assertEquals(3, diffs.get(0).size());
        assertEquals(diffs.get(0).get("barName"), "bar");
        assertEquals(diffs.get(0).get("fooName"), "foo");

        assertEquals(3, diffs.get(0).size());
        assertEquals(diffs.get(0).get("countMap"), foo1.countMap);

        assertEquals(diffs.get(0).get("countMap"), foo1.countMap);
        assertEquals(diffs.get(1).get("countMap"), foo2.countMap);
    }

    @Test
    public void testListed() {
        Foo foo1 = new Foo("top", "bar", "foo", null);
        foo1.strings.add("kjetil");
        Foo foo2 = new Foo("top", "barz", "fooz", null);
        foo2.strings.add("thomas");

        List<Map<String, Object>> diffs = new Differ().toDiffs(foo1, foo2);

        assertEquals(3, diffs.get(0).size());
        assertEquals(diffs.get(0).get("barName"), "bar");
        assertEquals(diffs.get(0).get("fooName"), "foo");

        assertEquals(3, diffs.get(0).size());
        assertEquals(diffs.get(0).get("strings"), foo1.strings);

        assertEquals(2, diffs.size());
        assertEquals(diffs.get(0).get("strings"), foo1.strings);
        assertEquals(diffs.get(1).get("strings"), foo2.strings);

    }

    @Test
    public void testSimple() {
        List<Map<String, Object>> diffs = new Differ().toDiffs(
                new Foo("top", "bar", "foo", null),
                new Foo("top", "barz", "fooz", null));

        assertEquals(2, diffs.size());

        assertEquals(2, diffs.get(0).size());
        assertEquals(diffs.get(0).get("barName"), "bar");
        assertEquals(diffs.get(0).get("fooName"), "foo");

        assertEquals(2, diffs.get(1).size());
        assertEquals(diffs.get(1).get("barName"), "barz");
        assertEquals(diffs.get(1).get("fooName"), "fooz");
    }
}
