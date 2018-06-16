package org.yipuran.xml;

import org.xml.sax.helpers.DefaultHandler;

/**
 * ＸＭＬ解析ハンドラ抽象クラス.
 *
 * 実装クラスで、org.xml.sax.helpers.DefaultHandler のメソッドをオーバライドして
 * ＸＭＬ解析の結果を result() で、返すように実装すること。
 * @param <T> XML解析結果オブジェクトGeneric
 */
public abstract class AbstractXmlHandler<T> extends DefaultHandler{
	/**
	 * 解析結果取得.
	 * @return 解析結果オブジェクト
	 */
	public abstract T result();

	/**
	 * XmlParser インスタンス生成.
	 * ３つの引数を持つ同じメソッド、createParser の３番目に、
	 * ResourceBundle.Control.TTL_DONT_CACHE を与えて実行するのと同じ効果があります。
	 * @param <T> result()が返す任意オブジェクトタイプ
	 * @param baseName XMLファイル ".xml" を除いた名称
	 * @param handler AbstractXmlHandler継承インスタンス
	 * @return XmlParserインスタンス
	 */
	@SuppressWarnings("unchecked")
	public static final <T> XmlParser<T> createParser(String baseName, AbstractXmlHandler<T> handler){
		return new XmlParserImpl<T>(baseName, handler);
	}

	/**
	 * XmlParser インスタンス生成（XML読込キャッシュ制御）.
	 * ResourceBundle.Control の下でロードされたリソースバンドルの有効期間 (TTL) 値を指定する
	 * ことを可能にしたインスタンス生成です。
	 * 第３引数に、
	 * ResourceBundle.Control.TTL_DONT_CACHE    ：キャッシュをしない
	 * ResourceBundle.TTL_NO_EXPIRATION_CONTROL ：有効期限なしのキャッシュをする。
	 * あるいは、値 0 は、ResourceBundle.Control#getTimeToLive の戻り値＝０と同じ意味を持ち、
	 * キャッシュ有効期限をミリ秒で与えます。
	 * @param <T> result()が返す任意オブジェクトタイプ
	 * @param baseName XMLファイル ".xml" を除いた名称
	 * @param handler AbstractXmlHandler継承インスタンス
	 * @param cacheTime ResourceBundle.Control の下でロードされたリソースバンドルの有効期間 (TTL) 値、ミリ秒
	 * @return XmlParserインスタンス
	 */
	@SuppressWarnings("unchecked")
   public static final <T> XmlParser<T> createParser(String baseName, AbstractXmlHandler<T> handler, long cacheTime){
		return new XmlParserImpl<T>(baseName, handler, cacheTime);
	}
	/**
	 * XmlParser インスタンス生成.
	 * @param <T> result()が返す任意オブジェクトタイプ
	 * @param cls xmlファイルと同じ場所のClass
	 * @param filename xmlファイルファイル名
	 * @param handler AbstractXmlHandler継承インスタンス
	 * @return XmlParserインスタンス
	 */
	@SuppressWarnings("unchecked")
	public static final <T> XmlParser<T> createParser(Class<?> cls, String filename, AbstractXmlHandler<T> handler){
		return new XmlParserImpl<T>(cls.getPackage().getName().replaceAll("\\.", "/") + "/" + filename.replaceAll("\\.xml$", ""), handler);
	}
}
