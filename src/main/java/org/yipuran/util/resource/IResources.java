package org.yipuran.util.resource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * リソースの読込.
 */
public interface IResources{
	/**
	 * テキスト読込.
	 * <PRE>
	 * このメソッドは、StandardCharsets.UTF_8 で実行される。
	 * </PRE>
	 * @param path リソースPATH
	 * @return テキストString
	 */
	public static String readResource(String path) {
		try(InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(path)){
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			int i;
			while((i = in.read()) > 0) {
				bo.write(i);
			}
			bo.flush();
			bo.close();
			return bo.toString(StandardCharsets.UTF_8.name());
		}catch(IOException e){
			throw new RuntimeException(e);
		}
	}
	/**
	 * テキスト読込（文字セット指定）.
	 * @param path リソースPATH
	 * @param charset 文字セット、文字セット SJIS を指定する場合は、Charset.forName("SJIS")
	 * @return テキストString
	 */
	public static String readResource(String path, Charset charset) {
		try(InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(path)){
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			int i;
			while((i = in.read()) > 0) {
				bo.write(i);
			}
			bo.flush();
			bo.close();
			return bo.toString(StandardCharsets.UTF_8.name());
		}catch(IOException e){
			throw new RuntimeException(e);
		}
	}
	/**
	 * InputStream取得.
	 * @param path リソースPATH
	 * @return InputStream
	 */
	public static InputStream readResourceStream(String path) {
		return ClassLoader.getSystemClassLoader().getResourceAsStream(path);
	}
	/**
	 * Reader取得.
	 * @param path リソースPATH
	 * @return Reader
	 */
	public static Reader readResourceReader(String path) {
		return new InputStreamReader(ClassLoader.getSystemClassLoader().getResourceAsStream(path), StandardCharsets.UTF_8);
	}
	/**
	 * Reader取得（文字セット指定）.
	 * @param path リソースPATH
	 * @param cs 文字セット
	 * @return Reader
	 */
	public static Reader readResourceReader(String path, Charset cs) {
		return new InputStreamReader(ClassLoader.getSystemClassLoader().getResourceAsStream(path), cs);
	}
}
