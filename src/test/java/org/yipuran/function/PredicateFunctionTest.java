package org.yipuran.function;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

public class PredicateFunctionTest {
	@Test
	public void simple() {
		String str = "2021/04/01";
		LocalDate date = PredicateFunction.of(
				   t->Pattern.compile("^\\d{4}/(0[1-9]|1[012])/(0[1-9]|[12][0-9]|3[01])$")
				      .matcher(t==null ? "" :t.toString()).matches()
				   , e->LocalDate.parse(e.toString(), DateTimeFormatter.ofPattern("yyyy/MM/dd"))
				  // , LocalDate.of(2021, 4, 1)
				).apply(str);
		Assert.assertThat( LocalDate.of(2021, 4, 1), CoreMatchers.is(date));

		str = "20210401";
		date = PredicateFunction.of(
				   t->Pattern.compile("^\\d{4}/(0[1-9]|1[012])/(0[1-9]|[12][0-9]|3[01])$")
				      .matcher(t==null ? "" :t.toString()).matches()
				   , e->LocalDate.parse(e.toString(), DateTimeFormatter.ofPattern("yyyy/MM/dd"))
				  // , LocalDate.of(2021, 4, 1)
				).apply(str);
		Assert.assertEquals(null, date);
	}
	@Test
	public void simple2() {
		String str = "2021/04/01";
		LocalDate date = PredicateFunction.of(
				   t->Pattern.compile("^\\d{4}/(0[1-9]|1[012])/(0[1-9]|[12][0-9]|3[01])$")
				      .matcher(t==null ? "" :t.toString()).matches()
				   , e->LocalDate.parse(e.toString(), DateTimeFormatter.ofPattern("yyyy/MM/dd"))
				   , LocalDate.of(2021, 4, 1)
				).apply(str);
		Assert.assertThat( LocalDate.of(2021, 4, 1), CoreMatchers.is(date));

		str = "20210401";
		date = PredicateFunction.of(
				   t->Pattern.compile("^\\d{4}/(0[1-9]|1[012])/(0[1-9]|[12][0-9]|3[01])$")
				      .matcher(t==null ? "" :t.toString()).matches()
				   , e->LocalDate.parse(e.toString(), DateTimeFormatter.ofPattern("yyyy/MM/dd"))
				   , LocalDate.of(2021, 4, 1)
				).apply(str);
		Assert.assertThat( LocalDate.of(2021, 4, 1), CoreMatchers.is(date));
	}
	@Test
	public void simple3() {
		String str = "2021/04/01";
		LocalDate date = PredicateFunction.of(
				   t->Pattern.compile("^\\d{4}/(0[1-9]|1[012])/(0[1-9]|[12][0-9]|3[01])$")
				      .matcher(t==null ? "" :t.toString()).matches()
				   , e->LocalDate.parse(e.toString(), DateTimeFormatter.ofPattern("yyyy/MM/dd"))
				   , ()->LocalDate.of(2021, 4, 1)
				).apply(str);
		Assert.assertThat( LocalDate.of(2021, 4, 1), CoreMatchers.is(date));

		str = "20210401";
		date = PredicateFunction.of(
				   t->Pattern.compile("^\\d{4}/(0[1-9]|1[012])/(0[1-9]|[12][0-9]|3[01])$")
				      .matcher(t==null ? "" :t.toString()).matches()
				   , e->LocalDate.parse(e.toString(), DateTimeFormatter.ofPattern("yyyy/MM/dd"))
				   , ()->LocalDate.of(2021, 4, 1)
				).apply(str);
		Assert.assertThat( LocalDate.of(2021, 4, 1), CoreMatchers.is(date));
	}

}
