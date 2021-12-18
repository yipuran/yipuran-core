package org.yipuran.function;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Predicate;

/**
 * Exception 捕捉 Predicate.
 *
 * <PRE>
 * Exception発生する Predicate の処理でExceptionを捕捉処理する関数を同時に定義する。
 * of メソッドで生成する。
 *
 * 例)
 * stream.filter(ThrowablePredicate.of(e->{
 *     // 通常の判定
 *     return true;
 * }, (e, x)->false))).forEach(e->{
 *    // todo
 * };
 *
 * </PRE>
 */
@FunctionalInterface
public interface ThrowablePredicate<T> extends Serializable{

	boolean test(T t) throws Exception;

	default Predicate<T> and(Predicate<? super T> other){
		Objects.requireNonNull(other);
			return (t)->{
			try{
				return test(t) && other.test(t);
			}catch(Exception e){
				throw new RuntimeException(e);
			}
		};
	}
	default Predicate<T> and(Predicate<? super T> other, BiFunction<T, Exception, Boolean> onCatch){
		Objects.requireNonNull(other);
			return (t)->{
			try{
				return test(t) && other.test(t);
			}catch(Exception e){
				return onCatch.apply(t, e);
			}
		};
	}

	default Predicate<T> negate(){
		return (t)->{
			try{
				return !test(t);
			}catch(Exception e){
				throw new RuntimeException(e);
			}
		};
	}
	default Predicate<T> negate(BiFunction<T, Exception, Boolean> onCatch){
		return (t)->{
			try{
				return !test(t);
			}catch(Exception e){
				return !onCatch.apply(t, e);
			}
		};
	}

	default Predicate<T> or(Predicate<? super T> other){
		Objects.requireNonNull(other);
		return (t)->{
			try{
				return test(t) || other.test(t);
			}catch(Exception e){
				throw new RuntimeException(e);
			}
		};
	}
	default Predicate<T> or(Predicate<? super T> other, BiFunction<T, Exception, Boolean> onCatch){
		Objects.requireNonNull(other);
		return (t)->{
			try{
				return test(t) || other.test(t);
			}catch(Exception e){
				return onCatch.apply(t, e);
			}
		};
	}

	/**
	 * ThrowablePredicate 生成.
	 * @param p 例外スローする Predicate&lt;T&gt;処理
	 * @param onCatch Exception捕捉処理 , boolean値を返さなければならない。
	 * @return Predicate&lt;T&gt;
	 */
	public static <T> Predicate<T> of(ThrowablePredicate<T> p, BiFunction<T, Exception, Boolean> onCatch){
		return t->{
			try{
				return p.test(t);
			}catch(Exception e){
				return onCatch.apply(t, e);
			}
		};
	}
	/**
	 * ThrowablePredicate 生成（外に例外スロー）.
	 * @param p 例外スローする Predicate&lt;T&gt;処理
	 * @return Predicate&lt;T&gt;
	 */
	public static <T> Predicate<T> of(ThrowablePredicate<T> p){
		return t->{
			try{
				return p.test(t);
			}catch(Throwable ex){
				throw new RuntimeException(ex);
			}
		};
	}
}
