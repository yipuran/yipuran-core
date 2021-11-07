package org.yipuran.xml;


/**
 * XML 解析インターフェース.
 * <PRE>
 * AbstractXmlHandler の createParser メソッドで、XMLファイル baseName と
 * AbstractXmlHandler 継承したインスタンスを生成して作成される。
 *
 * XmlParser xmlparser = AbstractXmlHandler.createParser(baseName,handler);
 * </PRE>
 * @param <T> XML解析結果オブジェクトGeneric
 */
public interface XmlParser<T>{

	/**
	 * ＸＭＬ解析.
	 * <PRE>
	 * AbstractXmlHandler継承インスタンスが AbstractXmlHandler抽象メソッドである
	 * public abstract T result() throws Exception;
	 * が返す任意のオブジェクトを返す。
	 * </PRE>
	 * @return T AbstractXmlHandler継承インスタンスが返す任意オブジェクト
	 */
	public T parse();

}
