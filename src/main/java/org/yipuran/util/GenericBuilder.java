package org.yipuran.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * GenericBuilder.
 * @param <T>
 */
public final class GenericBuilder<T>{
	private final Supplier<T> instantiator;

	private List<Consumer<T>> instanceModifiers = new ArrayList<>();

	/**
	 * コンストラクタ.
	 * @param instantiator Supplier<T>
	 */
	private GenericBuilder(Supplier<T> instantiator){
		this.instantiator = instantiator;
	}

	/**
	 * Builder of.
	 * @param instantiator インスタンス生成するSupplier<T>
	 * @param <T> T
	 * @return GenericBuilder
	 */
	public static <T> GenericBuilder<T> of(Supplier<T> instantiator){
		return new GenericBuilder<T>(instantiator);
	}

	/**
	 * setter with.
	 * @param consumer setterメソッド BiConsumer<T, U>
	 * @param value set value
	 * @param <U> U
	 * @return GenericBuilder
	 */
	public <U> GenericBuilder<T> with(BiConsumer<T, U> consumer, U value){
		Consumer<T> c = instance -> consumer.accept(instance, value);
		instanceModifiers.add(c);
		return this;
	}

	/**
	 * build.
	 * @return T
	 */
	public T build(){
		T value = instantiator.get();
		instanceModifiers.forEach(modifier -> modifier.accept(value));
		instanceModifiers.clear();
		return value;
	}
}
