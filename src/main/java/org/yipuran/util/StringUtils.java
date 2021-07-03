package org.yipuran.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * StringUtils：String ユーティリティ.
 */
public final class StringUtils{
	/** private constructor. */
	private StringUtils(){}

	/**
	 * InputStream → String 変換.
	 * <PRE>
	 * 呼出し後に InputStream は、close されることに注意
	 * </PRE>
	 * @param in InputStream
	 * @return String
	 * @throws IOException
	 * @since 4.23
	 */
	public static String toString(InputStream in) throws IOException{
		StringBuilder sb = new StringBuilder();
		try(BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"))){
			String line;
			while((line = br.readLine()) != null){
				sb.append(line);
			}
		}
		return sb.toString();
	}
	/**
	 * InputStream → String 変換（文字コード指定）.
	 * <PRE>
	 * 呼出し後に InputStream は、close されることに注意
	 * </PRE>
	 * @param in InputStream
	 * @param charset 文字コード名
	 * @return String
	 * @throws IOException
	 * @since 4.23
	 */
	public static String toString(InputStream in, String charset) throws IOException{
		StringBuilder sb = new StringBuilder();
		try(BufferedReader br = new BufferedReader(new InputStreamReader(in, charset))){
			String line;
			while((line = br.readLine()) != null){
				sb.append(line);
			}
		}
		return sb.toString();
	}
	/**
	 * String → Byte InputStream
	 * @param str 読込み対象文字列
	 * @return InputStream
	 * @since 4.23
	 */
	public static InputStream toByteStream(String str){
		return new ByteArrayInputStream(str.getBytes());
	}
	/**
	 * Unicode文字列→文字列変換.
	 * ("\u3042" -> "あ")
	 * convertToOiginal("\\u2611") → ☑
	 * convertToOiginal("\\u2610") → ☐
	 * @param unicode Unicode文字列
	 * @return UTF8文字列
	 */
	public static String unicodeToUtf8(String unicode){
		String[] codeStrs = unicode.split("\\\\u");
		int[] codePoints = new int[codeStrs.length - 1];
		for (int i = 0; i < codePoints.length; i++){
			codePoints[i] = Integer.parseInt(codeStrs[i + 1], 16);
		}
		String encodedText = new String(codePoints, 0, codePoints.length);
		return encodedText;
	}
	/**
	 * UTF8文字列→Unicode文字列変換.
	 * ("あ" -> "\u3042")
	 * @param str UTF8文字列
	 * @return Unicode文字列 "\\uXXXX"
	 */
	public static String utf8ToUnicode(String str){
		StringBuilder sb = new StringBuilder();
		for(int i=0; i < str.length(); i++){
			sb.append(String.format("\\u%04X", Character.codePointAt(str, i)));
		}
		String unicode = sb.toString();
		return unicode;
	}
   /**
    * ２バイト文字または制御文字を含むか判定.
    * @param str 検査文字列
    * @return true=含む
    */
   public static boolean includeMetaChractoer(String str){
		for(char c : str.toCharArray()){
			if (c < 0x20 || 0x7f < c) return true;
		}
		return false;
   }
	/**
	 * 全角数字→半角数字変換.
	 * @param str 対象文字列
	 * @return 変換後文字列
	 */
	public static String zenNumberTohanNumber(String str){
		if (str==null) return str;
		StringBuilder sb = new StringBuilder(str);
		for (int i = 0; i < str.length(); i++){
			char c = str.charAt(i);
			if ('０' <= c && c <= '９'){
				sb.setCharAt(i, (char) (c - '０' + '0'));
			}
		}
		return sb.toString();
	}
	/**
	 * 半角数字→全角数字変換
	 * @param str 半角数字を含む文字列
    * @return 半角数字→全角数字に変換された文字列
	 * @return
	 */
	public static String hankakuNumberToZenkaku(String str){
		if (str==null) return null;
		StringBuffer sb = new StringBuffer(str);
		for(int i=0; i < str.length(); i++){
			char c = str.charAt(i);
			if (c >= '0' && c <= '9'){
				sb.setCharAt(i, (char)(c - '0' + '０'));
			}else if(c == '-'){
				sb.setCharAt(i, '－');
			}
		}
		return sb.toString();
	}
	/**
	 * 印刷文字列長の取得.
	 * @param str 対象文字列
	 * @return 印字（表示）としての長さ、全角＝２、半角＝１で計算
	 */
	public static int printLength(String str){
		if (str==null) return 0;
		Pattern hankaku = Pattern.compile("[ -~｡-ﾟ]");
		int n = 0;
		for(String s:str.split("")){
			if ("".equals(s)) continue;
			if (hankaku.matcher(s).matches()){
				n++;
			}else{
				n += 2;
			}
		}
		return n;
	}
	/**
	 * メール正規表現チェック
	 */
   public static boolean isMailExpress(String str){
		Pattern ptn = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*((\\.[A-Za-z]{2,}){1}$)");
		Matcher matcher = ptn.matcher(str);
		return matcher.matches() ? true : false;
   }
	/**
	 * ファイルPath禁則文字変換.
	 *  \ / : * ? " < > |  文字を全て全角にする。" "空白文字は除かれる.
	 * @param str 対象文字列
	 * @return 結果
	 */
	public static String changeDownloadPathProhibition(String str){
		if (str==null) return null;
		return str.replaceAll("\\\\", "￥").replaceAll("/", "／").replaceAll(":", "：").replaceAll("\\*", "＊").replaceAll("\\?", "？").replaceAll("\"", "”")
				.replaceAll("<", "＜").replaceAll(">", "＞").replaceAll("\\|", "｜").replaceAll(" ", "").replaceAll("\\,", "，")
				.replaceAll("\\(", "（").replaceAll("\\)", "）").replaceAll("\\+", "＋").replaceAll("\\-", "－").replaceAll("\\&", "＆");
	}
}
