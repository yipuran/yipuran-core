package org.yipuran.function;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

public class ThrowableFunctionTest {

	@Test(expected=RuntimeException.class)
	public void throwTest() {
		Arrays.asList("1", "A", "2").stream()
		.map(ThrowableFunction.of(e->Integer.parseInt(e)))
		.forEach(e->{
			Assert.assertEquals(true, e==1);
		});
	}
	@Test
	public void catchTest() {
		List<Integer> clist = Arrays.asList(1, 0, 2).stream().collect(Collectors.toList());
		List<Integer> reslist = Arrays.asList("1", "A", "2").stream()
		.map(ThrowableFunction.of(e->Integer.parseInt(e),
				(e, x)->{
					Assert.assertThat(x, CoreMatchers.instanceOf(NumberFormatException.class));
					return 0;
				}
			)
		).collect(Collectors.toList());
		Assert.assertThat(reslist, CoreMatchers.is(clist));
	}
	@Test
	public void andThen() {
		List<Integer> clist = Arrays.asList(1, 0, 2).stream().collect(Collectors.toList());
		ThrowableFunction<String, Integer> ft = e->Integer.parseInt(e);
		List<Integer> reslist = Arrays.asList("1", "A", "2").stream()
		.map(ft.andThen(e->e, (e, x)->{
				Assert.assertThat(x, CoreMatchers.instanceOf(NumberFormatException.class));
				return 0;
			})
		).collect(Collectors.toList());
		Assert.assertThat(reslist, CoreMatchers.is(clist));
	}

	@Test
	public void compose() {
		List<Integer> clist = Arrays.asList(1, 0, 2).stream().collect(Collectors.toList());
		ThrowableFunction<String, Integer> ft = e->Integer.parseInt(e);
		List<Integer> reslist = Arrays.asList("1", "A", "2").stream()
		.map(ft.compose(e->e, (e, x)->{
			Assert.assertThat(x, CoreMatchers.instanceOf(NumberFormatException.class));
			return 0;
		})
		).collect(Collectors.toList());
		Assert.assertThat(reslist, CoreMatchers.is(clist));
	}
	@Test
	public void identity() {
		List<String> clist = Arrays.asList("1", "A", "2");
		List<String> reslist = Arrays.asList("1", "A", "2").stream()
		.map(ThrowableFunction.identity()).collect(Collectors.toList());
		Assert.assertThat(reslist, CoreMatchers.is(clist));
	}
}
