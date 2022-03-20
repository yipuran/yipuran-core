package org.yipuran.env;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;
/**
 * PropertyUtil   Properties ユーティリティ
 */
public final class PropertyUtil{
	private PropertyUtil(){}
	/**
	 * クラス指定、bundle 名指定 Properties取得
	 * クラスと同じ場所にproperties ファイルを置いて取得する
	 * 文字コードは、UTF-8
	 * 文字コード指定は、getBundle(Class<?> cls,String name,String charName) を使用
	 * @param cls class または、interface
	 * @param name ".properties" の前の名称
	 * @return Properties
	 */
	public static Properties getBundle(Class<?> cls,String name){
		return getBundle(cls,name,"UTF-8");
	}
	/**
	 * 文字コード指定、クラス指定、bundle 名指定 Properties取得.
	 * クラスと同じ場所にproperties ファイルを置いて取得する
	 * @param cls class または、interface
	 * @param name ".properties" の前の名称
	 * @param charName 文字コード
	 * @return Properties
	 */
	public static Properties getBundle(Class<?> cls,String name,String charName){
		URL url = ClassLoader.getSystemClassLoader().getResource( cls.getPackage().getName().replaceAll("\\.","/")
				+"/"+ name +".properties");
		try{
			return read(new File(url.getPath()),charName);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	/**
	 * ResouceBundle#getBundle 同様の properties 形式ファイル読込み、文字コードは、UTF-8
	 * @param name bundle名
	 * @return Properties
	 */
	public static Properties getBundle(String name){
		URL url = ClassLoader.getSystemClassLoader().getResource(name+".properties");
		try{
			return read(new File(url.getPath()),"UTF-8");
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	/**
	 * 文字コード指定、ResouceBundle#getBundle 同様の properties 形式ファイル読込み
	 * param name bundle名
	 * @param charName
	 * @return Properties
	 */
	public static Properties getBundle(String name,String charName){
		URL url = ClassLoader.getSystemClassLoader().getResource(name+".properties");
		try{
			return read(new File(url.getPath()),charName);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	/**
	 * properties 形式ファイル読込み、文字コードは、UTF-8
	 * @param path propertiesファイルPATH
	 * @return Properties
	 */
	public static Properties read(String path){
		return read(new File(path));
	}
	/**
	 * roperties 形式ファイル読込み、文字コードは、UTF-8
	 * @param file propertiesファイル
	 * @return Properties
	 */
	public static Properties read(File file){
		return read(file,"UTF-8");
	}
	/**
	 * properties 形式ファイル読込み、文字コードを指定する
	 * @param path propertiesファイルPATH
	 * @param encode 文字コード
	 * @return Properties
	 */
	public static Properties read(String path,String encode){
		return read(new File(path),encode);
	}
	/**
	 * roperties 形式ファイル読込み、文字コードを指定する
	 * @param file propertiesファイル
	 * @param encode 文字コード
	 * @return Properties
	 */
	public static Properties read(File file,String encode){
		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), encode))){
			Properties prop = new Properties();
			String line;
			while((line=br.readLine()) != null){
				int eq = line.indexOf("=");
				if (eq <= 0) continue;
				if (line.startsWith("#")) continue;
				prop.put(line.substring(0,eq).trim(),line.substring(eq+1).trim());
			}
			return prop;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
}
