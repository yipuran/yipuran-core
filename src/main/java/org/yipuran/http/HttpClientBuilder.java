package org.yipuran.http;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * HttpClientを生成するビルダー.
 */
public final class HttpClientBuilder{
	private URL url;
	private String method;
	private String contentType;
	/**
	 * private constructor.
	 * @param path URL path
	 */
	private HttpClientBuilder(String path){
		try{
			url = new URL(path);
		}catch(MalformedURLException e){
			throw new RuntimeException(e);
		}
	}
	/**
	 * HttpClientBuilder生成.
	 * @param path HTTP要求するAPI要求先のURL
	 * @return HttpClientBuilder
	 */
	public static HttpClientBuilder of(String path){
		return new HttpClientBuilder(path);
	}
	/**
	 * メソッド指定
	 * @param method "POST" or "GET"
	 * @return HttpClientBuilder
	 */
	public HttpClientBuilder method(String method) {
		this.method = method.toUpperCase();
		return this;
	}
	/**
	 * ContentType指定
	 * @param contentType contentType指定文字列
	 * @return HttpClientBuilder
	 */
	public HttpClientBuilder contentType(String contentType) {
		this.contentType = contentType;
		return this;
	}
	/**
	 * HttpClient生成.
	 * @return HttpClient
	 */
	public HttpClient build(){
		if (method==null) {
			throw new RuntimeException("method is unknown");
		}
		return new HttpClient(url, method, contentType);
	}
}
