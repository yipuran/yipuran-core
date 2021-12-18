package org.yipuran.file;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * PATH指定ファイル取得.
 * <PRE>
 * 現在実行中のクラスローダーより、PATH による、File取得, InputStream取得 , テキストへの読込み、を行う。
 * </PRE>
 */
public interface IPath{

	/**
	 * クラス相対PATH指定 File取得.
	 * @param cls PATH起点位置に置かれたクラス
	 * @param path PATH
	 * @return File
	 * @throws IOException
	 */
	public static File getPathFile(Class<?> cls, String path){
		try{
			return new File(ClassLoader.getSystemClassLoader()
				.getResource(cls.getPackage().getName().replaceAll("\\.", "/") + "/" + path).toURI());
		}catch(URISyntaxException e){
			throw new RuntimeException(e);
		}
	}
	/**
	 * 現在実行中のクラス起点PATH File取得.
	 * @param path 現在実行中のクラス起点PATH
	 * @return File
	 */
	public static File getCurrentPathFile(String path){
		String s = Thread.currentThread().getStackTrace()[2].getClassName();
		int n = s.lastIndexOf(".");
		String f = n < 0 ? path : s.substring(0, n).replaceAll("\\.", "/") + "/" + path;
		try{
			return new File(ClassLoader.getSystemClassLoader().getResource(f).toURI());
		}catch(URISyntaxException e){
			throw new RuntimeException(e);
		}
	}
	/**
	 * 存在確認つき、現在実行中のクラス起点PATH File取得.
	 * @param path 現在実行中のクラス起点PATH
	 * @return File
	 * @throws IOException
	 */
	public static File getCurrentPathFileSafety(String path) throws IOException{
		String s = Thread.currentThread().getStackTrace()[2].getClassName();
		int n = s.lastIndexOf(".");
		String f = n < 0 ? path : s.substring(0, n).replaceAll("\\.", "/") + "/" + path;
		return Optional.ofNullable(
			ClassLoader.getSystemClassLoader().getResource(f)
		).map(u->{
			try{
				return new File(u.toURI());
			}catch(URISyntaxException e){
				throw new RuntimeException(e);
			}
		}).orElseThrow(()->new IOException(path + " is not Found"));
	}
	/**
	 * 現在実行中のクラス起点PATH InputStream取得.
	 * @param path 現在実行中のクラス起点PATH
	 * @return InputStream
	 */
	public static InputStream getInputStream(String path){
		String s = Thread.currentThread().getStackTrace()[2].getClassName();
		int n = s.lastIndexOf(".");
		String f = n < 0 ? path : s.substring(0, n).replaceAll("\\.", "/") + "/" + path;
		try{
			return new FileInputStream(new File(ClassLoader.getSystemClassLoader().getResource(f).toURI()));
		}catch(URISyntaxException e){
			throw new RuntimeException(e);
		}catch(IOException e){
			throw new RuntimeException(e);
		}
	}
	/**
	 * 現在実行中のクラス起点PATH テキスト取得.
	 * <PRE>
	 * UTF-8 で読み込む為、UTF-8 以外は、Charsetを指定する readText(String path, Charset charset) を使用すること。
	 * このメソッドは、StandardCharsets.UTF_8 で実行される。
	 * </PRE>
	 * @param path 現在実行中のクラス起点PATH
	 * @return String
	 */
	public static String readText(String path) {
		String s = Thread.currentThread().getStackTrace()[2].getClassName();
		int n = s.lastIndexOf(".");
		String f = n < 0 ? path : s.substring(0, n).replaceAll("\\.", "/") + "/" + path;
		try{
			File file = new File(ClassLoader.getSystemClassLoader().getResource(f).toURI());
			InputStream in = new FileInputStream(file);
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			int i;
			while((i = in.read()) > 0) {
				bo.write(i);
			}
			bo.flush();
			bo.close();
			in.close();
			return bo.toString(StandardCharsets.UTF_8.name());
		}catch(IOException e){
			throw new RuntimeException(e);
		}catch(URISyntaxException e){
			throw new RuntimeException(e);
		}
	}
	/**
	 * 現在実行中のクラス起点PATH テキスト取得（文字セット指定）.
	 * @param path  現在実行中のクラス起点PATH
	 * @param charset 文字セット、文字セット SJIS を指定する場合は、Charset.forName("SJIS")
	 * @return String
	 */
	public static String readText(String path, Charset charset) {
		String s = Thread.currentThread().getStackTrace()[2].getClassName();
		int n = s.lastIndexOf(".");
		String f = n < 0 ? path : s.substring(0, n).replaceAll("\\.", "/") + "/" + path;
		try{
			File file = new File(ClassLoader.getSystemClassLoader().getResource(f).toURI());
			InputStream in = new FileInputStream(file);
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			int i;
			while((i = in.read()) > 0) {
				bo.write(i);
			}
			bo.flush();
			bo.close();
			in.close();
			return bo.toString(charset.name());
		}catch(IOException e){
			throw new RuntimeException(e);
		}catch(URISyntaxException e){
			throw new RuntimeException(e);
		}
	}
}
