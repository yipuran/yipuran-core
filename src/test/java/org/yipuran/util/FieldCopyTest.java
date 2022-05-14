package org.yipuran.util;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.yipuran.util.test.Item;

@RunWith(JUnit4.class)
public class FieldCopyTest {
	private Item it1;
	private Item it2;

	@Before
	public void before() {
		it1 = GenericBuilder.of(Item::new).with(Item::setId, "01").with(Item::setName, "ABC")
				.with(Item::setCreateAt, LocalDateTime.now())
				.with(Item::setUpdateAt, LocalDateTime.now()).build();
		it2 = GenericBuilder.of(Item::new).with(Item::setId, "01").with(Item::setName, "ABC")
				.build();
	}
	@Test
	public void single() {
		Assert.assertTrue(!it1.equals(it2));

		FieldCopy.of(t->"createAt", it2).accept(it1);
		Assert.assertTrue(!it1.equals(it2));

		FieldCopy.of(t->"updateAt", it2).accept(it1);
		Assert.assertTrue(it1.equals(it2));
	}
	@Test
	public void arrayTest() {
		Assert.assertTrue(!it1.equals(it2));

		FieldArrayCopy.of(t->new String[] { "createAt", "updateAt" }, it2).accept(it1);

		Assert.assertTrue(it1.equals(it2));
	}
	@Test
	public void listTest() {
		Assert.assertTrue(!it1.equals(it2));

		List<String> list = new ArrayList<>();
		list.add("createAt");
		list.add("updateAt");
		FieldListCopy.of(t->list, it2).accept(it1);

		Assert.assertTrue(it1.equals(it2));
	}

}
