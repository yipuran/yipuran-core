package org.yipuran.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

/**
 * HttpsClient.
 * HttpsClientBuilder で HttpClientインスタンスを生成する。
 *  <PRE>
 * 要求先 URL パス、要求の種類(POST/GET)、要求するパラメータは、HttpsClientBuilder で指定する。
 * HttpsClientBuilder を使用することで送信するパラメータ値のエンコードは保証される。
 *
 * HttpsClient client = HttpsClientBuilder.of("http://xxx/xxx")
 *                    .method("POST")
 *                    .contentType("application/JSON; charset=utf-8")
 *                    .build();
 * String result = client.execute(ThrowableConsumer.of(out->{
 *    OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
 *    writer.write("{\"name\":\"apple\",\"price\":150}");
 *    writer.flush();
 * }), (t, m)->{
 *    logger.debug("# ContentType = " + m );
 *    ((Map&lt;String, List&lt;String&gt;&gt;&gt;)m).entrySet().stream().forEach(e->{
 *    logger.info("headerName = " + e.getKey());
 *    List&lt;String&gt; l = e.getValue();
 *    if (l != null){
 *       l.stream().forEach(s->{
 *          logger.info(s);
 *       });
 *    }
 * });
 * または、
 * public void execute(Consumer&lt;OutputStream&gt; outconsumer, BiConsumer&lt;String, Map&lt;String, List&lt;String&gt;&gt;&gt; headconsumer, Consumer&lt;InputStream&gt; readconsumer)
 *
 * client.execute(ThrowableConsumer.of(out->{
 *    OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
 *    writer.write("{\"name\":\"apple\",\"price\":150}");
 *    writer.flush();
 * }), (type, m)->{
 *    System.out.println("type = " + type);
 *    m.entrySet().stream().forEach(e->{
 *       System.out.println(e.getKey() + " → " + e.getValue());
 *    });
 * }, in->{
 *    try(OutputStream out = new FileOutputStream(new File("result"))){
 *       byte[] buf = new byte[1024];
 *       int n;
 *       while((n = in.read(buf)) >= 0){
 *          out.write(buf, 0, n);
 *       }
 *    }catch(IOException e){
 *       e.printStackTrace();
 *    }
 * });
 *
 *  注意すべきは、１つのインスタンスで併用はできない。
 *  </PRE>
 */
public class HttpsClient{
	private URL url;
	private String method;
	private String contentType;
	private String proxy_server;
	private String proxy_user;
	private String proxy_passwd;
	private Integer proxy_port;
	private int httpresponsecode;
	/**
	 * コンストラクタ.
	 * @param url HTTP先URL
	 * @param method HTTPメソッド
	 * @param contentType contentType指定文字列
	 */
	protected HttpsClient(URL url, String method, String contentType){
		this.url = url;
		this.method = method;
		this.contentType = contentType;
	}
	/**
	 * コンストラクタ（Proxy指定）.
	 * @param url HTTP先URL
	 * @param method HTTPメソッド
	 * @param contentType contentType指定文字列
	 * @param proxy_server Proxyサーバ名
	 * @param proxy_user Proxyユーザ名
	 * @param proxy_passwd Proxyパスワード
	 * @param proxy_port Proxyポート番号
	 * @param map パラメータMap 値は、URLエンコードされてなければならない。HttpsClientBuilder を使用することで送信するパラメータ値のエンコードは保証される。
	 */
	protected HttpsClient(URL url, String method, String contentType, String proxy_server, String proxy_user, String proxy_passwd, Integer proxy_port){
		this.url = url;
		this.method = method;
		this.contentType = contentType;
		this.proxy_server = proxy_server;
		this.proxy_user = proxy_user;
		this.proxy_passwd = proxy_passwd;
		this.proxy_port = proxy_port;
	}
	/**
	 * HTTPS要求送受信.
	 * @param outconsumer 送信 OutputStream Consumer
	 * @param consumer HTTPS通信結果、受け取ったHTTP ContentType、HTTPヘッダを読取り処理する BiConsumer
	 * @return 要求した結果を受信したテキスト文字列。受信失敗は、null を返す。
	 */
	public String execute(Consumer<OutputStream> outconsumer, BiConsumer<String, Map<String, List<String>>> consumer){
		String result = null;
		try{
			SSLContext ctx = SSLContext.getInstance("SSL");
			ctx.init(null, new X509TrustManager[]{ new NonAuthentication() }, null);
			SSLSocketFactory factory = ctx.getSocketFactory();

			HttpsURLConnection uc;
			if (proxy_server != null){
				// Proxy利用
				if (proxy_user != null && proxy_passwd != null){
					Authenticator.setDefault(new Authenticator(){
						@Override
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(proxy_user, proxy_passwd.toCharArray());
						}
					});
				}
				Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxy_server, Optional.ofNullable(proxy_port).orElse(80)));
				uc = (HttpsURLConnection)url.openConnection(proxy);
			}else{
				uc = (HttpsURLConnection)url.openConnection();
			}
			uc.setSSLSocketFactory(factory);
			/* HTTPリクエストヘッダの設定 */
			uc.setDoOutput(true);              // こちらからのデータ送信を可能とする
			uc.setReadTimeout(0);              // 読み取りタイムアウト値をミリ秒単位で設定(0は無限)
			uc.setRequestMethod(method);       // URL 要求のメソッドを設定
			if (contentType != null) uc.setRequestProperty("Content-Type", contentType);
			// コネクション確立→送信
			uc.connect();

			try(OutputStream out = uc.getOutputStream()){
				outconsumer.accept(out);
				out.flush();
			}

			httpresponsecode = uc.getResponseCode();
			if (httpresponsecode != 200){
				return null;
			}
			// 戻り値取得
			consumer.accept(uc.getContentType(), uc.getHeaderFields());
			// 応答読込
			try(BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream(), StandardCharsets.UTF_8))){
				StringBuilder sout = new StringBuilder();
				char[] buf = new char[1024];
				int n;
				while((n = in.read(buf)) >= 0){
					sout.append(buf, 0, n);
				}
				result = sout.toString();
			}catch(IOException e){
				throw new RuntimeException(e);
			}
		}catch(Exception e){
		   throw new RuntimeException(e);
		}
		return result;
	}

	/**
	 * Stream受信（HTTPS要求→ダウンロード）.
	 * @param outconsumer 送信 OutputStream Consumer
	 * @param headconsumer HTTPS通信結果、受け取ったHTTP ContentType、HTTPヘッダを読取り処理する BiConsumer
	 * @param readconsumer 要求した結果の受け取り InputStream を指定する Consumer
	 */
	public void execute(Consumer<OutputStream> outconsumer, BiConsumer<String, Map<String, List<String>>> headconsumer, Consumer<InputStream> readconsumer){
		try{
			SSLContext ctx = SSLContext.getInstance("SSL");
			ctx.init(null, new X509TrustManager[]{ new NonAuthentication() }, null);
			SSLSocketFactory factory = ctx.getSocketFactory();

			HttpsURLConnection uc;
			if (proxy_server != null){
				// Proxy利用
				if (proxy_user != null && proxy_passwd != null){
					Authenticator.setDefault(new Authenticator(){
						@Override
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(proxy_user, proxy_passwd.toCharArray());
						}
					});
				}
				Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxy_server, Optional.ofNullable(proxy_port).orElse(80)));
				uc = (HttpsURLConnection)url.openConnection(proxy);
			}else{
				uc = (HttpsURLConnection)url.openConnection();
			}
			uc.setSSLSocketFactory(factory);

			/* HTTPリクエストヘッダの設定 */
			uc.setDoOutput(true);              // こちらからのデータ送信を可能とする
			uc.setReadTimeout(0);              // 読み取りタイムアウト値をミリ秒単位で設定(0は無限)
			uc.setRequestMethod(method);       // URL 要求のメソッドを設定
			if (contentType != null) uc.setRequestProperty("Content-Type", contentType);
			// コネクション確立→送信
			uc.connect();

			try(OutputStream out = uc.getOutputStream()){
				outconsumer.accept(out);
				out.flush();
			}

			httpresponsecode = uc.getResponseCode();
			if (httpresponsecode != 200){
				throw new RuntimeException("HTTP response " + httpresponsecode);
			}
			// 戻り値取得
			headconsumer.accept(uc.getContentType(), uc.getHeaderFields());
			// 応答読込
			readconsumer.accept(uc.getInputStream());
		}catch(Exception e){
		   throw new RuntimeException(e);
		}
	}

	/**
	 * HTTP通信時のレスポンスコードを返す.
	 * @return httpresponsecode HTTP通信時のレスポンスコード、正常時は 200
	 */
	public int getHttpResponsecode(){
		return httpresponsecode;
	}
}
