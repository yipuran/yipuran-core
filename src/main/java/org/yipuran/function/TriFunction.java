package org.yipuran.function;

import java.io.Serializable;

/**
 * triple Function.
 *
 * @since Ver4.17
 */
@FunctionalInterface
public interface TriFunction<T, U, V, R> extends Serializable{
	R apply(T t, U u, V v);
}
