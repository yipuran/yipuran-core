package org.yipuran.function;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

public class ThrowableUnaryOperatorTest {

	@Test
	public void throwTest1(){
		List<String> clist = Arrays.asList("10", "20", "30").stream().collect(Collectors.toList());
		List<String> list = Arrays.asList("1", "2", "3").stream()
		.map(ThrowableUnaryOperator.of(e->convert(e, 0), (e, x)->{
			Assert.assertEquals(true, x instanceof IllegalArgumentException);
			Assert.assertEquals("arg error", x.getMessage());
			return e+"0";
		})).collect(Collectors.toList());
		Assert.assertThat(list, CoreMatchers.is(clist));
	}
	@Test
	public void throwTest2(){
		List<String> clist = Arrays.asList("10", "20", "30").stream().collect(Collectors.toList());
		List<String> list = Arrays.asList("1", "2", "3").stream()
		.map(ThrowableUnaryOperator.of(e->convert(e, 1), (e, x)->{
			Assert.assertEquals(true, x instanceof IllegalArgumentException);
			Assert.assertEquals("arg error", x.getMessage());
			return e+"0";
		}
		)).collect(Collectors.toList());
		Assert.assertThat(list, CoreMatchers.is(clist));
	}
	@Test(expected=RuntimeException.class)
	public void throwTest3(){
		Arrays.asList("1", "2", "3").stream()
		.map(ThrowableUnaryOperator.of(e->convert(e, 1))).collect(Collectors.toList());
		Assert.fail();
	}
	@Test
	public void throwTest4(){
		List<String> clist = Arrays.asList("10", "20", "30").stream().collect(Collectors.toList());
		List<String> list = Arrays.asList("1", "2", "3").stream()
		.map(ThrowableUnaryOperator.of(e->convert(e, 0))).collect(Collectors.toList());
		Assert.assertThat(list, CoreMatchers.is(clist));
	}
	private String convert(String s,int n) {
		if (n==1)
			throw new IllegalArgumentException("arg error");
		return s + "0";
	}
}
