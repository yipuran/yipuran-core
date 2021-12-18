package org.yipuran.function;

import org.junit.Assert;
import org.junit.Test;

public class NullableFunctionTest {
	@Test
	public void andThen() {
		NullableFunction<String, Integer> f1 = t->Integer.parseInt(t);
		Assert.assertEquals(0, f1.andThen(e->e * 2).apply("0").intValue());
		NullableFunction<Food, Item> f2 = t->t.item;
		Food food = new Food();
		Assert.assertEquals("Apple", f2.andThen(e->"Lemon", "Apple").apply(food));
		Assert.assertEquals("Apple", f2.andThen(e->"Lemon", ()->"Apple").apply(food));
	}
	@Test
	public void compose() {
		Food food = new Food();
		NullableFunction<Item, String> f1 = t->"Apple";
		NullableFunction<Food, Item> f2 = t->t.item;
		Assert.assertEquals(null, f1.compose(f2).apply(food));
		Assert.assertEquals("a", f1.compose(f2, "a").apply(food));
		Assert.assertEquals("a", f1.compose(f2, ()->"a").apply(food));

	}
	@Test
	public void bind() {
		NullableFunction<Food, Item> f1 = t->t.item;
		NullableFunction<Item, String> f2 = t->t.name;
		Food food = new Food();
		Assert.assertEquals("A", NullableFunction.bind(f1, f2, "A").apply(food));
		Assert.assertEquals("A", NullableFunction.bind(f1, f2, ()->"A").apply(food));
		food.item = new Item("A");
		Assert.assertEquals("A", NullableFunction.bind(f1, f2).apply(food));
	}
	class Food{
		Item item;
	}
	class Item{
		String name;
		public Item(String name) {
			this.name = name;
		}
	}
}
