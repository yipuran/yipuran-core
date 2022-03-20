package org.yipuran.function;

import java.io.Serializable;
import java.util.function.Consumer;
/**
 * Consumer実行結果を取得する為の apply.
 * <pre>
 * （使用例）
 *     import lombok.Data;
 *     ＠Data
 *     public Foo {
 *        private String name;
 *        private int value;
 *     }
 *
 *     Foo foo = ReturnalConsumer.of(Foo.class)
 *               .with(e->e.setName("ABC"))
 *               .with(e->e.setValue(12))
 *               .get(new Foo());
 * </pre>
 * @since 4.31
 */
@FunctionalInterface
public interface ReturnalConsumer<T> extends Serializable{
	T apply(T t);

	public static <T> ReturnalConsumer<T> of(Class<T> t){
		return new ReturnalConsumer<T>(){
			@Override
			public T apply(T u){
				return u;
			}
		};
	}

	default ReturnalConsumer<T> with(Consumer<T> c){
		return  t ->{
			c.accept(apply(t));
			return t;
		};
	}
	default T get(T t) {
		return apply(t);
	}
}
