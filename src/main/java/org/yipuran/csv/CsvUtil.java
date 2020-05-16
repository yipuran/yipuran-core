package org.yipuran.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
	 * ＣＳＶ→List&lt;String&gt;
	 * 対象ＣＳＶサイズが膨大なサイズである場合は、使用すべきでない。
	 * @param filepath ＣＳＶファイルパス
	 * @param quotSwitch true=括り文字あり
	 * @return String[] リスト
	 * @throws IOException
	 */
	@Deprecated
	public static List<String[]> parse(String filepath, boolean quotSwitch) throws IOException{
		File file = new File(filepath);
		if (!file.exists() || file.isDirectory()){
			throw new FileNotFoundException("File Not Found ["+filepath+"]");
		}
		List<String[]> rtn = new ArrayList<String[]>();
		try(BufferedReader br = new BufferedReader(new FileReader(file))){
			String line;
			if (quotSwitch){
				while((line=br.readLine())!=null){
					String[] strs = splitAry(',',line);
					for(int i=0;i < strs.length;i++){
						if (strs[i].length() > 1){
							strs[i] = strs[i].substring(1,strs[i].length()-1);
						}
					}
					rtn.add(strs);
				}
			}else{
				while((line=br.readLine())!=null){
					rtn.add(splitAry(',',line));
				}
			}
		}
		return rtn;
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

}
