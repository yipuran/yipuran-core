package org.yipuran.pch.test;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.yipuran.util.pch.RepeatablePermutation;

/**
 * PermutationTest.java
 */
public class PermutationTest{

	public static void main(String[] args){
		RepeatablePermutation<String> r = RepeatablePermutation.of(Arrays.asList("A", "B", "C"));
		r.compute(3).stream()
		.map(e->e.stream().collect(Collectors.joining("")))
		.forEach(System.out::println);

	}

}
