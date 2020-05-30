package org.yipuran.regex;


import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
/**
 * 置換Map指定正規表現Replace処理.
 * <PRE>
 * Map＜String, String＞ map = new HashMap＜＞();
 *
 * Map<String, String> map = new HashMap<>();
 * map.put("aa", "あ");
 * map.put("bb", "い");
 * map.put("cc", "う");
 *
 * RegexMap r = RegexMap.of("[a-z]+", map);
 * String res = r.replace(input);
 * r.addRegex("cc", "Ｕ");
 * String res2 = r.replace(input);
 *
 * </PRE>
 * @since 4.20
 */
public final class RegexMap{
	private Map<String, String> replaceMap;
	private String regex;

	private RegexMap(String regex, Map<String, String> replaceMap){
		this.regex = regex;
		this.replaceMap = replaceMap;
	}
	/**
	 * インスタンス生成
	 * @param regex 正規表現
	 * @param replaceMap 置換Map key=正規表現に一致した文字列、value=置換文字列
	 * @return RegexMap
	 */
	public static RegexMap of(String regex, Map<String, String> replaceMap){
		return new RegexMap(regex, replaceMap);
	}
	/**
	 * 正規表現設定.
	 * @param regex
	 */
	public void setRegex(String regex) {
		this.regex = regex;
	}
	/**
	 * 置換Key-Value 追加.
	 * @param match 正規表現に一致した文字列
	 * @param replacestr 置換文字列
	 */
	public void addRegex(String match, String replacestr) {
		replaceMap.put(match, replacestr);
	}
	/**
	 * 置換Mapに従った正規表現マッチ置換実行
	 * @param input 対象文字列
	 * @return 置換処理後の文字列
	 */
	public String replace(String input) {
		Matcher m = Pattern.compile(regex).matcher(input);
		AtomicInteger i = new AtomicInteger(0);
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(new Iterator<String>(){
			@Override
			public boolean hasNext(){
				return m.find();
			}
			@Override
			public String next(){
				return input.substring(i.getAndSet(m.end()), m.start())
						+ Optional.ofNullable(replaceMap.get(m.group())).orElse(m.group());
			}
		}, Spliterator.ORDERED), false).collect(Collectors.joining()) + input.substring(i.get());
	}
}
