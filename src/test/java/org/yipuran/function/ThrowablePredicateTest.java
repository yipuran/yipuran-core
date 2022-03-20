package org.yipuran.function;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

public class ThrowablePredicateTest {
	@Test(expected=RuntimeException.class)
	public void throwTest() {
		Arrays.asList("1", "A", "2").stream()
		.filter(ThrowablePredicate.of(e->Integer.parseInt(e) % 2 == 0))
		.forEach(e->{});
	}
	@Test
	public void catchTest() {
		List<String> clist = Arrays.asList("2", "4").stream().collect(Collectors.toList());
		List<String> reslist = Arrays.asList("1", "A", "2", "3", "4").stream()
		.filter(ThrowablePredicate.of(e->Integer.parseInt(e) % 2 == 0, (e, x)->{
			Assert.assertThat(x, CoreMatchers.instanceOf(NumberFormatException.class));
			return false;
		}))
		.collect(Collectors.toList());
		Assert.assertThat(reslist, CoreMatchers.is(clist));
	}
	@Test(expected=RuntimeException.class)
	public void andTest(){
		ThrowablePredicate<String> pt = e->Integer.parseInt(e) > 3;
		Arrays.asList("1", "A", "2", "3", "4").stream()
		.filter(pt.and(ThrowablePredicate.of(e->Integer.parseInt(e) % 2 == 0)))
		.collect(Collectors.toList());
	}
	@Test
	public void andCatchTest(){
		List<String> clist = Arrays.asList("4").stream().collect(Collectors.toList());
		ThrowablePredicate<String> pt = e->Integer.parseInt(e) > 3;
		List<String> reslist = Arrays.asList("1", "A", "2", "3", "4").stream()
		.filter(pt.and(e->Integer.parseInt(e) % 2 == 0, (e, x)->{
				Assert.assertThat(x, CoreMatchers.instanceOf(NumberFormatException.class));
				return false;
			})
		).collect(Collectors.toList());
		Assert.assertThat(reslist, CoreMatchers.is(clist));
	}
	@Test(expected=RuntimeException.class)
	public void throwNegateTest() {
		ThrowablePredicate<String> pt = e->Integer.parseInt(e) % 2 == 0;
		Arrays.asList("1", "A", "2").stream()
		.filter(pt.negate())
		.forEach(e->{});
	}
	@Test
	public void negateCatchTest(){
		List<String> clist = Arrays.asList("1", "A", "2").stream().collect(Collectors.toList());
		ThrowablePredicate<String> pt = e->Integer.parseInt(e) > 2;
		List<String> reslist = Arrays.asList("1", "A", "2", "3", "4").stream()
		.filter(pt.negate((e, x)->{
				Assert.assertThat(x, CoreMatchers.instanceOf(NumberFormatException.class));
				return false;
			})
		).collect(Collectors.toList());
		Assert.assertThat(reslist, CoreMatchers.is(clist));
	}
	@Test(expected=RuntimeException.class)
	public void orTest(){
		ThrowablePredicate<String> pt = e->Integer.parseInt(e) > 2;
		Arrays.asList("1", "A", "2", "3", "4").stream()
		.filter(pt.or(ThrowablePredicate.of(e->Integer.parseInt(e) % 2 == 0)))
		.collect(Collectors.toList());
	}
	@Test
	public void orCatchTest(){
		List<String> clist = Arrays.asList("2", "3", "4").stream().collect(Collectors.toList());
		ThrowablePredicate<String> pt = e->Integer.parseInt(e) > 2;
		List<String> reslist = Arrays.asList("1", "A", "2", "3", "4").stream()
		.filter(pt.or(ThrowablePredicate.of(e->Integer.parseInt(e) % 2 == 0), (e, x)->{
			Assert.assertThat(x, CoreMatchers.instanceOf(NumberFormatException.class));
			return false;
		}))
		.collect(Collectors.toList());
		Assert.assertThat(reslist, CoreMatchers.is(clist));
	}
}
