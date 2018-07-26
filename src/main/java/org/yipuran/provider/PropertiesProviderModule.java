package org.yipuran.provider;

import java.util.Properties;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.google.inject.throwingproviders.ThrowingProviderBinder;
import com.google.inject.util.Providers;

/**
 * IPropertiesProvider ← PropertiesProvider 注入 Module.
 * <PRE>
 * 注意：　properties ファイルパスを指定
 * </PRE>
 */
public class PropertiesProviderModule extends AbstractModule{
	private String path;
	private String charset;
	/**
	 * コンストラクタ.
	 * @param path properties ファイルパス
	 */
	public PropertiesProviderModule(String path){
		this.path = path;
	}
	/**
	 * コンストラクタ.
	 * @param path properties ファイルパス
	 */
	public PropertiesProviderModule(String path, String charset){
		this.path = path;
		this.charset = charset;
	}
	/* (非 Javadoc)
	 * @see com.google.inject.AbstractModule#configure()
	 */
	@Override
	protected void configure(){
		binder().bind(String.class).annotatedWith(Names.named("PROPERTIES")).toInstance(path);
		binder().bind(String.class).annotatedWith(Names.named("CHARSET")).toProvider(Providers.of(charset));
		ThrowingProviderBinder.create(binder())
		.bind(IPropertiesProvider.class , Properties.class)
		.to(PropertiesProvider.class);
	}

}
