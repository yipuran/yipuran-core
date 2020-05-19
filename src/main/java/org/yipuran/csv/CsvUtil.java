package org.yipuran.csv;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * ＣＳＶユーティリティ.
 * <pre>
 * 文字列split（列の縮小を回避）、List&lt;String&gt; → CSV 1 Line ダブルクオート
 * String... → CSV 1 Line ダブルクオート
 * などの処理
 * </pre>
 * @since 1.1.2
 */
public final class CsvUtil{
	private CsvUtil(){}

	/**
	 * List&lt;String&gt; → CSV 1 Line ダブルクオート
	 * @param list List&lt;String&gt;
	 * @return csv 1line string
	 */
	public static String csvline(List<String> list){
		StringBuilder sb = new StringBuilder();
		for(Iterator<String> it=list.iterator();it.hasNext();){
			sb.append("\"");
			sb.append(it.next().replaceAll("\"","\"\""));
			sb.append("\"");
			if (it.hasNext()){
				sb.append(",");
			}
		}
		return sb.toString();
	}
	/**
	 * String... → CSV 1 Line ダブルクオート
	 * @param s String...
	 * @return csv 1line string
	 */
	public static String csvline(String...s){
		StringBuilder sb = new StringBuilder();
		for(int i=0;i < s.length;i++){
			sb.append("\"");
			sb.append(s[i].replaceAll("\"","\"\""));
			sb.append("\"");
			if ((i+1) < s.length){
				sb.append(",");
			}
		}
		return sb.toString();
	}

	/**
	 * 文字列split（列の縮小を回避）.
	 * <pre>
	 * 分割サンプル
	 * splitAry(',', "A,B,C")--> "A", "B", "C"
	 * splitAry(',', "A,,C") --> "A", "", "C"
	 * splitAry(',', ",,C")  --> "", "" , "C"
	 * splitAry(',', ",")    --> "", ""
	 * splitAry(',', "")     --> ""
	 * </pre>
	 * @param sep 区切り文字char
	 * @param str 対象文字列
	 * @return splitしたString配列
	 */
	public static String[] splitAry(char sep,String str){
		String[] rtn = new String[0];
		List<String> list = new ArrayList<String>();
		StringBuffer sb = new StringBuffer();
		char[] spc = { sep };
		String ns = str + new String(spc);
		char[] ch = ns.toCharArray();
		int k=0;
		for(int i=0;i < ch.length;i++){
			if (ch[i]==sep){
				list.add(sb.toString());
				sb.delete(0,k);
			}else{
				sb.append(ch[i]);
				k++;
			}
		}
		if (list.size() > 0){
			Object[] objs = list.toArray();
			rtn = new String[objs.length];
			for(int i=0;i < rtn.length;i++){
				rtn[i] = (String)objs[i];
			}
		}
		return rtn;
	}
	/**
	 * ファイルがBOM付きかチェックする。
	 * @param file 対象File
	 * @return true＝BOM付きである。
	 * @since 4.18
	 */
	public static boolean isBOMutf8(File file){
		boolean rtn = false;
		try(InputStream in = new FileInputStream(file)){
			byte[] b = new byte[3];
			int r = in.read(b);
			if (r==3) {
				if (b[0] == -17 && b[1] == -69 && b[2] == -65) rtn = true;
			}
		}catch(IOException e){
			throw new RuntimeException(e.getMessage());
		}
		return rtn;
	}
}
