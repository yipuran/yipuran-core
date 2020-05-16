package org.yipuran.function.number;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import org.yipuran.function.ThrowablePredicate;
import org.yipuran.function.TriPredicate;

/**
 * カウンタ付き Predicate.
 * <PRE>
 * BiPredicate＜T, Long＞ でストリーム Predicate実行時のカウント(long）を認識して処理する。
 * Predicate を指定する箇所で CountPredicate インスタンスを指定する。
 *
 *
 * CountPredicate.of((t, c)->{
 *    // t : 要素
 * 	// c : long カウンタ
 *
 * 	return true;
 * });
 *
 * ThrowablePredicate を継承しており、例外捕捉もカウンタを取得した TriPredicate 処理を指定できる。
 *
 * CountPredicate.of((t, c)->{
 *    // t : 要素
 *    // c : long カウンタ
 *
 * 	return true;
 * },(t , c, x)->{
 *    // t : 要素
 *    // c : long カウンタ
 *    // x : 例外 Throwable
 *
 *    return false;
 * });
 *
 * このインターフェースは、Serializable である
 * </PRE>
 * @since Ver4.17
 */
public interface CountPredicate<T> extends ThrowablePredicate<T>{

	default CountPredicate<T> and(CountPredicate<? super T> other){
		Objects.requireNonNull(other);
			return t->{
			try{
				return test(t) && other.test(t);
			}catch(Exception e){
				return false;
			}
		};
	}
	default CountPredicate<T> or(CountPredicate<? super T> other){
		Objects.requireNonNull(other);
		return t->{
			try{
				return test(t) || other.test(t);
			}catch(Exception e){
				return false;
			}
		};
	}

	public static <T> Predicate<T> of(BiPredicate<T, Long> bipredicate){
		AtomicLong c = new AtomicLong(1);
		return t->{
			try{
				return bipredicate.test(t, c.getAndIncrement());
			}catch(Exception ex){
				throw new RuntimeException(ex);
			}
		};
	}

	public static <T> Predicate<T> of(BiPredicate<T, Long> bipredicate, TriPredicate<T, Long, Throwable> onCatch){
		AtomicLong c = new AtomicLong(1);
		return t->{
			try{
				return bipredicate.test(t, c.getAndIncrement());
			}catch(Throwable ex){
				return onCatch.test(t, c.get(), ex);
			}
		};
	}
}
