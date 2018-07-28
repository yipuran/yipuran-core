package org.yipuran.provider;

import java.util.Properties;

import com.google.inject.AbstractModule;
import com.google.inject.throwingproviders.ThrowingProviderBinder;
import com.google.inject.util.Providers;

/**
 * プロパティファイルパス指定 の IPropertiesProvider ← PropertiesProvider 注入 Module.
 * <PRE>
 * 注意：プロパティ名(.properties より前の部分）を指定
 *       IPropertiesProvider.namedProp() より、プロパティ名を指定
 *       IPropertiesProvider.namedCharset() より、文字セットを指定するので
 *       Names.named で指定する "PROPNAME" と "CHARSET" が他のインジェクトバインド定義と被らないようにすること
 * </PRE>
 */
public class PropertiesProviderModule extends AbstractModule{
	private String propname;
	private String charset;
	/**
	 * コンストラクタ.
	 * @param propname プロパティ名、 (.properties より前の部分）
	 */
	public PropertiesProviderModule(String propname){
		this.propname = propname;
	}
	/**
	 * コンストラクタ.
	 * @param propname プロパティ名、 (.properties より前の部分）
	 * @param charset 文字コードセット名
	 */
	public PropertiesProviderModule(String propname, String charset){
		this.propname = propname;
		this.charset = charset;
	}
	/* (非 Javadoc)
	 * @see com.google.inject.AbstractModule#configure()
	 */
	@Override
	protected void configure(){
		binder().bind(String.class).annotatedWith(IPropertiesProvider.namedProp()).toInstance(propname);
		binder().bind(String.class).annotatedWith(IPropertiesProvider.namedCharset()).toProvider(Providers.of(charset));
		ThrowingProviderBinder.create(binder())
		.bind(IPropertiesProvider.class , Properties.class)
		.to(PropertiesProvider.class);
	}

}
