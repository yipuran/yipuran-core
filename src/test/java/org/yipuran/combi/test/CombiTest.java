package org.yipuran.combi.test;

import java.util.Arrays;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.yipuran.util.pch.Combinations;

/**
 * CombiTest.java
 */
public class CombiTest{

	public static void main(String[] args){
		List<String> list = Arrays.asList("A", "B", "C", "D");

		Combinations<String> c = Combinations.of(list);
		c.compute(3).stream().map(e->e.stream().collect(Collectors.joining(""))).forEach(System.out::println);

		System.out.println("---------------");

		Spliterator<List<String>> spliterator = Spliterators.spliteratorUnknownSize(c.iterator(3), 0);
		Stream<List<String>> stream = StreamSupport.stream(spliterator, false);
		stream.map(e->e.stream().collect(Collectors.joining(""))).forEach(System.out::println);

		System.out.println("---------------");

		String[] ary = new String[]{"A", "B", "C", "D"};
		c = Combinations.of(ary);
		c.compute(3).stream().map(e->e.stream().collect(Collectors.joining(""))).forEach(System.out::println);

	}

}
