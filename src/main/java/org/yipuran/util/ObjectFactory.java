package org.yipuran.util;

import java.util.Map;
import java.util.Properties;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;

/**
 * Properties or Map → Object 初期生成.
 * <pre>
 * ＠Inject ＠Named を付与されたフィールドを、指定する Properties , Map<String,String>
 * で初期化してインスタンスを生成する。
 * ただし、初期化対象は、プリミティブ型に制限される。もしプリミティブ型以外に、
 * ＠Inject ＠Named を付与してProperties , Map<String,String>し
 * を指定すると com.google.inject.ConfigurationException を投げる
 *
 * (例）
 * public class Sample{
 *   ＠Inject ＠Named("USER")   private String user;
 *   ＠Inject ＠Named("DOMAIN") private String domain;
 *   ＠Inject ＠Named("SIZE")   private long size;
 *     :
 * }
 * "USER"、"DOMAIN"、"SIZE" をＫｅｙに持つ Properties , Map<String,String>
 * を用意して、Sample を生成
 *
 * Sample s = ObjectFactory.createInstance(Sample.class,properties);
 *
 * Sample s = ObjectFactory.createInstance(Sample.class,map);
 *
 * </pre>
 */
public final class ObjectFactory{
	private ObjectFactory(){}
	/**
	 * Properties → Object 初期生成.
	 * @param cls 初期化対象クラス
	 * @param prop Properties
	 * @return 初期化されたclsインスタンス
	 */
	public static <T> T createInstance(Class<T> cls,Properties prop){
		final Properties p = prop;
		Injector injector = Guice.createInjector(new AbstractModule(){
			@Override
			protected void configure(){
				Names.bindProperties(binder(),p);
			}
		});
		return injector.getInstance(cls);
	}
	/**
	 * Map<String,String> → Object 初期生成.
	 * @param cls 初期化対象クラス
	 * @param m Map<String,String>
	 * @return 初期化されたclsインスタンス
	 */
	public static <T> T createInstance(Class<T> cls,Map<String,String> map){
		final Map<String,String> m = map;
		Injector injector = Guice.createInjector(new AbstractModule(){
			@Override
			protected void configure(){
				Names.bindProperties(binder(),m);
			}
		} );
		return injector.getInstance(cls);
	}
}
