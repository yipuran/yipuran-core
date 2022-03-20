package org.yipuran.function;

import java.io.Serializable;

/**
 * 4 elements Function.
 *
 * @since Ver4.17
 */
@FunctionalInterface
public interface TetraFunction<T, U, V, W, R> extends Serializable{
	R apply(T t, U u, V v, W w);
}
