package org.yipuran.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import org.yipuran.function.ThrowableConsumer;

/**
 * key-valueを示す文字列配列リストからのオブジェクト生成.
 * <PRE>
 * 「key-valueを示す文字列」＝セパレータ文字（デフォルト'='）で連結した文字列の配列、もしくはリストから
 * key名属性に、valueを属性値とする任意クラスのオブジェクトを生成する。
 *
 * key-value の配列、コレクション、Stream からの３つ方法でオブジェクトをする。
 * key-valueを連結する文字、セパレータ文字はデフォルトで、'=' 文字、コンストラクタまたは設定メソッドで変更する。
 * サポートしている属性値の型は、
 * String, Boolean, Integer, Long, Double, Float, Short, Character, Byte,
 * boolean, int, long, double, short, char, byte, java.math.BigInteger, java.math.BigDecimal,
 * java.time.LocalDate, java.time.LocalDateTime, jaav.time.LocalTime
 *
 * java.time.LocalDate, java.time.LocalDateTime, jaav.time.LocalTime については、
 * デフォルトで次のDateTimeFormatter で読みとれるようになっているが設定メソッドで変更することができる。
 *
 * LocalDateTimeとして読込む為のDateTimeFormatter設定：　DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
 * LocalDateとして読込む為のDateTimeFormatter設定　　：　DateTimeFormatter.ofPattern("yyyy-MM-dd")
 * LocalTimeとして読込む為のDateTimeFormatter設定　　：　DateTimeFormatter.ofPattern("HH:mm")
 *
 * 属性名（key）にセットする関数型インターフェース Function＜String, ?＞を指定することで、
 * Function が、value(String)からセットする属性値をセットするようにする設定メソッドが存在する
 *
 * （サンプル）
 * ＠Data
 * public class Foo{
 *    private int point;
 *    private String name;
 *    private boolean flg;
 *    private LocalDate date;
 *    private List<String> list;
 * }
 *
 * KeyValueToObject&lt;Foo&gt; argsToObject = new KeyValueToObject&lt;&gt;(Foo.class)
 * .setDeserilaizer("date", s->LocalDate.parse(s, DateTimeFormatter.ofPattern("yyyy/MM/dd")))
 * .setDeserilaizer("list", ThrowableFunction.of(s-&gt;new ObjectMapper().readValue(s, new TypeReference&lt;List&lt;String&gt;&gt;(){})));
 *
 * Foo foo = argsToObject.parse("name=ABC", "flg=true", "point=100","date=2022/08/24", "list=[ \"A\", \"B\", \"C\" ]");
 *
 * </PRE>
 * @since 4.36
 */
public class KeyValueToObject<T>{
	private Class<?> cls;
	private String separator = "=";
	private Constructor<T> con;
	private DateTimeFormatter dateform = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
	private DateTimeFormatter timeform = DateTimeFormatter.ofPattern("HH:mm");
	private DateTimeFormatter dayform = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private Map<String, Function<String, ?>> map;

	/**
	 * コンストラクタ
	 * @param c 生成クラス
	 */
	public KeyValueToObject(Class<T> c) {
		cls = c;
		map = new HashMap<>();
		try{
			con = c.getConstructor();
			con.newInstance();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	public KeyValueToObject(Class<T> c, String separator) {
		this.separator = separator;
		cls = c;
		map = new HashMap<>();
		try{
			con = c.getConstructor();
			con.newInstance();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	/**
	 * セパレータ設定
	 * @param separator 配列要素をセパレートする文字
	 * @return KeyValueToObject
	 */
	public KeyValueToObject<T> setSeparator(String separator) {
		this.separator = separator;
		return this;
	}
	/**
	 * デシリアライザ登録
	 * @param key セパレート後のキー
	 * @param function セパレート後の値文字列か生成オブジェクトにセットする値を返すFunction
	 * @return KeyValueToObject
	 */
	public KeyValueToObject<T> setDeserilaizer(String key, Function<String, ?> function) {
		map.put(key, function);
		return this;
	}
	/**
	 * LocalDateTimeとして読込む為のDateTimeFormatter設定
	 * @param formatter
	 * @return KeyValueToObject
	 */
	public KeyValueToObject<T> setLocalDateTimeFormatter(DateTimeFormatter formatter){
		dateform = formatter;
		return this;
	}
	/**
	 * LocalTimeとして読込む為のDateTimeFormatter設定
	 * @param formatter
	 * @return KeyValueToObject
	 */
	public KeyValueToObject<T> setLocalTimeFormatter(DateTimeFormatter formatter){
		timeform = formatter;
		return this;
	}
	/**
	 * LocalDateとして読込む為のDateTimeFormatter設定
	 * @param formatter
	 * @return KeyValueToObject
	 */
	public KeyValueToObject<T> setLocalDateFormatter(DateTimeFormatter formatter){
		dayform = formatter;
		return this;
	}
	/**
	 * 可変長文字列配列⇒オブジェクト生成
	 * @param args セパレータでkey-valueを連結した文字列配列
	 * @return T
	 */
	public T parse(String...args) {
		return parse(Arrays.stream(args));
	}
	/**
	 * セパレータでkey-valueを連結した文字列コレクション⇒オブジェクト生成
	 * @param collection セパレータでkey-valueを連結した文字列コレクション
	 * @return T
	 */
	public T parse(Collection<String> collection) {
		return parse(collection.stream());
	}
	/**
	 * セパレータでkey-valueを連結した文字列ストリーム⇒オブジェクト生成
	 * @param stream セパレータでkey-valueを連結した文字列コレクションストリーム
	 * @return T
	 */
	public T parse(Stream<String> stream) {
		try{
			T t = con.newInstance();
			stream.forEach(ThrowableConsumer.of(e->{
				String[] sp = e.split(separator);
				Field field = null;
				while(cls != null){
					try{
						field = cls.getDeclaredField(sp[0]);
						break;
					}catch(NoSuchFieldException x){
						cls = cls.getSuperclass();
					}
				}
				field.setAccessible(true);
				if (map.containsKey(sp[0])) {
					field.set(t, map.get(sp[0]).apply(sp[1]));
				}else{
					String typename = field.getType().getName();
					if ("java.lang.String".equals(typename)) {
						field.set(t, sp[1]);
					}else if("java.lang.Boolean".equals(typename)) {
						field.set(t, Boolean.valueOf(sp[1].toLowerCase()));
					}else if("java.lang.Integer".equals(typename)) {
						field.set(t, Integer.valueOf(sp[1]));
					}else if("java.lang.Long".equals(typename)) {
						field.set(t, Long.valueOf(sp[1]));
					}else if("java.lang.Double".equals(typename)) {
						field.set(t, Double.valueOf(sp[1]));
					}else if("java.lang.Float".equals(typename)) {
						field.set(t, Float.valueOf(sp[1]));
					}else if("java.lang.Short".equals(typename)) {
						field.set(t, Short.valueOf(sp[1]));
					}else if("java.lang.Character".equals(typename)) {
						field.set(t, (char)Integer.parseInt(sp[1]));
					}else if("java.lang.Byte".equals(typename)) {
						field.set(t, Byte.decode(sp[1]));
					}else if("boolean".equals(typename)) {
						field.setBoolean(t, Boolean.parseBoolean(sp[1].toLowerCase()));
					}else if("int".equals(typename)) {
						field.setInt(t, Integer.parseInt(sp[1]));
					}else if("long".equals(typename)) {
						field.setLong(t, Long.parseLong(sp[1]));
					}else if("double".equals(typename)) {
						field.setDouble(t, Double.parseDouble(sp[1]));
					}else if("float".equals(typename)) {
						field.setFloat(t, Float.parseFloat(sp[1]));
					}else if("short".equals(typename)) {
						field.setShort(t, Short.parseShort(sp[1]));
					}else if("java.math.BigDecimal".equals(typename)) {
						field.set(t, new BigDecimal(sp[1]));
					}else if("char".equals(typename)) {
						field.setChar(t, (char)Integer.parseInt(sp[1]));
					}else if("byte".equals(typename)) {
						field.set(t, Byte.decode(sp[1]));
					}else if("java.math.BigInteger".equals(typename)) {
						field.set(t, new BigInteger(sp[1]));
					}else if("java.time.LocalDate".equals(typename)) {
						field.set(t, LocalDate.parse(sp[1], dayform));
					}else if("java.time.LocalDateTime".equals(typename)) {
						field.set(t, LocalDateTime.parse(sp[1], dateform));
					}else if("jaav.time.LocalTime".equals(typename)) {
						field.set(t, LocalTime.parse(sp[1], timeform));
					}
				}
			}));
			return t;
		}catch (Exception e){
			throw new RuntimeException(e);
		}
	}
}
