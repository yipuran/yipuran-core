package org.yipuran.function;

import java.util.Optional;
import java.util.function.Supplier;

import org.junit.Assert;
import org.junit.Test;

public class AcceptFunctionTest {

	@Test
	public void simple() {
		Supplier<Integer> sp = ()->Integer.parseInt("12");
		Foo foo = Optional.of(new Foo())
		.map(AcceptFunction.of(Foo::setName, "Apple"))
		.map(AcceptFunction.of(Foo::setValue, sp) )
		.get();
		Assert.assertEquals("Apple", foo.getName());
		Assert.assertEquals(12, foo.getValue());
	}
	@Test
	public void multi() {
		Foo foo = Optional.of(new Foo())
		.map(AcceptFunction.of(Foo::setValue, Integer::parseInt, "12") )
		.get();
		Assert.assertEquals(12, foo.getValue());
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
