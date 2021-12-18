package org.yipuran.function;

import java.io.Serializable;
import java.util.Objects;

/**
 * 4 elements Consumer.
 *
 * @since Ver4.17
 */
@FunctionalInterface
public interface TetraConsumer<T, U, V, W> extends Serializable{
	void accept(T t, U u, V v, W w);

	default TetraConsumer<T, U, V, W> andThen(TetraConsumer<? super T, ? super U, ? super V, ? super W> after){
		Objects.requireNonNull(after);
		return (t, u, v, w)->{
			accept(t, u, v, w);
			after.accept(t, u, v, w);
		};
	}
}
