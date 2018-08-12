package org.yipuran.util;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Spliterators;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * HEXダンプ処置.
 * <PRE>
 * 文字列、String インスタンスを指定してHEXダンプのリストもしくは Stream を取得する。
 * リストもしくは Streamでダンプする単位は、１６バイト固定である。
 *
 * （呼出し方法）
 *      List<String> list = DumpHex.of(data).list();
 *
 *      DumpHex.of(data).foEach(s->{
 *          // s is 16byte pack HEX dump
 *      }):
 *
 * </PRE>
 * @since 4.3
 */
public final class DumpHex{
	private String str;
	private static String spacer = "                                  ";
	private DumpHex(String str){
		this.str = str;
	}
	/**
	 * DumpHex インスタンス生成.
	 * @param str ダンプ対象の String
	 * @return DumpHex
	 */
	public static DumpHex of(String str){
		return new DumpHex(str);
	}
	/**
	 * String Supplier → DumpHex インスタンス生成.
	 * @param supplier ダンプ対象 Supplier&lt;String&gt;
	 * @return DumpHex
	 */
	public static DumpHex of(Supplier<String> supplier){
		return new DumpHex(supplier.get());
	}
	/**
	 * １６バイトずつのHEXダンプ結果Stream取得
	 * @return Stream&lt;String&gt;
	 */
	public Stream<String> stream(){
		byte[] bs = str.getBytes();
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(BytepackIterator.of(bs, 16), 0), false)
		.map(b->{
			StringBuilder sb = new StringBuilder();
			for(int x=0;x < b.length;x++){
				if (x > 0 && x % 4 == 0) sb.append(" ");
				sb.append(String.format("%02x", b[x]));
			}
			String hexstring = sb.toString();
			if (35 > hexstring.length()){
				hexstring += spacer.substring(0, 35 - hexstring.length());
			}
			for(int n=0;n < b.length;n++){
				if (b[n] < 0){
					b[n] = 0x3f;
				}else if(b[n] < 0x20){
					b[n] = 0x2e;
				}
			}
			String ss = new String(b);
			return hexstring + "  [" + ss + "]";
		});
	}
	/**
	 * １６バイトずつのHEXダンプ結果リスト取得,
	 * @return
	 */
	public List<String> list(){
		return stream().collect(Collectors.toList());
	}
	/**
	 * １バイト１６進文字列生成.
	 * <PRE>
	 * ByteArrayInputStream で読み取って生成するので巨大な String に対して様々な目的の
	 * 呼出し使用の可能性があるメソッド
	 * </PRE>
	 * @param splitstring 区切り文字
	 * @return 区切り文字指定１バイト１６進文字 %02x で変換した結果を連結した文字列を返す。
	 */
	public String hexstring(String splitstring){
		ByteArrayInputStream inst = new ByteArrayInputStream(str.getBytes());
		return Stream.generate(inst::read).limit(inst.available())
		.map(e->String.format("%02x", e)).collect(Collectors.joining(splitstring));
	}

}
