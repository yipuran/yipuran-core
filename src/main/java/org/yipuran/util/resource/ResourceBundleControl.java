package org.yipuran.util.resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * ResourceBundle.Controlカスタマイズクラス.
 * <pre>
 * propertiesファイルの value に日本語文字、２バイト文字を含む時に、
 * 文字エンコードを指定して、ResourceBundleを取得する為のクラス
 * 【使用方法】
 *     ResourceBundle rs = ResourceBundle.getBundle(baseName,new ResourceBundleControl("MS932"));
 *
 *     String v = rs.getString(key);
 *
 *     // １時間後に再ロードは、コンストラクタで指定
 *     ResourceBundle rs = ResourceBundle.getBundle(baseName
 *                         ,new ResourceBundleControl("MS932",60*60*1000));
 *
 * （注意）JDK1.6 以上でなければならない。
 * </pre>
 * @since 1.1.3
 */
public class ResourceBundleControl extends ResourceBundle.Control{
	private String charName;
	private long cashLimit;
	/**
	 * コンストラクタ.
	 * @param charName 読取りの文字エンコードを指定する
	 */
	public ResourceBundleControl(String charName){
		super();
		this.charName = charName;
		this.cashLimit = TTL_NO_EXPIRATION_CONTROL;
	}
	/**
	 * 再ロード指定のコンストラクタ.
	 * <pre>
	 * getBundleでResourceBundleオブジェクトがキャッシュされたものを
	 * 破棄して再ロードを開始する時間を指定できる。
	 * ResourceBundleControl(String charName)コンストラクタを用いた場合、
	 * キャッシュに保持できる時間は無制限（デフォルト）であるため、再ロードは行われない
	 * </pre>
	 * @param charName  読取りの文字エンコードを指定する
	 * @param cashLimit キャッシュを破棄し再ロードする時間ミリ秒
	 */
	public ResourceBundleControl(String charName,long cashLimit){
		super();
		this.charName = charName;
		this.cashLimit = cashLimit;
	}
	/*
	 * (非 Javadoc)
	 * @see java.util.ResourceBundle.Control#newBundle(java.lang.String, java.util.Locale, java.lang.String, java.lang.ClassLoader, boolean)
	 */
	@Override
	public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) throws IllegalAccessException,InstantiationException,IOException{
		String bundleName = toBundleName(baseName, locale);
		ResourceBundle bundle = null;
		if (format.equals("java.class")){
         try{
				@SuppressWarnings("unchecked")
				Class<? extends ResourceBundle> bundleClass = (Class<? extends ResourceBundle>)loader.loadClass(bundleName);
				// If the class isn't a ResourceBundle subclass, throw a
				// ClassCastException.
				if (ResourceBundle.class.isAssignableFrom(bundleClass)) {
					bundle = bundleClass.newInstance();
				}else{
					throw new ClassCastException(bundleClass.getName()+" cannot be cast to ResourceBundle");
				}
         }catch(ClassNotFoundException e){
         }
		}else if(format.equals("java.properties")){
			final String resourceName = toResourceName(bundleName,"properties");
			final ClassLoader classLoader = loader;
			final boolean reloadFlag = reload;
			/*************
			InputStream stream = null;
			try{
				stream = AccessController.doPrivileged(new PrivilegedExceptionAction<InputStream>() {
					@Override
					public InputStream run() throws IOException{
						InputStream is = null;
						if (reloadFlag){
							URL url = classLoader.getResource(resourceName);
							if (url != null){
								URLConnection connection = url.openConnection();
								if (connection != null){
									// Disable caches to get fresh data for
									// reloading.
									connection.setUseCaches(false);
									is = connection.getInputStream();
								}
							}
						}else{
							is = classLoader.getResourceAsStream(resourceName);
						}
						return is;
					}
				});
			}catch(PrivilegedActionException e){
				throw (IOException)e.getException();
			}
			if (stream != null){
				try{
					//----------- change!  START ------------
					//bundle = new PropertyResourceBundle(stream);
					BufferedReader br = new BufferedReader(new InputStreamReader(stream,this.charName));
					bundle = new PropertyResourceBundle(br);
					br.close();
					//----------- change!  END   ------------
				}finally{
					stream.close();
				}
			}
			/*********************/
			try(InputStream stream = AccessController.doPrivileged(new PrivilegedExceptionAction<InputStream>() {
					@Override
					public InputStream run() throws IOException{
						if (reloadFlag){
							URL url = classLoader.getResource(resourceName);
							if (url != null){
								URLConnection connection = url.openConnection();
								if (connection != null){
									connection.setUseCaches(false);
									return connection.getInputStream();
								}
							}
						}else{
							return classLoader.getResourceAsStream(resourceName);
						}
						return null;
					}
				});
				BufferedReader br = new BufferedReader(new InputStreamReader(stream,this.charName));
			){
				bundle = new PropertyResourceBundle(br);
			}catch(PrivilegedActionException e){
				throw (IOException)e.getException();
			}
		}else{
			throw new IllegalArgumentException("unknown format: "+format);
		}
		return bundle;
	}
	/*
	 * (非 Javadoc)
	 * @see java.util.ResourceBundle.Control#getTimeToLive(java.lang.String, java.util.Locale)
	 */
	@Override
	public long getTimeToLive(String baseName, Locale locale) {
		if (baseName == null || locale == null) {
			throw new NullPointerException();
		}
		return this.cashLimit;
	}

}
