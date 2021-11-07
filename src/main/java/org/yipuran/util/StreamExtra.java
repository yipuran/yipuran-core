package org.yipuran.util;

import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * StreamExtra.
 * <PRE>
 * Java8 で不充分な Stream のサポート、
 * Java9 の takeWhile や dropWhile と同等の機能など。
 * Java9 以降では不要な機能
 *
 * Stream をラップする方法で使用する。
 * dropstream、whilestream の実行を１回だけでなく複数回実行できるように（Stream再利用できない制約）
 * 内部で、List に変換して抱えたものから、Stream生成実行する。
 * </PRE>
 * @since Ver 4.14
 */
public class StreamExtra<T>{

	private List<T> list;

	private StreamExtra(Stream<T> stream){
		list = stream.collect(Collectors.toList());
	}
	/**
	 * インスタンス生成
	 * @param stream
	 * @return
	 */
	public static <T> StreamExtra<T> of(Stream<T> stream){
		return new StreamExtra<>(stream);
	}
	/**
	 * Java9 dropWhile の代用
	 * @param predicate
	 * @return
	 */
	public Stream<T> dropstream(Predicate<? super T> predicate) {
		Spliterator<T> itr = list.stream().spliterator();
		return StreamSupport.stream(
			new Spliterators.AbstractSpliterator<T>(itr.estimateSize(), 0){
				@Override
				public boolean tryAdvance(Consumer<? super T> consumer) {
					return itr.tryAdvance(e->{
						if (!predicate.test(e)) consumer.accept(e);
					});
				}
			}, false
		);
	}
	/**
	 * Java9 takeWhile の代用
	 * @param predicate
	 * @return
	 */
	public Stream<T> whilestream(Predicate<? super T> predicate){
		Spliterator<T> itr = list.stream().spliterator();
		return StreamSupport.stream(
			new Spliterators.AbstractSpliterator<T>(itr.estimateSize(), 0) {
				boolean still = true;
				@Override
				public boolean tryAdvance(Consumer<? super T> consumer) {
					if (still){
						boolean hasNext = itr.tryAdvance(e->{
							if (predicate.test(e)){
								consumer.accept(e);
							}else{
								still = false;
							}
						});
						return hasNext && still;
					}
					return false;
				}
			}, false
		);
	}
	/**
	 * Java9 dropWhile の代用（static 実行）注：Streamはcloseされる
	 * @param predicate
	 * @return
	 */
	public static <T> Stream<T> dropstream(Stream<T> stream, Predicate<? super T> predicate) {
		Spliterator<T> itr = stream.spliterator();
		return StreamSupport.stream(
			new Spliterators.AbstractSpliterator<T>(itr.estimateSize(), 0){
				@Override
				public boolean tryAdvance(Consumer<? super T> consumer) {
					return itr.tryAdvance(e->{
						if (!predicate.test(e)) consumer.accept(e);
					});
				}
			}, false
		);
	}
	/**
	 * Java9 takeWhile の代用（static 実行）注：Streamはcloseされる
	 * @param predicate
	 * @return
	 */
	public static <T> Stream<T> whilestream(Stream<T> stream, Predicate<? super T> predicate){
		Spliterator<T> itr = stream.spliterator();
		return StreamSupport.stream(
			new Spliterators.AbstractSpliterator<T>(itr.estimateSize(), 0) {
				boolean still = true;
				@Override
				public boolean tryAdvance(Consumer<? super T> consumer) {
					if (still){
						boolean hasNext = itr.tryAdvance(e->{
							if (predicate.test(e)){
								consumer.accept(e);
							}else{
								still = false;
							}
						});
						return hasNext && still;
					}
					return false;
				}
			}, false
		);
	}

}
