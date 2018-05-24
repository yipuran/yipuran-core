package org.yipuran.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * XmlParser 実装クラス.
 * 解析対象 XMLファイル baseName と、AbstractXmlHandler インスタンスをコンストラクタで受け取る。
 * @param <T> XML解析結果オブジェクトGeneric
 */
final class XmlParserImpl<T> implements XmlParser{
	private String baseName;
	AbstractXmlHandler<T> handler;
	long cacheTime;

	/**
	 * コンストラクタ.
	 * @param baseName String
	 * @param handler AbstractXmlHandler<T>
	 */
	protected XmlParserImpl(String baseName, AbstractXmlHandler<T> handler){
		this.baseName = baseName;
		this.handler = handler;
		this.cacheTime = ResourceBundle.Control.TTL_DONT_CACHE;
	}
	/**
	 * コンストラクタ.
	 * @param baseName String
	 * @param handler  AbstractXmlHandler<T>
	 * @param cacheTime long
	 */
	protected XmlParserImpl(String baseName, AbstractXmlHandler<T> handler, long cacheTime){
		this.baseName = baseName;
		this.handler = handler;
		this.cacheTime = cacheTime;
	}

	/*
	 * @see XmlParser#parse()
	 */
	@Override
	public Object parse(){
		ResourceBundle.getBundle(this.baseName	, new ResourceBundle.Control(){
			@Override
			public List<String> getFormats(String base_Name){
				if (base_Name==null) throw new NullPointerException();
				return Arrays.asList("xml");
			}
			@Override
			public ResourceBundle newBundle(String base_Name, Locale locale, String format, ClassLoader loader, boolean reload)
					throws IllegalAccessException, InstantiationException, IOException{
				if (base_Name == null || locale == null || format == null || loader == null)
					throw new NullPointerException();
				if (format.equals("xml")){
					String bundleName = toBundleName(base_Name, locale);
					String resourceName = toResourceName(bundleName, format);
					try(InputStream stream = loader.getResourceAsStream(resourceName)){
						MXMLparse xmlParse = new MXMLparse(stream);
						xmlParse.parse();
					}catch(Exception e){
						throw new IOException(e);
					}
				}
				return new DummyBundle();
			}
			@Override
			public long getTimeToLive(String base_Name, Locale llocale){
				if (base_Name==null || llocale==null){
					throw new NullPointerException();
				}
				return XmlParserImpl.this.cacheTime;
			}
		});
		return this.handler.result();
	}
	//------------------------------------------------
	/**
	 * XMLparse.
	 */
	class MXMLparse {
		private InputStream steam;
		/**
		 * constructor.
		 * @param stream InputStream
		 */
		MXMLparse(InputStream stream){
			this.steam = stream;
		}
		/**
		 * parse.
		 * @throws Exception Error
		 */
		void parse() throws Exception{
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(this.steam, XmlParserImpl.this.handler);
		}
	}
	//------------------------------------------------
	/**
	 * DummyBundle .
	 */
	class DummyBundle extends ResourceBundle {
		private Properties props;
		/** constrctor. */
		DummyBundle(){
			this.props = new Properties();
		}
		@Override
		protected Object handleGetObject(String key) {
			return this.props.getProperty(key);
		}
		@Override
		public Enumeration<String> getKeys() {
			return null;
		}
	}
}
