package org.yipuran.provider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * プロパティ Provider.
 * <PRE>
 * IPropertiesProvider を実装するクラス
 * PropertiesProviderModule でインジェクトが約束される。
 * IPropertiesProvider の namedProp() と namedCharset() を使用して
 * プロパティ名(.properties より前の部分）、文字キャラクタを指定、以下のように IPropertiesProvider のインスタンス生成の
 * インジェクションを行う。
 *
 * （例）"sample.properties" を UTF-8 で読み込む IPropertiesProvider の場合
 *
 * Injector injector = Guice.createInjector(new AbstractModule(){
 *      ＠Override
 *      protected void configure(){
 *         binder().bind(String.class).annotatedWith(IPropertiesProvider.namedProp()).toInstance("sample");
 *         binder().bind(String.class).annotatedWith(IPropertiesProvider.namedCharset()).toInstance("UTF-8");
 *         binder().bind(IPropertiesProvider.class).to(PropertiesProvider.class);
 *       }
 *  });
 *
 *
 * </PRE>
 */
public class PropertiesProvider implements IPropertiesProvider{
	private String propname;
	private String charset;
	/**
	 * コンストラクタ.
	 * @param propname properties 名
	 * @param charset 文字コードセット
	 */
	@Inject
	public PropertiesProvider(@Named("PROPNAME")String propname, @Named("CHARSET") @Nullable String charset){
		this.propname = propname;
		this.charset = charset==null ? "UTF-8" : charset;
	}

	/* @see org.yipuran.provide.IPropertiesProvider#get() */
	@Override
	public Properties get() throws IOException{
		ResourceBundle rs = ResourceBundle.getBundle(propname, new ResourceBundle.Control(){
			@Override
			public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
			throws IllegalAccessException, InstantiationException, IOException{
				try(InputStreamReader sr = new InputStreamReader(
							loader.getResourceAsStream(toResourceName(toBundleName(baseName, locale), "properties"))
						, charset); BufferedReader reader = new BufferedReader(sr)){
					return new PropertyResourceBundle(reader);
				}
			}
		});
		Properties prop = new Properties();
		for(String key : rs.keySet()){
			prop.setProperty(key,rs.getString(key));
		}
		return prop;
	}
}
