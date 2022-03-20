package org.yipuran.http;

/**
 * BASE64変換ユーティリティ.
 * <pre>
 * BASE64 エンコード処理とデコード処理を提供します。
 *
 * </pre>
 */
public final class Base64Util{
   private Base64Util(){}
   /**
    * BASE64エンコード処理
    * @param value 変換対象
    * @return String  変換結果
    */
   public static String base64Encode(String value){
      return base64Encode(value.getBytes());
   }
   /**
    * BASE64エンコード処理
    * @param byteList 変換対象
    * @return String  変換結果
    */
   public static String base64Encode(byte[] byteList){
      StringBuffer encoded = new StringBuffer();
      for(int i=0; i < byteList.length;i+= 3){
         encoded.append(base64EncodeBlock(byteList, i));
      }
      return encoded.toString();
   }
   /**
    * BASE64変換処理
    * @param byteList 変換対象
    * @param iOffset  変換対象
    * @return char[]  変換結果
    */
   private static char[] base64EncodeBlock(byte[] byteList,int iOffset){
      int block = 0;
      int slack = byteList.length - iOffset - 1;
      int end = (slack >= 2)? 2 : slack;
      for(int i=0;i <= end;i++){
         byte b = byteList[iOffset + i];
         int neuter = (b < 0)? b + 256 : b;
         block += neuter << (8 * (2 - i));
      }
      char[] base64 = new char[4];
      for(int i=0;i < 4;i++){
         int sixBit = (block >>> (6 * (3 - i))) & 0x3f;
         base64[i] = base64EncodeGetChar(sixBit);
      }
      if (slack < 1) base64[2] = '=';
      if (slack < 2) base64[3] = '=';
      return base64;
   }
   /**
    * BASE64変換処理
    * 概要：指定された6ビットをBASE64形式でエンコードし、その文字を返す。
    * @param iSixBit 6bit
    * @return char[] 変換結果
    */
   private static char base64EncodeGetChar(int iSixBit) {
      if (iSixBit >= 0 && iSixBit <= 25)   return (char)('A' + iSixBit);
      if (iSixBit >= 26 && iSixBit <= 51)  return (char)('a' + (iSixBit - 26));
      if (iSixBit >= 52 && iSixBit <= 61)  return (char)('0' + (iSixBit - 52));
      if (iSixBit == 62) return '+';
      if (iSixBit == 63) return '/';
      return '?';
   }

   /**
    * <PRE>
    * BASE64デコード処理
    * 概要　：指定された文字列を、BASE64形式でデコードし、そのバイト列を返す
    * </PRE>
    * @param base64  BASE64形式の文字列.
    * @return  デコード後のバイト列.
    */
   public static byte[] base64Decode(String base64) {
      if (base64==null || base64.length()==0 || base64.length()%4!=0) {
         return  new byte[0];
      }
      int pad = 0;
      for(int i=base64.length() - 1; base64.charAt(i) == '='; i--)
         pad++;
      int length = base64.length() * 6 / 8 - pad;
      byte[] raw = new byte[length];
      int rawIndex = 0;
      for(int i=0; i < base64.length();i += 4){
         int block = (base64DecodeGetValue(base64.charAt(i)) << 18)
                   + (base64DecodeGetValue(base64.charAt(i + 1)) << 12)
                   + (base64DecodeGetValue(base64.charAt(i + 2)) << 6)
                   + (base64DecodeGetValue(base64.charAt(i + 3)));

         for(int j=0; j < 3 && rawIndex + j < raw.length;j++)
            raw[rawIndex + j] = (byte)((block >> (8 * (2 - j))) & 0xff);
         rawIndex += 3;
      }
      return raw;
   }
   /**
    * <PRE>
    * BASE64デコード処理(6ビット)
    * 概要　：BASE64形式の文字からBinary変換値を復元する
    * </PRE>
    * @param c BASE64形式の文字
    * @return  Binary変換値
    */
   private static int base64DecodeGetValue(char c) {
      if (c >= 'A' && c <= 'Z') return c - 'A';
      if (c >= 'a' && c <= 'z') return c - 'a' + 26;
      if (c >= '0' && c <= '9') return c - '0' + 52;
      if (c == '+') return 62;
      if (c == '/') return 63;
      if (c == '=') return 0;
      return -1;
   }
}
