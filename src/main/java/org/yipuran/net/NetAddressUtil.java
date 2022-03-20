package org.yipuran.net;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
/**
 * Ipv4 アドレスユーティリティ.
 * <pre>
 * java.net.* を使用せず単純にIpv4 アドレスを計算、サブネットマスクによる検査等を
 * 目的に用意したユーティリティ
 * 以下のメソッドを提供
 * isScorp      : サブネットマスク検査
 * getMask      : サブネットマスク
 * minSegment   : サブネットマスクした値の最小アドレス
 * maxSegment   : サブネットマスクした値の最大アドレス
 * countSegment : マスク長で有効数を算出
 * addrTobyte   : ＩＰアドレス文字列、"xxx.xxx.xxx.xxx" → byte[].
 * byteTointAry : byte[] → int[]
 * </pre>
 */
public final class NetAddressUtil{
	private NetAddressUtil(){}
	/**
	 * Long型 ＩＰアドレス→InetAddress 変換
	 *
	 * 【注意】arms.util.IPUtil.inetNtoa に合わせて、String が逆になってるので、途中のbyte[] 配列格納は逆
	 *
	 * @param longaddr
	 * @return
	 * @throws UnknownHostException
	 */
	public static InetAddress longToInetAddress(Long longaddr) throws UnknownHostException{
		if (longaddr==null) return null;
		Long lt = longaddr;
		byte[] b = new byte[4];
		int k=3;
		for(int i=0;i < 4;i++){
			b[k] = (byte)(lt % 256L);
			lt = lt / 256L;
			k--;
		}
		return InetAddress.getByAddress(b);
	}
	/**
	 * ＩＰアドレス文字列、"xxx.xxx.xxx.xxx" → long 値.
	 * @param s ＩＰアドレス文字列
	 * @return long 値
	 */
	public static long addrTolong(String s){
		String[] sp = s.split("\\.");
		byte[] b = new byte[4];
		for(int i=0;i < 4;i++){
		   b[i] = (byte)(Integer.parseInt(sp[i]));
		}
		BigInteger bigint = new BigInteger(1,b);
		return bigint.longValue();
	}
	/**
	 * ＩＰアドレス文字列、long 値 → "xxx.xxx.xxx.xxx"  .
	 * @param s ＩＰアドレス文字列
	 * @return long 値
	 */
	public static String longToaddr(long ip){
		if (ip==0) return null;
		String[] str = new String[4];
		Long _ipt = ip;
		for(int i=0;i < 4;i++){
		   str[i] = Long.toString(_ipt % 256L);
		   _ipt = _ipt / 256L;
		}
		return str[3]+"."+str[2] +"."+str[1]+"."+str[0];
	}
	/**
	 * サブネットマスク検査.
	 * <pre>
	 * 検査したいＩＰアドレス、セグメントＩＰアドレス、マスク長を指定して、
	 * サブネットマスクしたＩＰアドレスの範囲であるかを検査する。
	 * </pre>
	 * @param ipaddr 検査したいＩＰアドレス文字列、"xxx.xxx.xxx.xxx" 形式
	 * @param segaddr セグメントＩＰアドレス、"xxx.xxx.xxx.xxx" 形式
	 * @param len マスク長、1～32
	 * @return true=OK,false=NG
	 */
	public static boolean isScorp(String ipaddr,String segaddr,int len){
		byte[] bmask = getMask(len);
		byte[] bseg = addrTobyte(segaddr);
		BigInteger minBigInt = new BigInteger(byteAND(bseg,bmask));
		byte[] bmax = byteOR(bseg,byteXOR(bmask,addrTobyte("255.255.255.255")));
		BigInteger maxBigInt = new BigInteger(bmax);
		BigInteger bchk = new BigInteger(addrTobyte(ipaddr));
		return minBigInt.compareTo(bchk) <= 0 && bchk.compareTo(maxBigInt) <= 0 ? true : false;
	}
	/**
	 * サブネットマスクした値の最小アドレスを返す。
	 * <pre>
	 * （例）
	 * 192.168.87.22 とマスク長 24 を指定した場合、192.168.87.0 を byte[] で返す
	 * </pre>
	 * @param segaddr IPアドレス"xxx.xxx.xxx.xxx" 形式
	 * @param len マスク長、1～32
	 * @return 最小アドレスbyte[]
	 */
	public static byte[] minSegment(String segaddr,int len){
		byte[] bmask = getMask(len);
		byte[] bseg = addrTobyte(segaddr);
		byte[] bw = byteAND(bseg,bmask);
		if (bw[0]==0 && bw[1]==0 && bw[2]==0 && bw[3]==0){
			byte[] b = {0,0,0,0};
			byte[] br = new byte[4];
			int p = b.length > 4 ? 1 : 0;
			for(int i=0;i < br.length;i++,p++){
				br[i] = b[p];
			}
			return br;
		}
		byte[] b = new BigInteger(1,bw).toByteArray();
		byte[] br = new byte[4];
		int p = b.length > 4 ? 1 : 0;
		for(int i=0;i < br.length;i++,p++){
			br[i] = b[p];
		}
		return br;
	}
	/**
	 * サブネットマスクした値の最大アドレスを返す。
	 * <pre>
	 * （例）
	 * 192.168.87.22 とマスク長 24 を指定した場合、192.168.87.255 を byte[] で返す
	 * </pre>
	 * @param segaddr IPアドレス"xxx.xxx.xxx.xxx" 形式
	 * @param len マスク長、1～32
	 * @return 最大アドレスbyte[]
	 */
	public static byte[] maxSegment(String segaddr,int len){
		byte[] bmask = getMask(len);
		byte[] bseg = addrTobyte(segaddr);
		byte[] bmax = byteOR(bseg,byteXOR(bmask,addrTobyte("255.255.255.255")));
		byte[] b = new BigInteger(1,bmax).toByteArray();
		byte[] br = new byte[4];
		int p = b.length > 4 ? 1 : 0;
		for(int i=0;i < br.length;i++,p++){
			br[i] = b[p];
		}
		return br;
	}
	/**
	 * マスク値で、有効アドレス数を求める
	 * @param len len マスク長、1～32
	 * @return 有効アドレス数
	 */
	public static long countSegment(int len){
		byte[] bmask = getMask(len);
		byte[] bseg = new byte[]{-1,-1,-1,-1};
		byte[] bx = new BigInteger(1,byteOR(bseg,byteXOR(bmask,addrTobyte("255.255.255.255")))).toByteArray();
		byte[] bw = byteAND(bseg,bmask);
		if (bw[0]==0 && bw[1]==0 && bw[2]==0 && bw[3]==0){
		byte[] b = {0,0,0,0};
		byte[] br = new byte[4];
		int p = b.length > 4 ? 1 : 0;
		for(int i=0;i < br.length;i++,p++){
			br[i] = b[p];
		}
		byte[] bmax = new byte[4];
		byte[] bmin = new byte[4];
		int k = bx.length > 4 ? 1 : 0;
		for(int i=0;i < bmax.length;i++,k++){
			bmax[i] = bx[k];
			bmin[i] = br[k];
		}
		BigInteger bigintMax = new BigInteger(1,bmax);
		BigInteger bigintMin = new BigInteger(1,bmin);
			return bigintMax.longValue() - bigintMin.longValue() + 1;
		}
		byte[] bn = new BigInteger(1,bw).toByteArray();
		byte[] bmax = new byte[4];
		byte[] bmin = new byte[4];
		int p = bx.length > 4 ? 1 : 0;
		for(int i=0;i < bmax.length;i++,p++){
			bmax[i] = bx[p];
			bmin[i] = bn[p];
		}
		BigInteger bigintMax = new BigInteger(1,bmax);
		BigInteger bigintMin = new BigInteger(1,bmin);
		return bigintMax.longValue() - bigintMin.longValue() + 1;
	}
	/**
	 * ＩＰアドレス文字列、"xxx.xxx.xxx.xxx" → byte[].
	 * @param s ＩＰアドレス文字列
	 * @return byte[]
	 */
	public static byte[] addrTobyte(String s){
		String[] sp = s.split("\\.");
		byte[] b = new byte[4];
		for(int i=0;i < 4;i++){
			b[i] = (byte)(Integer.parseInt(sp[i]));
		}
		return b;
	}
	/**
	 * ＩＰアドレス文字列、"xxx.xxx.xxx.xxx" → int[].
	 * @param s ＩＰアドレス文字列
	 * @return int[]
	 */
	public static int[] addrToint(String s){
		String[] sp = s.split("\\.");
		int[] ii = new int[4];
		for(int i=0;i < 4;i++){
			ii[i] = (byte)(Integer.parseInt(sp[i]));
		}
		return ii;
	}

	/**
	 * アドレスbyte[] → "xxx.xxx.xxx.xxx" フォーマット文字列
	 * @param b byte[]
	 * @return "xxx.xxx.xxx.xxx" フォーマット文字列
	 */
   public static String byteToAddrStr(byte[] b){
		int[] i = new int[b.length];
		byte[] bt = new byte[1];
		for(int k=0;k < b.length;k++){
		   bt[0] = b[k];
		   i[k] = new BigInteger(1,bt).intValue();
		}
		StringBuffer sb = new StringBuffer();
		sb.append(i[0]);
		for(int k=1;k < i.length;k++){
		   sb.append(".");
		   sb.append(i[k]);
		}
		return sb.toString();
   }
	/**
	 * アドレスint[] → "xxx.xxx.xxx.xxx" フォーマット文字列
	 * @param i int[]
	 * @return "xxx.xxx.xxx.xxx" フォーマット文字列
	 */
	public static String intToAddrStr(int[] i){
		StringBuffer sb = new StringBuffer();
		sb.append(i[0]);
		for(int k=1;k < i.length;k++){
			sb.append(".");
			sb.append(i[k]);
		}
		return sb.toString();
	}
	/**
	 * byte[] → int[]
	 * @param b byte[]
	 * @return int[]
	 */
	public static int[] byteTointAry(byte[] b){
		int[] i = new int[b.length];
		byte[] bt = new byte[1];
		for(int k=0;k < b.length;k++){
			bt[0] = b[k];
			i[k] = new BigInteger(1,bt).intValue();
		}
		return i;
	}
	/**
	 * マスク長→サブネットマスク byte[]
	 * @param n マスク長
	 * @return サブネットマスク byte[]
	 */
	public static byte[] getMask(int n){
		byte[] b = new BigInteger(1,new byte[]{-1,-1,-1,-1})
				.shiftRight(n)
				.xor(new BigInteger(1,new byte[]{-1,-1,-1,-1}))
				.toByteArray();
		byte[] br = new byte[4];
		for(int i=0;i < br.length;i++){
			br[i] = b[i+1];
		}
		return br;
	}
	static byte[] byteAND(byte[] b1,byte[] b2){
		byte[] r = new byte[b1.length];
		for(int i=0;i < r.length;i++){
			r[i] = (byte)(b1[i] & b2[i]);
		}
		return r;
	}
	static byte[] byteOR(byte[] b1,byte[] b2){
		byte[] r = new byte[b1.length];
		for(int i=0;i < r.length;i++){
			r[i] = (byte)(b1[i] | b2[i]);
		}
		return r;
	}
	static byte[] byteXOR(byte[] b1,byte[] b2){
		byte[] r = new byte[b1.length];
		for(int i=0;i < r.length;i++){
			r[i] = (byte)(b1[i] ^ b2[i]);
		}
		return r;
	}
}
