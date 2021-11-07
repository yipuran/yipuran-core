package org.yipuran.function;

import java.io.Serializable;
import java.util.Objects;

/**
 * 4 elements Predicate.
 *
 * @since Ver4.17
 */
public interface TetraPredicate<T, U, V, W> extends Serializable{
	boolean test(T t, U u, V v, W w);

	default TetraPredicate<T, U, V, W> and(TetraPredicate<? super T, ? super U, ? super V, ? super W> other){
		Objects.requireNonNull(other);
			return (t, u, v, w)->{
			try{
				return test(t, u, v, w) && other.test(t, u, v, w);
			}catch(Exception e){
				return false;
			}
		};
	}
	default TetraPredicate<T, U, V, W> or(TetraPredicate<? super T, ? super U, ? super V, ? super W> other){
		Objects.requireNonNull(other);
		return (t, u, v, w)->{
			try{
				return test(t, u, v, w) || other.test(t, u, v, w);
			}catch(Exception e){
				return false;
			}
		};
	}
	default TetraPredicate<T, U, V, W> negate(){
		return (t, u, v, w)->{
			try{
				return !test(t, u, v, w);
			}catch(Exception e){
				return false;
			}
		};
	}
}
