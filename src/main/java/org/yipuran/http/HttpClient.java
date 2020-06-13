package org.yipuran.http;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * HttpClient.
 * HttpClientBuilder で HttpClientインスタンスを生成する。
 *  <PRE>
 * 要求先 URL パス、要求の種類(POST/GET)、要求するパラメータは、HttpClientBuilder で指定する。
 * HttpClientBuilder を使用することで送信するパラメータ値のエンコードは保証される。
 *
 * HttpClient client = HttpClientBuilder.of("http://xxx/xxx")
 *                    .method("POST")
 *                    .contentType("application/json; charset=utf-8")
 *                    .build();
 *
 * public void execute(Consumer&lt;OutputStream&gt; outconsumer, BiConsumer&lt;String, Map&lt;String, List&lt;String&gt;&gt;&gt; headconsumer, Consumer&lt;InputStream&gt; inconsumer)
 *
 * int status = client.execute(ThrowableConsumer.of(out->{
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
 * 注意すべきは、１つのインスタンスで併用はできない。
 *  </PRE>
 */
public class HttpClient{
	private URL url;
	private String method;
	private String contentType;
	private Map<String, String> headerOptions;

	/**
	 * コンストラクタ.
	 * @param url HTTP先URL
	 * @param method HTTPメソッド
	 * @param map パラメータMap 値は、URLエンコードされてなければならない。HttpClientBuilder を使用することで送信するパラメータ値のエンコードは保証される。
	 * @param contentType Content-Type をHTTPヘッダにつける場合に、null 以外を指定
	 */
	protected HttpClient(URL url, String method, String contentType, Map<String, String> headerOptions){
		this.url = url;
		this.method = method;
		this.contentType = contentType;
		this.headerOptions = headerOptions;
	}
	/**
	 * HTTP要求送受信.
	 * @param outconsumer 送信 OutputStream Consumer
	 * @param headconsumer HTTP通信結果、受け取ったHTTP ContentType、HTTPヘッダを読取り処理する BiConsumer
	 * @param inconsumer 要求した結果の受け取り InputStream を指定する Consumer
	 * @return 要求した結果を受信したテキスト文字列。受信失敗は、null を返す。
	 */
	public int execute(Consumer<OutputStream> outconsumer, BiConsumer<String, Map<String, List<String>>> headconsumer, Consumer<InputStream> inconsumer){
		int status;
		try{
			HttpURLConnection uc = (HttpURLConnection)url.openConnection();
			/* HTTPリクエストヘッダの設定 */
			uc.setDoOutput(true);              // こちらからのデータ送信を可能とする
			uc.setReadTimeout(0);              // 読み取りタイムアウト値をミリ秒単位で設定(0は無限)
			uc.setRequestMethod(method);       // URL 要求のメソッドを設定
			if (contentType != null) uc.setRequestProperty("Content-Type", contentType);
			if (headerOptions.size() > 0) {
				headerOptions.entrySet().stream().forEach(e->{
					uc.setRequestProperty(e.getKey(), e.getValue());
				});
			}
			// コネクション確立→送信
			uc.connect();

			try(OutputStream out = uc.getOutputStream()){
				outconsumer.accept(out);
				out.flush();
			}

			status = uc.getResponseCode();

			// Header 戻り値取得
			headconsumer.accept(uc.getContentType(), uc.getHeaderFields());
			// 応答読込
			inconsumer.accept(uc.getInputStream());
		}catch(Exception e){
		   throw new RuntimeException(e);
		}
		return status;
	}
}
