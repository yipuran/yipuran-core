package org.yipuran.function;

import java.io.Serializable;
import java.util.Objects;

/**
 * triple Predicate.
 *
 * @since Ver4.17
 */
@FunctionalInterface
public interface TriPredicate<T, U, V> extends Serializable{
	boolean test(T t, U u, V v);

	default TriPredicate<T, U, V> and(TriPredicate<? super T, ? super U, ? super V> other){
		Objects.requireNonNull(other);
			return (t, u, v)->{
			try{
				return test(t, u, v) && other.test(t, u, v);
			}catch(Exception e){
				return false;
			}
		};
	}
	default TriPredicate<T, U, V> or(TriPredicate<? super T, ? super U, ? super V> other){
		Objects.requireNonNull(other);
		return (t, u, v)->{
			try{
				return test(t, u, v) || other.test(t, u, v);
			}catch(Exception e){
				return false;
			}
		};
	}
	default TriPredicate<T, U, V> negate(){
		return (t, u, v)->{
			try{
				return !test(t, u, v);
			}catch(Exception e){
				return false;
			}
		};
	}
}
