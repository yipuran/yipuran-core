package org.yipuran.util;

import java.io.IOException;
import java.io.OutputStream;

/**
 * BOM操作クラス.
 */
public final class BOMfunction{
	/** private constructor. */
	private BOMfunction(){}

	/**
	 * BOMを出力する.
	 * @param out OutputStream
	 * @throws IOException
	 */
	public static void push(OutputStream out) throws IOException{
		out.write(new byte[]{ (byte)0xef,(byte)0xbb, (byte)0xbf });
	}

	/**
	 * BOMが先頭に付いた文字列のBOMを除去する.
	 * @param str 除去前の文字列
	 * @return 除去後の文字列
	 */
	public static String chop(String str){
		byte[] b = str.getBytes();
		if (b.length < 3) return str;
		if (b[0] != -17 || b[1] != -69 || b[2] != -65) return str;
		byte[] n = new byte[b.length-3];
		for(int i=0,k=3; i < n.length;i++, k++){
			n[0] = b[k];
		}
		return new String(n);
	}
}
