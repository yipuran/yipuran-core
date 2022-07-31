package org.yipuran.util;

import org.junit.Assert;
import org.junit.Test;

public class CurlyBracketsNumTemplatorTest {

	@Test
	public void test1() {
		String temp = "abc {0} def {1} 123 {0} {2}";
		String res = CurlyBracketsNumTemplator.replace(temp, "A", 1023, "C");
		Assert.assertEquals("abc A def 1023 123 A C", res);
	}


	@Test
	public void test2() {
		CurlyBracketsNumTemplator templator = ()->"abc {0} def {1} 123 {0} {2}";
		String res = templator.replace("A", 1023, "C");
		Assert.assertEquals("abc A def 1023 123 A C", res);
	}

}
