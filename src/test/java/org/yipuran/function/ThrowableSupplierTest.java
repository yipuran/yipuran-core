package org.yipuran.function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

public class ThrowableSupplierTest {

	@Test
	public void throwTest1(){
		List<String> clist = Arrays.asList("1", "2", "3").stream().collect(Collectors.toList());
		List<String> list = Arrays.asList("1", "2", "3").stream()
		.collect(ThrowableSupplier.to(()->{
			return createErrList(1);
		}, ex->{
			return new ArrayList<>();
		}), (r, t)->r.add(t), (t, u)->t.addAll(u));
		Assert.assertThat(list, CoreMatchers.is(clist));
	}
	@Test
	public void throwTest2(){
		List<String> clist = Arrays.asList("1", "2", "3").stream().collect(Collectors.toList());
		List<String> list = Arrays.asList("1", "2", "3").stream()
		.collect(ThrowableSupplier.to(()->{
			return createErrList(0);
		}, ex->{
			Assert.assertEquals(true, ex instanceof IllegalArgumentException);
			Assert.assertEquals("arg error", ex.getMessage());
			return new ArrayList<>();
		}), (r, t)->r.add(t), (t, u)->t.addAll(u));
		Assert.assertThat(list, CoreMatchers.is(clist));
	}
	@Test(expected=RuntimeException.class)
	public void throwTest3(){
		Arrays.asList("1", "2", "3").stream()
		.collect(ThrowableSupplier.to(()->createErrList(1)), (r, t)->r.add(t), (t, u)->t.addAll(u));
		Assert.fail();
	}

	@Test
	public void throwTest4(){
		List<String> clist = Arrays.asList("1", "2", "3").stream().collect(Collectors.toList());
		List<String> list = Arrays.asList("1", "2", "3").stream()
		.collect(ThrowableSupplier.to(()->createErrList(0)), (r, t)->r.add(t), (t, u)->t.addAll(u));
		Assert.assertThat(list, CoreMatchers.is(clist));
	}

	private List<String> createErrList(int n) throws IllegalArgumentException{
		if (n==1)
		throw new IllegalArgumentException("arg error");
		return new ArrayList<>();
	}
}
