package org.yipuran.util;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.function.Supplier;

import org.yipuran.function.ThrowableBiConsumer;
import org.yipuran.function.ThrowableSupplier;

/**
 * Field ユーティリティ.
 */
public final class FieldUtil{
	private FieldUtil(){
	}

	/**
	 * Fieldコピー（厳格）.
	 * コピー先に同一名称、型が存在しないと、NoSuchFieldException 等の例外を発生する。
	 * @param t   コピー元のインスタンス
	 * @param sup コピー先を返す Supplier
	 * @return 引数Supplierに、コピー後のインスタンス
	 */
	public static <R, T> R copy(T t, Supplier<R> s){
		GenericBuilder<R> builder = Arrays.stream(t.getClass().getDeclaredFields())
		.collect(ThrowableSupplier.to(()->GenericBuilder.of(s)), ThrowableBiConsumer.of((r, f)->{
			f.setAccessible(true);
			r.with(Fieldsetter.of((p, u)->f.getName()), f.get(t));
		}), (r, u)->{});
		return builder.build();
	}
	/**
	 * コピー（寛大）.
	 * コピー先に同一名称、型が存在しないフィールドは無視される。
	 * @param t   コピー元のインスタンス
	 * @param sup コピー先を返す Supplier
	 * @return 引数Supplierに、コピー後のインスタンス
	 */
	public static <R, T> R copylenient(T t, Supplier<R> s){
		GenericBuilder<R> builder = Arrays.stream(t.getClass().getDeclaredFields())
		.collect(ThrowableSupplier.to(()->GenericBuilder.of(s)), ThrowableBiConsumer.of((g, f)->{
			f.setAccessible(true);
			g.with((v, u)->{
				try{
					Field d = v.getClass().getField(f.getName());
					d.setAccessible(true);
					d.set(v, u);
				}catch(Throwable x){
				}
			}, f.get(t));
		}, (a, b)->{})
		,(v, u)->{});
		return builder.build();
	}
}
