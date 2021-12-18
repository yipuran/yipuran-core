package org.yipuran.util.collection.test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.yipuran.util.collection.MapFunction;

public class MapFunctionTest {

	@Test
	void testApply() {
		Map<String, List<String>> mapA = new HashMap<>();
		List<String> a = new ArrayList<>();
		a.add("10");
		a.add("11");
		mapA.put("A", a);
		List<String> b = new ArrayList<>();
		b.add("20");
		mapA.put("B", b);
		List<String> c = new ArrayList<>();
		c.add("30");
		mapA.put("C", c);

		Map<String, List<String>> map = new HashMap<>();
		MapFunction<String, List<String>> m = MapFunction.of(map, ArrayList::new);
		m.apply("A").add("10");
		m.apply("A").add("11");
		m.apply("B").add("20");
		m.apply("C").add("30");
		assertThat(map, is(mapA));
	}

}
