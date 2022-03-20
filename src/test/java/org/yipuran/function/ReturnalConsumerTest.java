package org.yipuran.function;

import org.junit.Assert;
import org.junit.Test;

public class ReturnalConsumerTest {

	@Test
	public void test1() {
		Foo foo = ReturnalConsumer.of(Foo.class)
		.with(e->e.setName("ABC"))
		.with(e->e.setValue(12))
		.get(new Foo());

		Assert.assertEquals("ABC", foo.getName());
		Assert.assertEquals(12, foo.getValue());
	}

	@Test
	public void test2() {
		ReturnalConsumer<Foo> rc = ReturnalConsumer.of(Foo.class);
		rc = setData(rc);
		Foo foo = rc.get(new Foo());

		Assert.assertEquals("ABC", foo.getName());
		Assert.assertEquals(12, foo.getValue());
	}
	private ReturnalConsumer<Foo> setData(ReturnalConsumer<Foo> r){
		return r.with(e->e.setName("ABC")).with(e->e.setValue(12));
	}


	class Foo{
		private String name;
		private int value;
		public String getName() {
			return name;
		}
		public void setName(String s) {
			name = s;
		}
		public int getValue() {
			return value;
		}
		public void setValue(int value) {
			this.value = value;
		}
	}
}
