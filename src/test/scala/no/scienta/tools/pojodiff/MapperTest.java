package no.scienta.tools.pojodiff;

import org.junit.Test;

import java.util.Arrays;

public class MapperTest {

    @Test
    public void map() {
        System.out.println(new JavaMapper(
                new Foo("top", "bar", "foo", new Foo("nestedTop", "nestedBar", "nestedFoo", null))).toMap());
    }

    @Test
    public void mapList() {
        Foo foo = new Foo("top", "bar", "foo", new Foo("nestedTop", "nestedBar", "nestedFoo", null));

        foo.strings.addAll(Arrays.asList("s1", "s2", "s3"));

        foo.foos.add(new Foo("f1", null, null, null));
        foo.foos.add(new Foo("f2", null, null, null));
        foo.foos.add("string");

        System.out.println(new JavaMapper(foo).toMap());
    }
}
