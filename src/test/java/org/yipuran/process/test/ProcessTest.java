package org.yipuran.process.test;

import java.util.Arrays;

import org.yipuran.util.process.ProcessExecutor;
import org.yipuran.util.process.ScriptExecutor;

/**
 * ProcessTest.java
 */
public class ProcessTest{
	public static void main(String[] args){

		int sts = ScriptExecutor.run(()->"python"
		       , ()->Arrays.asList("print(\"Hello Python\")\n", "exit()\n"), t->{
		      	 System.out.println("stdout : " + t );
		       }, (t, e)->{
		          System.out.println("stderr : " + t );
		          e.printStackTrace();
		       });
		System.out.println("sts = " + sts);

		System.out.println("-----------------------");
		sts = ScriptExecutor.run(()->"cmd.exe /C python c:/work/hello.py", t->{
			System.out.println("stdout : " + t );
		}, (t, e)->{
			System.out.println("stderr : " + t );
			e.printStackTrace();
		});
		System.out.println("sts = " + sts);

		System.out.println("-----------------------");
		ProcessExecutor a = new Aprocess();

		int s = a.exec();
		String stdout = a.getStdout();
		String stderr = a.getStderr();
		System.out.println("sts = " + s );
		System.out.println("---------stdout--------------");
		System.out.println(stdout);
		System.out.println("--------stderr--------------");
		System.out.println(stderr);

	}

}
