package org.yipuran.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * トークン生成→ List or Stream.
 * <PRE>
 * 区切り文字と区切り文字をエスケープする文字を指定して、文字列、入力ストリームから、
 * トークンを生成して、List＜String＞  または、Stream＜String＞ を取得する。
 * </PRE>
 * @since 4.22
 */
public interface Tokenstring{

	/**
	 * 文字列 → List＜String＞ トークンリスト
	 * @param str 文字列
	 * @param sep 区切り文字 char
	 * @param escape エスケープ文字 char  '\0' を指定した場合は、split で取得するものと同じになりエスケープは行わなれない。
	 * @return トークンリスト
	 */
	public static List<String> tokenToList(String str, char sep, char escape){
		List<String> list = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		boolean isEscape = false;
		for(char c:str.toCharArray()) {
			if (isEscape) {
				isEscape = false;
			}else if(c==escape){
				isEscape = true;
				sb.append(c);
				continue;
			}else if(c==sep){
				list.add(sb.toString());
				sb.setLength(0);
				continue;
			}
			sb.append(c);
		}
		list.add(sb.toString());
		return list;
	}

	/**
	 * InputStream → List＜String＞ トークンリスト
	 * @param in 入力ストリーム new InputStreamReader(in, StandardCharsets.UTF_8) で処理される
	 * @param sep 区切り文字 char
	 * @param escape エスケープ文字 char  '\0' を指定した場合は、split で取得するものと同じになりエスケープは行わなれない。
	 * @return トークンリスト
	 * @throws IOException
	 */
	public static List<String> tokenToList(InputStream in, char sep, char escape) throws IOException{
		return tokenToList(new InputStreamReader(in, StandardCharsets.UTF_8), sep, escape);
	}

	/**
	 * InputStreamReader → List＜String＞ トークンリスト
	 * @param reader 入力 InputStreamReader Charsetを指定したInputStreamReaderを使用すべき。
	 * @param sep 区切り文字 char
	 * @param escape エスケープ文字 char  '\0' を指定した場合は、split で取得するものと同じになりエスケープは行わなれない。
	 * @return トークンリスト
	 * @throws IOException
	 */
	public static List<String> tokenToList(InputStreamReader reader, char sep, char escape) throws IOException{
		List<String> list = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		try{
			boolean isEscape = false;
			int i;
			while((i=reader.read()) > 0){
				char c = (char)i;
				if (isEscape){
					isEscape = false;
				}else if(c==escape){
					isEscape = true;
					sb.append(c);
					continue;
				}else if(c==sep){
					list.add(sb.toString());
					sb.setLength(0);
					continue;
				}
				sb.append(c);
			}
			list.add(sb.toString());
		}catch(IOException e){
			throw e;
		}
		return list;
	}

	/**
	 * 文字列 → Stream＜String＞ トークン
	 * @param str 文字列
	 * @param sep 区切り文字 char
	 * @param escape エスケープ文字 char  '\0' を指定した場合は、split で取得するものと同じになりエスケープは行わなれない。
	 * @return Stream
	 */
	public static Stream<String> tokenToStream(String str, char sep, char escape){
		Stream.Builder<String> b = Stream.builder();
		StringBuilder sb = new StringBuilder();
		boolean isEscape = false;
		for(char c:str.toCharArray()) {
			if (isEscape) {
				isEscape = false;
			}else if(c==escape){
				isEscape = true;
				sb.append(c);
				continue;
			}else if(c==sep){
				b.add(sb.toString());
				sb.setLength(0);
				continue;
			}
			sb.append(c);
		}
		b.add(sb.toString());
		return b.build();
	}

	/**
	 * InputStream → Stream＜String＞ トークン
	 * @param in 入力ストリーム new InputStreamReader(in, StandardCharsets.UTF_8) で処理される
	 * @param sep 区切り文字 char
	 * @param escape エスケープ文字 char  '\0' を指定した場合は、split で取得するものと同じになりエスケープは行わなれない。
	 * @return トークンStream
	 * @throws IOException
	 */
	public static Stream<String> tokenToStream(InputStream in, char sep, char escape) throws IOException{
		return tokenToStream(new InputStreamReader(in, StandardCharsets.UTF_8), sep, escape);
	}

	/**
	 * InputStreamReader → List＜String＞ トークン
	 * @param reader 入力 InputStreamReader Charsetを指定したInputStreamReaderを使用すべき。
	 * @param sep 区切り文字 char
	 * @param escape エスケープ文字 char  '\0' を指定した場合は、split で取得するものと同じになりエスケープは行わなれない。
	 * @return トークンStream
	 * @throws IOException
	 */
	public static Stream<String> tokenToStream(InputStreamReader in, char sep, char escape) throws IOException{
		Stream.Builder<String> b = Stream.builder();
		StringBuilder sb = new StringBuilder();
		try{
			boolean isEscape = false;
			int i;
			while((i=in.read()) > 0){
				char c = (char)i;
				if (isEscape){
					isEscape = false;
				}else if(c==escape){
					isEscape = true;
					sb.append(c);
					continue;
				}else if(c==sep){
					b.add(sb.toString());
					sb.setLength(0);
					continue;
				}
				sb.append(c);
			}
			b.add(sb.toString());
		}catch(IOException e){
			throw e;
		}
		return b.build();
	}
}
