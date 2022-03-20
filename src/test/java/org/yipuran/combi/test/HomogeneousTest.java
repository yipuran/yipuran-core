package org.yipuran.combi.test;

import java.util.Arrays;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.yipuran.util.pch.Homogeneous;

/**
 * HomogeneousTest.java
 */
public class HomogeneousTest{

	public static void main(String[] args){
		List<String> list = Arrays.asList("A", "B", "C", "D");

		Homogeneous<String> h = Homogeneous.of(list);
		h.compute(3).stream().map(e->e.stream().collect(Collectors.joining(""))).forEach(System.out::println);

		System.out.println("---------------");

		Spliterator<List<String>> spliterator = Spliterators.spliteratorUnknownSize(h.iterator(3), 0);
		Stream<List<String>> stream = StreamSupport.stream(spliterator, false);
		stream.map(e->e.stream().collect(Collectors.joining(""))).forEach(System.out::println);


	}

}
