package org.yipuran.provider;

import java.io.IOException;
import java.util.Properties;

import com.google.inject.name.Names;
import com.google.inject.throwingproviders.CheckedProvider;
/**
 * プロパティ Provider インターフェース.
 *
 * （例）"sample.properties" を UTF-8 で読み込む IPropertiesProvider の場合
 *
 * Injector injector = Guice.createInjector(new AbstractModule(){
 *      ＠Override
 *      protected void configure(){
 *         binder().bind(String.class).annotatedWith(IPropertiesProvider.namedProp()).toInstance("sample");
 *         binder().bind(String.class).annotatedWith(IPropertiesProvider.namedCharset()).toInstance("UTF-8");
 *         binder().bind(IPropertiesProvider.class).to(PropertiesProvider.class);
 *      }
 * });
 *
 */
public interface IPropertiesProvider extends CheckedProvider<Properties>{
	@Override
	public Properties get() throws IOException;

	/**
	 * プロパティ名アノテーションバインド連結 com.google.inject.name.Named 作成.
	 * @return Names.named("PROPNAME")
	 */
	public static com.google.inject.name.Named namedProp(){
		return Names.named("PROPNAME");
	}
	/**
	 * 文字セットアノテーションバインド連結 com.google.inject.name.Named 作成.
	 * @return Names.named("CHARSET");
	 */
	public static com.google.inject.name.Named namedCharset(){
		return Names.named("CHARSET");
	}
}
