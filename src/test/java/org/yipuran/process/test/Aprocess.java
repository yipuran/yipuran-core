package org.yipuran.process.test;

import org.yipuran.util.process.AbstractProcessExec;

/**
 * Aprocess.java
 */
public class Aprocess  extends AbstractProcessExec{
	@Override
	public String arrange(){
		return "python c:/work/hello.py";
	}

}
