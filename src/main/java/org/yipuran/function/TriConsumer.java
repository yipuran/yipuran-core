package org.yipuran.function;

import java.io.Serializable;
import java.util.Objects;

/**
 * triple Consumer.
 *
 * @since Ver4.17
 */
@FunctionalInterface
public interface TriConsumer<T, U, V> extends Serializable{
	void accept(T t, U u, V v);

	default TriConsumer<T, U, V> andThen(TriConsumer<? super T, ? super U, ? super V> after){
		Objects.requireNonNull(after);
		return (t, u, v)->{
			accept(t, u, v);
			after.accept(t, u, v);
		};
	}
}
