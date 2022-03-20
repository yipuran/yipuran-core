package org.yipuran.util;

import java.io.Serializable;
import java.util.Iterator;
import java.util.function.Supplier;

/**
 * byte配列 Packイテレータ.
 * <PRE>
 * 指定するバイト数で、バイト配列のイテレータを生成する。
 * 生成は、byte[]
 * </PRE>
 * @since 4.3
 */
public class BytepackIterator implements Iterator<byte[]>, Serializable{
	private byte[] b;
	private boolean next;
	private long seek = 0;
	private int blength = 16;

	private BytepackIterator(byte[] b, int len){
		if (len < 1) new IllegalArgumentException("pack length must be greater than 0");
		this.b = b;
		blength = len;
		next = b != null && b.length > 0 ? true : false;
	}
	/**
	 * バイト配列→バイト配列イテレータ
	 * @param bytes バイト配列
	 * @param len イテレータで返して欲しい byte[] の長さ
	 * @return BytepackIterator byte[] の Iterator で Serializable
	 */
	public static BytepackIterator of(byte[] bytes, int len){
		return new BytepackIterator(bytes, len);
	}
	/**
	 * 文字列→バイト配列イテレータ
	 * @param str  byte[]に分割したい文字列
	 * @param len イテレータで返して欲しい byte[] の長さ
	 * @return BytepackIterator byte[] の Iterator で Serializable
	 */
	public static BytepackIterator of(String str, int len){
		return new BytepackIterator(str.getBytes(), len);
	}
	/**
	 * Supplier 指定バイト配列イテレータ
	 * @param arysupplier  byte[]を返すSupplier
	 * @param len イテレータで返して欲しい byte[] の長さ
	 * @return BytepackIterator byte[] の Iterator で Serializable
	 */
	public static BytepackIterator of(Supplier<byte[]> arysupplier, int len){
		return new BytepackIterator(arysupplier.get(), len);
	}

	@Override
	public boolean hasNext(){
		return next;
	}
	@Override
	public byte[] next(){
		if (next){
			long n = seek + blength;
			if ( n < b.length ){
				byte[] r = new byte[blength];
				for(int i=0;i < blength;i++){
					r[i] = b[Long.valueOf(seek).intValue()];
					seek++;
				}
				next = seek < b.length;
				return r;
			}
			byte[] r = new byte[ Long.valueOf(blength - (n - b.length)).intValue() ];
			for(int i=0;i < r.length;i++){
				r[i] = b[Long.valueOf(seek).intValue()];
				seek++;
			}
			next = false;
			return r;
		}
		return null;
	}

}
