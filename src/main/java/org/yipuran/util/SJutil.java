package org.yipuran.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * ユーティリティ
 */
public final class SJutil{
	private static final String HORIZONTAL_BAR = new Character((char)0x2014).toString();
	private static final String WAVE_DASH = new Character((char)0x301C).toString();
	private static final String DOUBLE_VERTICAL_LINE = new Character((char)0x2016).toString();
	private static final String MINUS_SIGN = new Character((char)0x2212).toString();
	private static final String CENT_SIGN = new Character((char)0x00A2).toString();
	private static final String POND_SIGN = new Character((char)0x00A3).toString();
	private static final String NOT_SIGN = new Character((char)0x00AC).toString();
	private static final String NON_CONVERT_CHAR = new Character((char)0xFFFD).toString();

	private SJutil(){}
	/**
	 * printf String を返す
	 * @param fmt %d %s %02x 等のフォーマット
	 * @param o 対象配列
	 * @return printf結果
	 */
	public static String printf(String fmt,Object...o){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try(PrintStream p = new PrintStream(baos)){
			p.printf(fmt,o);
		}
		return baos.toString();
	}
	/**
	 * 右詰めPadding（半角空白文字を右に詰める）
	 * @param s 対象文字列
	 * @param len 長さ（byte)
	 * @return 右詰めPadding結果
	 */
	public static String rightPad(String s,int len){
		if (len <= 0) return "";
		if (s==null){
			char[] ch = new char[len];
		   for(int i=0;i < len;i++){
		      ch[i] = ' ';
		   }
		   return new String(ch);
		}
		byte[] b = s.getBytes();
		if (len==b.length) return s;
		if (len < b.length){
			return new String(b,0,len);
		}
		char[] ch = new char[len-b.length];
		for(int i=0;i < ch.length;i++){
			ch[i] = ' ';
		}
		return s + new String(ch);
	}
	/**
	 * 左詰めPadding（半角空白文字を左に詰める）
	 * @param s 対象文字列
	 * @param len 長さ（byte)
	 * @return 左詰めPadding結果
	 */
	public static String leftPad(String s,int len){
		if (len <= 0) return "";
		if (s==null){
			char[] ch = new char[len];
			for(int i=0;i < len;i++){
				ch[i] = ' ';
			}
			return new String(ch);
		}
		byte[] b = s.getBytes();
		if (len==b.length) return s;
		if (len < b.length){
			return new String(b,0,len);
		}
		char[] ch = new char[len-b.length];
		for(int i=0;i < ch.length;i++){
			ch[i] = ' ';
		}
		return new String(ch) + s;
   }
	/**
	 * 右詰めPadding（指定文字 c を右に詰める）
	 * @param s 対象文字列
	 * @param len 長さ（byte)
	 * @param c 指定文字
	 * @return 右詰めPadding結果
	 */
	public static String rightPad(String s,int len,char c){
		if (len <= 0) return "";
		if (s==null){
			char[] ch = new char[len];
			for(int i=0;i < len;i++){
				ch[i] = c;
			}
			return new String(ch);
		}
		byte[] b = s.getBytes();
		if (len==b.length) return s;
		if (len < b.length){
			return new String(b,0,len);
		}
		char[] ch = new char[len-b.length];
		for(int i=0;i < ch.length;i++){
			ch[i] = c;
		}
		return s + new String(ch);
	}
	/**
	 * 左詰めPadding（指定文字 c を左に詰める）
	 * @param s 対象文字列
	 * @param len 長さ（byte)
	 * @param c 指定文字
	 * @return 左詰めPadding結果
	 */
	public static String leftPad(String s,int len,char c){
		if (len <= 0) return "";
		if (s==null){
			char[] ch = new char[len];
			for(int i=0;i < len;i++){
				ch[i] = c;
			}
			return new String(ch);
		}
		byte[] b = s.getBytes();
		if (len==b.length) return s;
		if (len < b.length){
			return new String(b,0,len);
		}
		char[] ch = new char[len-b.length];
		for(int i=0;i < ch.length;i++){
			ch[i] = c;
		}
		return new String(ch) + s;
	}
	/**
	 * 文字化け対応
	 * @param str 対象文字列
	 * @return 変換済文字列
	 */
	public static String excludeNonConv(String str){
		String rtn = str;
	   if (str != null && str.length() > 0) {
	   	rtn = rtn.replaceAll(HORIZONTAL_BAR, "―");
			rtn = rtn.replaceAll(WAVE_DASH, "～");
			rtn = rtn.replaceAll(DOUBLE_VERTICAL_LINE, "∥");
			rtn = rtn.replaceAll(MINUS_SIGN, "－");
			rtn = rtn.replaceAll(CENT_SIGN, "￠");
			rtn = rtn.replaceAll(POND_SIGN, "￡");
			rtn = rtn.replaceAll(NOT_SIGN, "￢");
			rtn = rtn.replaceAll(NON_CONVERT_CHAR, "?");
	   }
	   return rtn;
	}

	/**
	 * 文字列詰替.
	 * @param s 対象文字列
	 * @param max 長さ（byte)
	 * @return 詰替済文字列、maxを超える文字は切り捨てられる。
	 */
	public static String packStr(String s,int max){
		StringBuffer sb = new StringBuffer();
		int cnt=0;
		for(int i=0;i < s.length();i++){
			String _s = s.substring(i,i+1);
			int k = _s.getBytes().length;
			if ((k+cnt) > max){
				break;
			}
			sb.append(_s);
			cnt += k;
		}
		return sb.toString();
	}
	/**
	 * 全角数字→半角数字変換
	 * @param str 全角数字を含む文字列
	 * @return 全角数字→半角数字に変換された文字列
	 */
	public static String zenkakuNumberToHankaku(String str){
		if (str==null) return null;
		StringBuffer sb = new StringBuffer(str);
		for(int i=0;i < sb.length();i++){
			char c = sb.charAt(i);
			if (c >= '０' && c <= '９'){
				sb.setCharAt(i, (char)(c - '０' + '0'));
			}else if(c == '－'){
				sb.setCharAt(i, '-');
			}
		}
		return sb.toString();
	}

	/**
	 * byte[]→16進数表現.  %02x でフォーマット
	 * @param bytes
	 * @return
	 */
	public static String toHexString(byte[] bytes){
		StringBuffer sb = new StringBuffer();
		for(byte b : bytes){
			sb.append(String.format("%02x",b));
		}
		return sb.toString();
	}
   /**
    * 16進数表現→byte[]
    * @param hex %02x フォーマット
    * @return
    */
	public static byte[] hexToBytes(String hex) {
		byte[] b = new byte[hex.length() / 2];
		for(int i=0;i < b.length;i++){
			b[i] = (byte) Integer.parseInt(hex.substring(i * 2,(i+1)*2),16);
		}
		return b;
	}
	/**
	 * 62進数変換 String → long
	 * Long.MAX_VALUE          = 9223372036854775807 → aZl8N0y58M7
	 * Integer.Long.MAX_VALUE  = 2147483647          → 2lkCB1
	 * @param value
	 * @return
	 */
   public static long str62Tolong(String s){
		char[] c = s.toCharArray();
		long rtn=0;
		long k=0;
		for(int i=c.length-1;i > -1;i--){
			if ('0' <= c[i] && c[i] <= '9'){
				rtn += (c[i] - '0') * Math.pow(62L,k);
			}else if('a' <= c[i] && c[i] <= 'z'){
				rtn += (c[i] - 'a' + 10) * Math.pow(62L,k);
			}else if('A' <= c[i] && c[i] <= 'Z'){
				rtn += (c[i] - 'A' + 36) * Math.pow(62L,k);
			}
			k++;
		}
		return rtn;
	}
	/**
	 * 62進数変換 long → String
	 * Long.MAX_VALUE          = 9223372036854775807 → aZl8N0y58M7
	 * Integer.Long.MAX_VALUE  = 2147483647          → 2lkCB1
	 * @param value
	 * @return
	 */
	public static String long62String(long value){
		if (value==0) return "0";
		long v = value;
		StringBuilder sb = new StringBuilder();
		while(v > 0){
			int i = (int)(v % 62);
			if (i < 10){
				sb.append(i);
			}else if(i < 36){
				i += 'a' - 10;
				sb.append((char)i);
			}else{
				i += 'A' - 36;
				sb.append((char)i);
			}
			v = v / 62;
		}
		return new String(sb.reverse());
   }
	/**
	 * 36進数変換 long → String
	 * Long.MAX_VALUE          = 9223372036854775807 → 1y2p0ij32e8e7
	 * Integer.Long.MAX_VALUE  = 2147483647          → zik0zj
	 * @param value
	 * @return
	 */
	public static String long36String(long value){
		if (value==0) return "0";
		long v = value;
		StringBuilder sb = new StringBuilder();
		while(v > 0){
			int i = (int)(v % 36);
			if (i < 10){
				sb.append(i);
			}else{
				i += 'a' - 10;
				sb.append((char)i);
			}
			v = v / 36;
		}
		return new String(sb.reverse());
   }
	/**
	 * 36進数変換 String → long
	 * Long.MAX_VALUE          = 9223372036854775807 → 1y2p0ij32e8e7
	 * Integer.Long.MAX_VALUE  = 2147483647          → zik0zj
	 * @param value
	 * @return
	 */
	public static long str36long(String s){
		char[] c = s.toCharArray();
		long rtn=0;
		long k=0;
		for(int i=c.length-1;i > -1;i--){
			if ('0' <= c[i] && c[i] <= '9'){
				rtn += (c[i] - '0') * Math.pow(36L,k);
			}else if('a' <= c[i] && c[i] <= 'z'){
				rtn += (c[i] - 'a' + 10) * Math.pow(36L,k);
			}
			k++;
		}
		return rtn;
	}
	/**
	 * クラス指定、対象ファイル読み込みＵＲＩの取得.
	 * 取得したURI で、File インスタンス生成などの目的用途に使う
	 * @param cls nameで指定するファイルが、clsクラスと同じ階層に存在すること
	 * @param name ファイル名
	 * @return
	 */
	public URI getURI(Class<?> cls,String name){
		try{
			return cls.getClassLoader().getResource(cls.getPackage().getName().replaceAll("\\.","/")+"/"+name).toURI();
		}catch(URISyntaxException e){
			throw new RuntimeException(e);
		}
	}
	static String getCRcode(){
		return java.io.File.pathSeparatorChar==';' ? "\r\n" : "\n";
	}
	static String CRCD = getCRcode();
	/**
	 * Exceptionスタックトレースメッセージ文字列取得
	 * @param e 発生したException
	 * @return e.printStracktrace()と同じ結果の文字列を返す。
	 */
	public static String exceptionTrace(Throwable e){
		StringBuffer sb = new StringBuffer(e.getClass().getName());
		sb.append(":");
		sb.append(e.getMessage());
		sb.append(CRCD);
		for(StackTraceElement ste : e.getStackTrace()){
			sb.append("\tat ");
			sb.append(ste.toString());
			sb.append(CRCD);
		}
		return sb.toString();
	}
	/**
	 * Unicode文字列に変換する("あ" → "\u3042")
	 * @param string 変換対象文字列 nullを指定すると、空文字が返る。
	 * @return 変換結果 Unicode文字列
	 * @since Ver 4.16
	 */
	public static String toUnicode(String string){
		if (string==null || string.isEmpty()) return "";
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < string.length(); i++) {
			sb.append(String.format("\\u%04X", Character.codePointAt(string, i)));
		}
		String unicode = sb.toString();
		return unicode;
	}

	/**
	 * Unicode文字列から元の文字列に変換する ("\u3042" → "あ")
	 * @param unicode Unicode文字列
	 * @return 変換結果 UTF-8 文字列
	 * @since Ver 4.16
	 */
	public static String fromUnicode(String unicode){
		if (unicode==null || unicode.isEmpty()) return "";
		String[] codeStrs = unicode.split("\\\\u");
		int[] codePoints = new int[codeStrs.length - 1]; // 最初が空文字なのでそれを抜かす
		for (int i = 0; i < codePoints.length; i++) {
			codePoints[i] = Integer.parseInt(codeStrs[i + 1], 16);
		}
		String encodedText = new String(codePoints, 0, codePoints.length);
		return encodedText;
	}
}
