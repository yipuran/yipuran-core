package org.yipuran.function;

import org.junit.Assert;
import org.junit.Test;

public class ApplyConsumerTest {

	@Test
	public void accept() {
		Base base = new Base("A", 11);
		Item item = new Item("B", 12);

		ApplyConsumer.of(Base::getId, Item::setId)
		.and(Base::getName, Item::setName)
		.accept(base, item);
		Assert.assertEquals(11, item.getId());
		Assert.assertEquals("A", item.getName());
	}
	class Base{
		private String name;
		private int id;
		public Base(String s, int i){
			name = s;
			id = i;
		}
		public String getName() {
			return name;
		}
		public void setName(String s) {
			name = s;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
	}
	class Item{
		private String name;
		private int id;
		public Item(String s, int i){
			name = s;
			id = i;
		}
		public String getName() {
			return name;
		}
		public void setName(String s) {
			name = s;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
	}
}
