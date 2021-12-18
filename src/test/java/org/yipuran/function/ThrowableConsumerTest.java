package org.yipuran.function;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

public class ThrowableConsumerTest {

	@Test(expected=RuntimeException.class)
	public void throwTest() {
		Arrays.asList("1", "A").stream().forEach(ThrowableConsumer.of(e->{
			Assert.assertThat(1, CoreMatchers.is(Integer.parseInt(e)));
		}));
	}
	@Test
	public void catchTest() {
		Arrays.asList("1", "A").stream().forEach(ThrowableConsumer.of(e->{
			Assert.assertThat(1, CoreMatchers.is(Integer.parseInt(e)));
		}, (e, x)->{
			Assert.assertEquals("A", e);
			Assert.assertThat(x, CoreMatchers.instanceOf(NumberFormatException.class));
		}));
	}
	@Test
	public void andThen() {
		String[] sary = { "1", "2", "A" };
		ThrowableConsumer<String> ct = e->Integer.parseInt(e);
		AtomicInteger index = new AtomicInteger(0);
		Arrays.stream(sary).forEach(
			ct.andThen(e->{
				Assert.assertEquals(sary[index.getAndIncrement()], e);
			}, (e, x)->{
				Assert.assertEquals(sary[index.get()], e);
				Assert.assertThat(x, CoreMatchers.instanceOf(NumberFormatException.class));
			})
		);
	}
}
