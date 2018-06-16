package org.yipuran.env;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * キー入力ユーティリティ
 */
public final class StdKeyUtil{
	private StdKeyUtil(){}
	/**
	 * 標準キー入力.
	 * <br>入力制限＝256バイト<br>
	 * 指定するプロンプト文字列を標準出力した後に標準入力を開始する。
	 * @param prompt プロンプト
	 * @return 標準入力された文字列
	 * @throws IOException
	 */
	public final static String getKeyin(String prompt) throws IOException{
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
	}
}
