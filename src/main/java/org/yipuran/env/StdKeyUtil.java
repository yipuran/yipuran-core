package org.yipuran.env;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * キー入力ユーティリティ
 */
public final class StdKeyUtil{
	private StdKeyUtil(){}
	/**
	 * 標準キー入力.
	 * <PRE>指定するプロンプト文字列を標準出力した後に標準入力を開始する。</PRE>
	 * @param prompt プロンプト
	 * @return 標準入力された文字列
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public final static String getKeyin(String prompt) throws IOException{
		System.out.print(prompt);
		Scanner scan = new Scanner(System.in);
		return scan.nextLine();
		/*
		int iCount;
		String rtn="";
		char cStr[] = new char[256];
		InputStream inputStream=System.in;
		System.out.print(prompt);
		InputStreamReader reader=new InputStreamReader(inputStream);
		iCount=reader.read(cStr,0,255);
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<iCount;i++){
		   if (cStr[i]>=0x20) sb.append(cStr[i]);
		}
		rtn=sb.toString();
		return rtn;
		*/
	}
	/**
	 * 連続キー入力.
	 * <PRE>
	 * 文字列 List を順に標準出力 System.out.print 実行して
	 * 出力した文字列と対になるキー入力の Map を取得する。
	 * （注意：実行後のプロセスの標準入力 System.in は close する）
	 * </PRE>
	 * @param promptList 標準出力する文字列 List
	 * @return key=標準出力した文字列、value=入力文字列のMap
	 */
	public static Map<String, String> getKeyin(List<String> promptList){
		try(Scanner scan = new Scanner(System.in)){
			return promptList.stream().collect(()->new HashMap<String, String>(), (r, t)->{
				System.out.print(t);
				r.put(t, scan.nextLine());
			},(r, u)->{});
		}
	}
}
