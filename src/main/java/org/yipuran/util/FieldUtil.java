package org.yipuran.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
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
	/**
	 * Fieldコピー（除外フィールド指定）.
	 * コピー先に同一名称、型が存在しないと、NoSuchFieldException 等の例外を発生する。
	 * @param t   コピー元のインスタンス
	 * @param sup コピー先を返す Supplier
	 * @param exclude 除外フィールドの名称 Predicate 、trueを返すことで、negate()が実行されてコピー対象から除外される。
	 * @return 引数Supplierに、コピー後のインスタンス
	 * @since v4.10
	 */
	public static <R, T> R copy(T t, Supplier<R> s, Predicate<String> exclude) {
		GenericBuilder<R> builder = Arrays.stream(t.getClass().getDeclaredFields()).filter(e->exclude.negate().test(e.getName()))
		.collect(ThrowableSupplier.to(()->GenericBuilder.of(s)), ThrowableBiConsumer.of((r, f)->{
			f.setAccessible(true);
			r.with(Fieldsetter.of((p, u)->f.getName()), f.get(t));
		}), (r, u)->{});
		return builder.build();
	}
	/**
	 * コピー（寛大で除外フィールド指定）.
	 * コピー先に同一名称、型が存在しないフィールドは無視される。
	 * @param t   コピー元のインスタンス
	 * @param sup コピー先を返す Supplier
	 * @param exclude 除外フィールドの名称 Predicate 、trueを返すことで、negate()が実行されてコピー対象から除外される。
	 * @return 引数Supplierに、コピー後のインスタンス
	 * @since v4.10
	 */
	public static <R, T> R copylenient(T t, Supplier<R> s, Predicate<String> exclude){
		GenericBuilder<R> builder = Arrays.stream(t.getClass().getDeclaredFields()).filter(e->exclude.negate().test(e.getName()))
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
	/**
	 * 総称型配列を指定値で生成
	 * @param n 長さ
	 * @param value nullでないTの値
	 * @return T[]
	 * @since Ver 4.14
	 */
	public static <T> T[] valueArrays(int n, T value) {
		@SuppressWarnings("unchecked")
		T[] t = (T[])Array.newInstance(value.getClass(), n);
		List<T> l = new ArrayList<>(Collections.emptyList());
		for(int i=0; i < n; i++){
			l.add(value);
		}
		return l.toArray(t);
	}
}
