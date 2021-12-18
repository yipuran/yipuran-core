package org.yipuran.function;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

/**
 * Exception 捕捉 BiPredicate.
 *
 * <PRE>
 * Exception発生する BiPredicate の処理でExceptionを捕捉処理する関数を同時に定義する。
 * of メソッドで生成する。
 *
 * 例)
 * stream.filter(ThrowableBiPredicate.of((t, u)->{
 *     // 通常の処理
 * }
 * , (p, x)->{
 *     // 例外捕捉処理、 x は Exception
 * }).forEach(e->{});
 *     p は、t と u の AbstractMap.SimpleEntry&lt;T, U&gt;
 * </PRE>
 *
 */
@FunctionalInterface
public interface ThrowableBiPredicate<T, U> extends Serializable{
	boolean test(T t, U u) throws Exception;

	default BiPredicate<T, U> and(BiPredicate<? super T, ? super U> other){
		Objects.requireNonNull(other);
			return (t, u)->{
			try{
				return test(t, u) && other.test(t, u);
			}catch(Exception e){
				throw new RuntimeException(e);
			}
		};
	}
	default BiPredicate<T, U> and(BiPredicate<? super T, ? super U> other, BiFunction<T, Exception, Boolean> onCatch){
		Objects.requireNonNull(other);
			return (t, u)->{
			try{
				return test(t, u) && other.test(t, u);
			}catch(Exception e){
				return onCatch.apply(t, e);
			}
		};
	}

	default BiPredicate<T, U> negate(){
		return (t, u)->{
			try{
				return !test(t, u);
			}catch(Exception e){
				throw new RuntimeException(e);
			}
		};
	}
	default BiPredicate<T, U> negate(BiFunction<T, Exception, Boolean> onCatch){
		return (t, u)->{
			try{
				return !test(t, u);
			}catch(Exception e){
				return !onCatch.apply(t, e);
			}
		};
	}

	default BiPredicate<T, U> or(BiPredicate<? super T, ? super U> other){
		Objects.requireNonNull(other);
		return (t, u)->{
			try{
				return test(t, u) || other.test(t, u);
			}catch(Exception e){
				throw new RuntimeException(e);
			}
		};
	}
	default BiPredicate<T, U> or(BiPredicate<? super T, ? super U> other, BiFunction<T, Exception, Boolean> onCatch){
		Objects.requireNonNull(other);
		return (t, u)->{
			try{
				return test(t, u) || other.test(t, u);
			}catch(Exception e){
				return onCatch.apply(t, e);
			}
		};
	}

	/**
	 * ThrowableBiPredicate 生成.
	 * @param p 例外スローする ThrowableBiPredicate&lt;T, U&gt;処理
	 * @param onCatch Exception捕捉処理 , boolean値を返さなければならない。
	 * @return Predicate&lt;T&gt;
	 */
	public static <T, U> BiPredicate<T, U> of(ThrowableBiPredicate<T, U> p, BiFunction<AbstractMap.SimpleEntry<T, U>, Exception, Boolean> onCatch){
		return (t, u)->{
			try{
				return p.test(t, u);
			}catch(Exception e){
				return onCatch.apply(new AbstractMap.SimpleEntry<>(t, u), e);
			}
		};
	}
	/**
	 * ThrowableBiPredicate 生成（外に例外スロー）.
	 * @param p 例外スローする Predicate&lt;T&gt;処理
	 * @return Predicate&lt;T&gt;
	 */
	public static <T, U> BiPredicate<T, U> of(ThrowableBiPredicate<T, U> p){
		return (t, u)->{
			try{
				return p.test(t, u);
			}catch(Throwable ex){
				throw new RuntimeException(ex);
			}
		};
	}
}
