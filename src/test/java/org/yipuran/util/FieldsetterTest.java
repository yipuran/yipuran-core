package org.yipuran.util;

import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.yipuran.util.test.Item;
import org.yipuran.util.test.SubItem;

@RunWith(JUnit4.class)
public class FieldsetterTest {

	@Test
	public void test1() {
		LocalDateTime now = LocalDateTime.now();
		Item i1 = GenericBuilder.of(Item::new).with(Item::setId, "01").with(Item::setName, "ABC")
				.with(Item::setCreateAt, now).with(Item::setUpdateAt, now)
				.build();
		Item i2 = new Item();
		Fieldsetter.of((t, u)->"id").accept(i2, "01");
		Fieldsetter.of((t, u)->"name").accept(i2, "ABC");
		Fieldsetter.of((t, u)->"createAt").accept(i2, now);
		Fieldsetter.of((t, u)->"updateAt").accept(i2, now);

		Assert.assertEquals(i1, i2);
	}
	@Test
	public void test2() {
		LocalDateTime now = LocalDateTime.now();
		SubItem i1 = GenericBuilder.of(SubItem::new).with(SubItem::setId, "01").with(SubItem::setName, "ABC")
				.with(SubItem::setCreateAt, now).with(SubItem::setUpdateAt, now)
				.with(SubItem::setUsername, "あああ").with(SubItem::setLength, 24)
				.build();
		SubItem i2 = new SubItem();
		Fieldsetter.of((t, u)->"id").accept(i2, "01");
		Fieldsetter.of((t, u)->"name").accept(i2, "ABC");
		Fieldsetter.of((t, u)->"createAt").accept(i2, now);
		Fieldsetter.of((t, u)->"updateAt").accept(i2, now);
		Fieldsetter.of((t, u)->"username").accept(i2, "あああ");
		Fieldsetter.of((t, u)->"length").accept(i2, 24);

		Assert.assertEquals(i1, i2);
	}
}
