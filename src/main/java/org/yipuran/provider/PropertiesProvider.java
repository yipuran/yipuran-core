package org.yipuran.provider;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * プロパティ Provider.
 * <PRE>
 * IPropertiesProvider を実装するクラス
 * PropertiesProviderModule でインジェクトが約束される。
 * Nameアノテーション PROPERTIES でプロパティファイルPATH
 * Nameアノテーション CHARSET で文字キャラクタセットでインスタンス生成する
 *
 *
 * Properties 読込み先を自由に設定、複数 Properties 管理を目的に、
 * PropertiesProviderModule もしくは、任意で Moduleを指定する為に用意された
 * この PropertiesProvider を使用せずに IPropertiesProvider 実装を用意した場合の
 * インジェクトは、以下のようにする。
 *
 * Injector injector = Guice.createInjector(new AbstractModule(){
 *      ＠Override
 *      protected void configure(){
 *         // IPropertiesProvider実装中で Properties 読込みの為の指定 "sample.properties" を読込み
 *         binder().bind(String.class).annotatedWith(Names.named("PROP_NAME")).toInstance("sample");
 *         binder().bind(String.class).annotatedWith(Names.named("ResourcePath")).toInstance(classpath);
 *         // IPropertiesProvider実装→ Properties 提供を約束するクラスのインジェクト
 *         ThrowingProviderBinder.create(this.binder())
 *          .bind(IPropertiesProvider.class, Properties.class)
 *          .to(LaboProvider.class);
 *       }
 *  });
 *  PropertiesProvider を使用する場合は、PropertiesProviderModule で、
 *  Properties の読込みファイルPATHを指定しなければならない。
 *
 * </PRE>
 */
public class PropertiesProvider implements IPropertiesProvider{
	private String filePath;
	private String charset;
	/**
	 * コンストラクタ.
	 * @param filePath properties ファイルパス
	 * @param charset 文字コードセット
	 */
	@Inject
	public PropertiesProvider(@Named("PROPERTIES")String filePath, @Named("CHARSET") @Nullable String charset){
		this.filePath = filePath;
		this.charset = charset==null ? "UTF-8" : charset;
	}
	/* (非 Javadoc)
	 * @see org.yipuran.provide.IPropertiesProvider#get()
	 */
	@Override
	public Properties get() throws IOException{
		Properties prop = new Properties();
		try(InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(this.filePath)){
			try(InputStream is = new FileInputStream(this.filePath)){
				prop.load(new InputStreamReader(is, charset));
			}
		}
		return prop;
	}
}
