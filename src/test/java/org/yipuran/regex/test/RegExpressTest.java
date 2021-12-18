package org.yipuran.regex.test;

import org.yipuran.regex.RegExpress;

/**
 * RegExpressTest.java
 */
public class RegExpressTest{
	public static void main(String[] args){
		String string = "abc5Hello 120 yipuran 30 core 76 test";

		RegExpress.matchToStream("[0-9]+", string).forEach(e->System.out.println("["+e+"]"));
		System.out.println("------------------");

		RegExpress.findMatches("[0-9]+", string).forEach(m->System.out.println("["+m.group()+"] start="+m.start()+" end="+m.end()));
		System.out.println("------------------");

		RegExpress.resultMatch("[0-9]+", string, m->{
			System.out.println("["+m.group()+"] start="+m.start()+" end="+m.end());
		});

		System.out.println("------------------");
		RegExpress.resultMatch("[0-9]+", string, (m, i)->{
			System.out.println(i+":["+m.group()+"] start="+m.start()+" end="+m.end());
		});

		System.out.println("------------------");
		String res = RegExpress.replace("[0-9]+", string, (e, i)->"{"+i+":"+e+"}");
		System.out.println(res);
	}

}
