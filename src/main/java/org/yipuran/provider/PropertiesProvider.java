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
 * PropertiesProviderModule でインジェクトが約束される。
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
