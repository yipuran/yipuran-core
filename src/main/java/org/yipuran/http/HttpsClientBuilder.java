package org.yipuran.http;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * HttpsClientを生成するビルダー.
 */
public final class HttpsClientBuilder{
	private URL url;
	private String method;
	private String contentType;
	private Map<String, String> headerOptions;
	private String proxy_server;
	private String proxy_user;
	private String proxy_passwd;
	private Integer proxy_port;
	/**
	 * private constructor.
	 * @param path URL path
	 */
	private HttpsClientBuilder(String path){
		headerOptions = new HashMap<>();
		try{
			url = new URL(path);
		}catch(MalformedURLException e){
			throw new RuntimeException(e);
		}
	}
	/**
	 * HttpsClientBuilder生成.
	 * @param path HTTP要求するAPI要求先のURL
	 * @return HttpsClientBuilder
	 */
	public static HttpsClientBuilder of(String path){
		return new HttpsClientBuilder(path);
	}
	/**
	 * メソッド指定
	 * @param method "POST" or "GET"
	 * @return HttpsClientBuilder
	 */
	public HttpsClientBuilder method(String method) {
		this.method = method.toUpperCase();
		return this;
	}
	/**
	 * ContentType指定
	 * @param contentType contentType指定文字列
	 * @return HttpsClientBuilder
	 */
	public HttpsClientBuilder contentType(String contentType) {
		this.contentType = contentType;
		return this;
	}
	/**
	 * Header property 追加.
	 * @param name key
	 * @param value value
	 * @return HttpsClientBuilder
	 */
	public HttpsClientBuilder addHeaderProperty(String name, String value) {
		headerOptions.put(name, value);
		return this;
	}
	/**
	 * Proxy使用指定.
	 * @param proxy_server Proxyサーバ名
	 * @param proxy_user Proxyユーザ名
	 * @param proxy_passwd Proxyパスワード
	 * @param proxy_port Proxyポート番号
	 * @return HttpsClientBuilder
	 */
	public HttpsClientBuilder addProxy(String proxy_server, String proxy_user, String proxy_passwd, Integer proxy_port){
		this.proxy_server = proxy_server;
		this.proxy_user = proxy_user;
		this.proxy_passwd = proxy_passwd;
		this.proxy_port = proxy_port;
		return this;
	}
	/**
	 * HttpsClient生成.
	 * @return HttpsClient
	 */
	public HttpsClient build(){
		if (method==null) {
			throw new RuntimeException("method is unknown");
		}
		if (proxy_server==null) return new HttpsClient(url, method, contentType, headerOptions);
		return new HttpsClient(url, method, contentType, headerOptions, proxy_server, proxy_user, proxy_passwd, proxy_port);
	}
}
