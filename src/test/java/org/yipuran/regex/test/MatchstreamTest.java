package org.yipuran.regex.test;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.yipuran.regex.MatcherStream;

/**
 * MatchstreamTest.java
 */
public class MatchstreamTest{

	public static void main(String[] args){
		String string = "aaa5Hello 120 yipuran 30 core 76 test";

		System.out.println("---------------");
		MatcherStream.find(Pattern.compile("[0-9]+"), string).forEach(e->{
			System.out.println("["+e+"]");
		});
		System.out.println("---------------");
		MatcherStream.findMatches(Pattern.compile("[0-9]+"), string).forEach(m->{
			System.out.println("["+m.group()+"]");
		});
		System.out.println("---------------");
		String result = matchReplace(string, "[0-9]+", (m, i)->{
			return "{" + i + "}";
		});
		System.out.println("result = [" + result + "]");
	}
	public static String matchReplace(String string, String regex, BiFunction<MatchResult, Integer, String> f) {
		AtomicInteger i = new AtomicInteger(0);
		AtomicInteger x = new AtomicInteger(0);
		return Stream.concat(	MatcherStream.findMatches(Pattern.compile("[0-9]+"), string)
		.collect(()->new ArrayList<String>(), (r, t)->{
			r.add( string.substring(i.getAndSet(t.end()), t.start()) + f.apply(t, x.getAndIncrement())  );
		},(r, u)->{}).stream(), Stream.of(string.substring(i.get())))
		.collect(Collectors.joining());
	}

}
