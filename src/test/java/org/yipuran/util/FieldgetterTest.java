package org.yipuran.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.yipuran.util.test.Item;
import org.yipuran.util.test.SubItem;


@RunWith(JUnit4.class)
public class FieldgetterTest {


	@Test
	public void test1() {
		Item i1 = GenericBuilder.of(Item::new).with(Item::setId, "01")
				.with(Item::setName, "ABC")
				.build();

		Assert.assertEquals(i1.getId(), Fieldgetter.of(t->"id").apply(i1));

		SubItem i2 = GenericBuilder.of(SubItem::new).with(Item::setId, "01")
				.with(Item::setName, "ABC").with(SubItem::setUsername, "あああ").with(SubItem::setLength, 24)
				.build();

		Assert.assertEquals(i2.getId(), Fieldgetter.of(t->"id").apply(i2));
		Assert.assertEquals(i2.getUsername(), Fieldgetter.of(t->"username").apply(i2));
		Assert.assertEquals(i2.getLength(), Fieldgetter.of(t->"length").apply(i2));

		Assert.assertEquals("01", Fieldgetter.of(t->"id").apply(i2));
		Assert.assertEquals("あああ", Fieldgetter.of(t->"username").apply(i2));
		Assert.assertEquals(24, Fieldgetter.of(t->"length").apply(i2));

	}
}
