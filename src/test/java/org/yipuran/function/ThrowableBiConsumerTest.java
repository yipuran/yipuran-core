package org.yipuran.function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

public class ThrowableBiConsumerTest {

	@Test(expected=RuntimeException.class)
	public void throwTest() {
		//List<Integer> list =
		Arrays.asList("1","2","a").stream().collect(()->new ArrayList<Integer>(),
			ThrowableBiConsumer.of((t, u)->t.add(Integer.parseInt(u))), (t, u)->{});
		Assert.fail();
	}
	@Test
	public void catchTest() {
		List<Integer> clist = Arrays.asList(1, 2).stream().collect(Collectors.toList());
		List<Integer> list =
		Arrays.asList("1","2", "a").stream().collect(()->new ArrayList<Integer>(),
			ThrowableBiConsumer.of((t, u)->t.add(Integer.parseInt(u)),
				(e, x)->{
					Assert.assertEquals("a", e.getValue());
					Assert.assertThat(x, CoreMatchers.instanceOf(NumberFormatException.class));
				}), (t, u)->{});
		Assert.assertThat(list, CoreMatchers.is(clist));
	}
	@Test
	public void andThen() {
	}
}
