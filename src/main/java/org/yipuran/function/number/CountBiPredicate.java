package org.yipuran.function.number;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiPredicate;

import org.yipuran.function.TetraPredicate;
import org.yipuran.function.ThrowableBiPredicate;
import org.yipuran.function.TriPredicate;

/**
 * カウンタ付き BiPredicate.
 * <PRE>
 * TriPredicate＜T, U, Long＞ でストリーム BiPredicate実行時のカウント(long）を認識して処理する。
 * BiPredicate を指定する箇所で CountBiPredicate インスタンスを指定する。
 *
 *
 * CountBiPredicate.of((t, u, c)->{
 *    // t : 要素１
 *    // u : 要素２
 * 	// c : long カウンタ
 *
 * 	return true;
 * });
 *
 * ThrowableBiPredicate を継承しており、例外捕捉もカウンタを取得した TetraPredicate 処理を指定できる。
 *
 * CountBiPredicate.of((t, u,  c)->{
 *    // t : 要素１
 *    // u : 要素２
 * 	// c : long カウンタ
 *
 * 	return true;
 * },(t, u, c, x)->{
 *    // t : 要素１
 *    // u : 要素２
 * 	// c : long カウンタ
 *    // x : 例外 Throwable
 *
 *    return false;
 * });
 *
 * このインターフェースは、Serializable である
 * </PRE>
 * @since Ver4.17
 */
public interface CountBiPredicate<T, U> extends ThrowableBiPredicate<T, U>{

	default CountBiPredicate<T, U> and(CountBiPredicate<? super T, ? super U> other){
		Objects.requireNonNull(other);
			return (t, u)->{
			try{
				return test(t, u) && other.test(t, u);
			}catch(Exception e){
				return false;
			}
		};
	}
	default CountBiPredicate<T, U>  or(CountBiPredicate<? super T, ? super U> other){
		Objects.requireNonNull(other);
		return (t, u)->{
			try{
				return test(t, u) || other.test(t, u);
			}catch(Exception e){
				return false;
			}
		};
	}

	public static <T, U> BiPredicate<T, U> of(TriPredicate<T, U, Long> tripredicate){
		AtomicLong c = new AtomicLong(1);
		return (t, u)->{
			try{
				return tripredicate.test(t, u, c.getAndIncrement());
			}catch(Exception ex){
				throw new RuntimeException(ex);
			}
		};
	}

	public static <T, U> BiPredicate<T, U> of(TriPredicate<T, U, Long> tripredicate, TetraPredicate<? super T, ? super U, Long, Throwable> tetrapredivate){
		AtomicLong c = new AtomicLong(1);
		return (t, u)->{
			try{
				return tripredicate.test(t, u, c.getAndIncrement());
			}catch(Throwable ex){
				return tetrapredivate.test(t, u, c.get(), ex);
			}
		};
	}

}
