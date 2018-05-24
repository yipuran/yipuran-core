package org.yipuran.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.yipuran.xml.AbstractXmlHandler;

/**
 * HTTP通信ユーティリティ.
 * <pre>
 * 指定ＵＲＬへの HTTP送信または、HTTP(BASIC認証)送信を行う。
 * </pre>
 * @author dvctcyamada
 */
public final class HttpUtil{
	/** HTTPヘッダ属性名 "Content-Type" */
	public final static String KEY_CONTENT_TYPE = "Content-Type";    // text/xml
	/** HTTPヘッダ属性名 "Content-Type" */
	public final static String KEY_CONNECTION   = "Connection";      // close

	/** HTTPメソッド名 POST*/
	public final static String POST = "POST";

	/** HTTPメソッド名 GET*/
	public final static String GET = "GET";

	private HttpUtil(){}

	/**
	 * ＨＴＴＰ送信
	 * @param url ＵＲＬ
	 * @param headerMap 指定するHTTPヘッダをMap<String,String>で指定する.<br>
	 * HttpURLConnection#setRequestProperty(String,String)実行対象のMapである
	 * @param method リクエストメソッド GET or POST
	 * @param str 送信データ
	 * @param timeout 接続タイムアウト時間（ミリ秒）
	 * @param encode エンコード
	 * @return 応答レスポンス HttpUtilzResponseを参照してください
	 * @throws Exception
	 */
	public static HttpUtilResponse send(URL url,Map<String,String> headerMap, String method,String str,int timeout,String encode) throws Exception {
	   return send(url,headerMap,null,null,method,str,timeout,encode);
	}

	/**
	 * ＨＴＴＰ送信（HTTPヘッダデフォルト）
	 * <pre>
	 * HTTPヘッダデフォルトは、以下
	 * Content-Type : text/xml
	 * Connection : close
	 * </pre>
	 * @param url ＵＲＬ
	 * @param str 送信データ
	 * @param timeout 接続タイムアウト時間（ミリ秒）
	 * @return 応答レスポンス HttpUtilzResponseを参照してください
	 * @throws Exception
	 */
	public static HttpUtilResponse send(URL url,String str,int timeout) throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		map.put(KEY_CONTENT_TYPE,"text/xml");
		map.put(KEY_CONNECTION,"close");
		return send(url,map,POST,str,timeout,"UTF-8");
	}
	/**
	 * ＨＴＴＰ送信（デフォルト）.
	 * <br> デフォルトでは、接続タイムアウト時間は、３０秒で、HTTPヘッダデフォルトである
	 * @param url ＵＲＬ
	 * @param str 送信データ
	 * @return 応答レスポンス HttpUtilzResponseを参照してください
	 * @throws Exception
	 */
	public static HttpUtilResponse send(URL url,String str) throws Exception{
		return send(url,str,30000);
	}

	/**
	 * ＨＴＴＰ送信 BASIC認証
	 * @param url ＵＲＬ
	 * @param headerMap 指定するHTTPヘッダをMap<String,String>で指定する.<br>
	 * HttpURLConnection#setRequestProperty(String,String)実行対象のMapである
	 * @param user ユーザＩＤ,user,passwd共にNULLであれば、BASIC認証なし
	 * @param passwd パスワード,user,passwd共にNULLであれば、BASIC認証なし
	 * @param method メソッド名
	 * @param str 送信データ
	 * @param timeout 接続タイムアウト時間（ミリ秒）
	 * @param encode エンコード
	 * @return 応答レスポンス HttpUtilzResponseを参照してください
	 * @throws Exception
	 */
	public static HttpUtilResponse send(URL url,Map<String,String> headerMap,String user,String passwd,String method,String str,int timeout,String encode) throws Exception{
		HttpURLConnection uc = null;
		OutputStreamWriter osw = null;
		HttpUtilResponse res = new HttpUtilResponse();
		try{
			// HttpURLConnectionオブジェクト取得
			uc = (HttpURLConnection)url.openConnection();
			/** HTTPリクエストヘッダの設定 */
			uc.setDoOutput(true);             // こちらからのデータ送信を可能とする
			uc.setReadTimeout(timeout);        // 読み取りタイムアウト値をミリ秒単位で設定(0は無限)
			uc.setRequestMethod(method);       // URL 要求のメソッドを設定
			SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss",Locale.US);
			uc.setRequestProperty("Date"," "+sdf.format(new Date())+" GMT");
			if (user != null && passwd != null){
				// 認証用パラメータを作成する。(認証ID,パスワード)
				uc.setRequestProperty("Authorization"," Basic "+Base64Util.base64Encode(user+":"+passwd));
			}
			for(Iterator<String> it=headerMap.keySet().iterator();it.hasNext();){
				String key = it.next();
				uc.setRequestProperty(key,headerMap.get(key));
			}

			// Content-LengthはPOSTの場合だけ
			if (method.equals(POST)){
				uc.setRequestProperty("Content-Length",Integer.toString(str.getBytes(encode).length));
				//コネクション確立
				uc.connect();
				osw = new OutputStreamWriter(uc.getOutputStream(), encode);
				osw.write(str);
				osw.flush();
			}

			/** 戻り値取得 */
			res.setContentType(uc.getContentType());

			Map<String,List<String>> hmap = uc.getHeaderFields();
			List<String> hlist = hmap.get("Content-Language");
			if (hlist != null && hlist.size()==1){
				res.setContentLanguage(hlist.get(0));
			}
			hlist = hmap.get("Date");
			if (hlist != null && hlist.size()==1){
				res.setContentDate(hlist.get(0));
			}
			hlist = hmap.get("Content-Length");
			if (hlist != null && hlist.size()==1){
				res.setContentLength(hlist.get(0));
			}
			res.setResponseCode(uc.getResponseCode());
			res.setResponseMessage(uc.getResponseMessage());
			StringBuffer out = new StringBuffer();
			try{
				try(BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream(),encode))){
					char[] buf = new char[1024];
					int n;
					while((n = in.read(buf)) >= 0){
						out.append(buf, 0, n);
					}
				}
			}catch(IOException e){
				throw new RuntimeException(e);
			}
			res.setMessage(out.toString());
		}catch(IOException e){
			res.setResponseMessage(e.getMessage());
			if (uc != null){
					StringBuffer out_e = new StringBuffer();
					char[] buf = new char[1024];
					int n;
					try(BufferedReader in_e = new BufferedReader(new InputStreamReader(uc.getErrorStream(), encode))){
						while((n = in_e.read(buf)) >= 0){
							out_e.append(buf, 0, n);
						}
					}
					res.setMessage(out_e.toString());
			}
		}catch(Exception e){
			throw e;
		}finally{
			if (osw != null){ osw.close(); }
			if (uc != null){ uc.disconnect(); }
		}
		return res;
	}
	/**
	 * ＨＴＴＰ送信 BASIC認証（HTTPヘッダデフォルト）.
	 * <pre>
	 * HTTPヘッダデフォルトは、以下
	 * Content-Type : text/xml
	 * Connection : close
	 * </pre>
	 * @param url ＵＲＬ
	 * @param user ユーザＩＤ
	 * @param passwd パスワード
	 * @param str 送信データ
	 * @param timeout 接続タイムアウト時間（ミリ秒）
	 * @return 応答レスポンス HttpUtilzResponseを参照してください
	 * @throws Exception
	 */
	public static HttpUtilResponse send(URL url,String user,String passwd,String str,int timeout) throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		map.put(KEY_CONTENT_TYPE,"text/xml");
		map.put(KEY_CONNECTION,"close");
		return send(url,map,user,passwd,str,POST,timeout,"UTF-8");
	}

	/**
	 * ＨＴＴＰ送信 BASIC認証（デフォルト）.
	 * <br> デフォルトでは、接続タイムアウト時間は、３０秒で、HTTPヘッダデフォルトである
	 * @param url ＵＲＬ
	 * @param user ユーザＩＤ
	 * @param passwd パスワード
	 * @param str 送信データ
	 * @return 応答レスポンス HttpUtilzResponseを参照してください
	 * @throws Exception
	 */
	public static HttpUtilResponse send(URL url,String user,String passwd,String str) throws Exception{
		return send(url,user,passwd,str,30000);
	}

	/**
	 * ＨＴＴＰ送信 BASIC認証（メソッド指定）.
	 * <br> デフォルトでは、接続タイムアウト時間は、３０秒で、HTTPヘッダデフォルトである
	 * @param url ＵＲＬ
	 * @param user ユーザＩＤ
	 * @param passwd パスワード
	 * @param method メソッド GET or POST
	 * @param str 送信データ
	 * @return 応答レスポンス HttpUtilzResponseを参照してください
	 * @throws Exception
	 */
	public static HttpUtilResponse send(URL url,String user,String passwd, String method, String str) throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		map.put(KEY_CONTENT_TYPE, "text/plain");
		map.put(KEY_CONNECTION, "close");
		return send(url,map,user,passwd,str,method,30000,"UTF-8");
	}
	/**
	 * ＨＴＴＰ送信 返信が XML 形式の時のハンドラ指定
	 * param url ＵＲＬ
	 * @param headerMap 指定するHTTPヘッダをMap<String,String>で指定する.<br>
	 * HttpURLConnection#setRequestProperty(String,String)実行対象のMapである
	 * @param method リクエストメソッド GET or POST
	 * @param str 送信データ
	 * @param timeout 接続タイムアウト時間（ミリ秒）
	 * @param encode エンコード
	 * @param handler XML解析ハンドラ 応答メッセージは、実行後に、ハンドラの result() で取得します。
	 * HttpUtilzResponse では参照できません。
	 * @return 応答レスポンス HttpUtilzResponseを参照してください、メッセージは、ハンドラの result() で取得します。
	 * @throws Exception
	 */
	public static HttpUtilResponse send(URL url,Map<String,String> headerMap, String method,String str,int timeout,String encode,AbstractXmlHandler handler) throws Exception {
		return send(url,headerMap,null,null,method,str,timeout,encode,handler);
	}
	/**
	 * ＨＴＴＰ送信 BASIC認証 返信が XML 形式の時のハンドラ指定
	 * @param url ＵＲＬ
	 * @param headerMap 指定するHTTPヘッダをMap<String,String>で指定する.<br>
	 * HttpURLConnection#setRequestProperty(String,String)実行対象のMapである
	 * @param user ユーザＩＤ,user,passwd共にNULLであれば、BASIC認証なし
	 * @param passwd パスワード,user,passwd共にNULLであれば、BASIC認証なし
	 * @param method メソッド名
	 * @param str 送信データ
	 * @param timeout 接続タイムアウト時間（ミリ秒）
	 * @param encode エンコード
	 * @param handler XML解析ハンドラ 応答メッセージは、実行後に、ハンドラの result() で取得します。
	 * HttpUtilzResponse では参照できません。
	 * @return 応答レスポンス HttpUtilzResponseを参照してください メッセージは、ハンドラの result() で取得します。
	 * @throws Exception
	 */
	public static HttpUtilResponse send(URL url,Map<String,String> headerMap,String user,String passwd,String method,String str,int timeout,String encode,AbstractXmlHandler handler) throws Exception{
		HttpURLConnection uc = null;
		OutputStreamWriter osw = null;
		HttpUtilResponse res = new HttpUtilResponse();
		try{
			// HttpURLConnectionオブジェクト取得
			uc = (HttpURLConnection)url.openConnection();
			/** HTTPリクエストヘッダの設定 */
			uc.setDoOutput(true);             // こちらからのデータ送信を可能とする
			uc.setReadTimeout(timeout);        // 読み取りタイムアウト値をミリ秒単位で設定(0は無限)
			uc.setRequestMethod(method);       // URL 要求のメソッドを設定
			SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss",Locale.US);
			uc.setRequestProperty("Date"," "+sdf.format(new Date())+" GMT");
			if (user != null && passwd != null){
				// 認証用パラメータを作成する。(認証ID,パスワード)
				uc.setRequestProperty("Authorization"," Basic "+Base64Util.base64Encode(user+":"+passwd));
			}
			for(Iterator<String> it=headerMap.keySet().iterator();it.hasNext();){
				String key = it.next();
				uc.setRequestProperty(key,headerMap.get(key));
			}

			// Content-LengthはPOSTの場合だけ
			if (method.equals(POST)){
				uc.setRequestProperty("Content-Length",Integer.toString(str.getBytes(encode).length));
				//コネクション確立
				uc.connect();
				osw = new OutputStreamWriter(uc.getOutputStream(), encode);
				osw.write(str);
				osw.flush();
			}

			/** 戻り値取得 */
			res.setContentType(uc.getContentType());

			Map<String,List<String>> hmap = uc.getHeaderFields();
			List<String> hlist = hmap.get("Content-Language");
			if (hlist != null && hlist.size()==1){
			   res.setContentLanguage(hlist.get(0));
			}
			hlist = hmap.get("Date");
			if (hlist != null && hlist.size()==1){
			   res.setContentDate(hlist.get(0));
			}
			hlist = hmap.get("Content-Length");
			if (hlist != null && hlist.size()==1){
			   res.setContentLength(hlist.get(0));
			}
			res.setResponseCode(uc.getResponseCode());
			res.setResponseMessage(uc.getResponseMessage());
			//BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream(),encode));

			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(uc.getInputStream(),handler);

			//res.setMessage(out.toString());
		}catch(IOException e){
			res.setResponseMessage(e.getMessage());
			if (uc != null){
				StringBuffer out_e = new StringBuffer();
				char[] buf = new char[1024];
				int n;
				try(BufferedReader in_e = new BufferedReader(new InputStreamReader(uc.getErrorStream(), encode))){
					while((n = in_e.read(buf)) >= 0){
						out_e.append(buf, 0, n);
					}
				}
				res.setMessage(out_e.toString());
			}
		}catch(Exception e){
			throw e;
		}finally{
			if (osw != null){ osw.close(); }
			if (uc != null){ uc.disconnect(); }
		}
		return res;
	}
}
