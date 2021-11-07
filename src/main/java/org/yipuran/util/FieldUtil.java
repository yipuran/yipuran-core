package org.yipuran.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

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
	public static <R, T> R copy(T t, Supplier<R> supplier){
		UnaryOperator<Class<?>> superFind = c->c.getSuperclass();
		UnaryOperator<String> topUpper = s->s.substring(0, 1).toUpperCase() + s.substring(1);
		Class<?> c = t.getClass();
		R r = supplier.get();
		try{
			do{
				for(Field f : c.getDeclaredFields()){
					String n = f.getName();
					String name = topUpper.apply(n);
					Method getter = c.getDeclaredMethod((c.getDeclaredField(n).getType().equals(boolean.class) ? "is" : "get") + name);
					Method setter = r.getClass().getDeclaredMethod("set"+ name, getter.getReturnType());
					setter.invoke(r, getter.invoke(t));
				}
			}while(!(c=superFind.apply(c)).equals(Object.class));
		}catch(SecurityException | NoSuchFieldException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e){
			throw new RuntimeException(e);
		}
		return r;
	}
	/**
	 * コピー（寛大）.
	 * コピー先に同一名称、型が存在しないフィールドは無視される。
	 * @param t   コピー元のインスタンス
	 * @param sup コピー先を返す Supplier
	 * @return 引数Supplierに、コピー後のインスタンス
	 */
	public static <T, R> R copylenient(T t, Supplier<R> supplier){
		UnaryOperator<Class<?>> superFind = c->c.getSuperclass();
		UnaryOperator<String> topUpper = s->s.substring(0, 1).toUpperCase() + s.substring(1);
		Class<?> c = t.getClass();
		R r = supplier.get();
		try{
			do{
				for(Field f : c.getDeclaredFields()){
					String n = f.getName();
					String name = topUpper.apply(n);
					Method getter = c.getDeclaredMethod((c.getDeclaredField(n).getType().equals(boolean.class) ? "is" : "get") + name);
					try{
						Method setter = r.getClass().getDeclaredMethod("set"+ name, getter.getReturnType());
						setter.invoke(r, getter.invoke(t));
					}catch(NoSuchMethodException e){
					}
				}
			}while(!(c=superFind.apply(c)).equals(Object.class));
		}catch(SecurityException | NoSuchFieldException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e){
			throw new RuntimeException(e);
		}
		return r;
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
	public static <T> T[] valueArrays(int n, T value){
		@SuppressWarnings("unchecked")
		T[] t = (T[])Array.newInstance(value.getClass(), n);
		for(int i=0; i < n; i++){
			t[i] = value;
		}
		return t;
	}
	/**
	 * 総称型配列を指定値（可変引数）で生成
	 * @param n 長さ
	 * @param values 長さ不足分は、null がセットされる。
	 * @return T[]
	 * @since Ver 4.16
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] valueArrays(int n, T...values){
		T[] t = (T[])Array.newInstance(values[0].getClass(), n);
		int vlen = values.length;
		for(int i=0; i < n; i++)
			t[i] = i < vlen ? values[i] : null;
		return t;
	}
	/**
	 * 総称型配列を任意別リストで生成
	 * @param n 長さ
	 * @param cls 生成する配列のクラス
	 * @param list 任意別リスト
	 * @param function 任意別リストの値から結果配列格納値を求めるFunction
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T,U> T[] valueArrays(int n, Class<T> cls,  List<U> list, Function<U, T> function){
		T[] t = (T[])Array.newInstance(cls, n);
		int vlen = list.size();
		for(int i=0; i < n; i++)
			t[i] = i < vlen ? function.apply(list.get(i)) : null;
		return t;
	}
}
