package org.yipuran.regex;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.Spliterators.AbstractSpliterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * 正規表現ユーティリティ.
 * @since 4.7
 */
public final class RegExpress{
	private RegExpress(){
	}
	/**
	 * 正規表現マッチ文字列 Stream.
	 * @param regex 正規表現
	 * @param input 検査対象
	 * @return マッチ文字列 Stream
	 */
	public static Stream<String> matchToStream(String regex, CharSequence input){
		Matcher m = Pattern.compile(regex).matcher(input);
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(new Iterator<String>(){
			@Override
			public boolean hasNext(){
				return m.find();
			}
			@Override
			public String next(){
				return m.group();
			}
		}, Spliterator.ORDERED), false);
	}
	/**
	 * MatchResult Stream の取得.
	 * @param regex 正規表現
	 * @param input 検査対象
	 * @return MatchResultの Stream
	 */
	public static Stream<MatchResult> findMatches(String regex, CharSequence input){
		Matcher matcher = Pattern.compile(regex).matcher(input);
		Spliterator<MatchResult> spliterator = new AbstractSpliterator<MatchResult>(Long.MAX_VALUE, Spliterator.ORDERED | Spliterator.NONNULL){
			@Override
			public boolean tryAdvance(Consumer<? super MatchResult> action){
				if (!matcher.find())
					return false;
				action.accept(matcher.toMatchResult());
				return true;
			}
		};
		return StreamSupport.stream(spliterator, false);
	}
	/**
	 * 正規表現マッチ文字列 Consumer.
	 * @param regex 正規表現
	 * @param input 検査対象
	 * @param c マッチ文字列 Consumer
	 */
	public static void results(String regex, String input, Consumer<String> c){
		Matcher m = Pattern.compile(regex).matcher(input);
		while(m.find()){
			c.accept(m.group());
		}
	}
	/**
	 * MatchResult Consumer処理.
	 * @param regex 正規表現
	 * @param input 検査対象
	 * @param c MatchResult Consumer
	 */
	public static void resultMatch(String regex, String input, Consumer<MatchResult> c){
		Matcher m = Pattern.compile(regex).matcher(input);
		while(m.find()){
			c.accept(m.toMatchResult());
		}
	}
	/**
	 * MatchResult BiConsumer処理.
	 * @param regex 正規表現
	 * @param input 検査対象
	 * @param c MatchResult と ０始まりカウンタの BiConsumer&lt;MatchResult, Integer &gt;
	 */
	public static void resultMatch(String regex, String input, BiConsumer<MatchResult, Integer> c){
		Matcher m = Pattern.compile(regex).matcher(input);
		AtomicInteger i = new AtomicInteger(0);
		while(m.find()){
			c.accept(m.toMatchResult(), i.getAndIncrement());
		}
	}
	/**
	 * BiFunction&lt;String, Integer, String&gt; による置換
	 * @param regex  正規表現
	 * @param input 対象
	 * @param BiFunction&lt;String, Integer, String&gt; 一致した文字列とカウンタ（開始＝０）より置換文字列を返す BiFunction
	 * @return 置換後の文字列
	 */
	public static String replace(String regex, String input, BiFunction<String, Integer, String> f){
		Matcher m = Pattern.compile(regex).matcher(input);
		AtomicInteger i = new AtomicInteger(0);
		AtomicInteger x = new AtomicInteger(0);
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(new Iterator<String>(){
			@Override
			public boolean hasNext(){
				return m.find();
			}
			@Override
			public String next(){
				return input.substring(i.getAndSet(m.end()), m.start()) + f.apply(m.group(), x.getAndIncrement());
			}
		}, Spliterator.ORDERED), false).collect(Collectors.joining()) + input.substring(i.get());
	}
}
