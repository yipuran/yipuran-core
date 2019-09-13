package org.yipuran.util;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.function.Function;

/**
 * Fieldgetter. リフレクションでフィールド値の取得
 * <PRE>
 *  static メソッド of でフィールド名を渡してフィールド値の取得の Function の apply() を実行することで
 * フィールド値を取得する。
 * 例）
 *    public class Foo{
 *       private name;
 *       public Foo(String s){ name = s; }
 *    }
 *
 *    Foo foo;
 *       :
 *    String name = Optional.ofNullable(foo).map(e->(String)Fieldgetter.of(t->"name").apply(e)).orElse("");
 *
 *
 * </PRE>
 * @since 4.7
 */
@FunctionalInterface
public interface Fieldgetter<T, R> extends Serializable{
	/**
	 * フィールド値を取得.
	 * @param t Functionから受け取るインスタンスs
	 * @return フィールド値
	 * @throws Exception
	 */
	String get(T t) throws Exception;

	/**
	 * フィールド値を取得の為にフィールド名を返す Function を指定する
	 * @param f フィールド名を返す Function
	 * @return Function
	 */
	@SuppressWarnings("unchecked")
	static <T, R> Function<T, R> of(Fieldgetter<T, R> f){
		return t->{
			try{
				Field field = t.getClass().getDeclaredField(f.get(t));
				field.setAccessible(true);
				return (R)field.get(t);
			}catch(Throwable ex){
				throw new RuntimeException(ex);
			}
		};
	}
}
