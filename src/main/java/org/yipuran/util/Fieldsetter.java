package org.yipuran.util;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.function.BiConsumer;
/**
 * Fieldsetter. リフレクションでフィールドに値をセット
 * <PRE>
 * static メソッド of で取得する BiConsumer がリフレクションでフィールドに値をセットする。
 * セットするフィールド名称を返す get メソッドが関数型インターフェースとして約束されている。
 * （使用例）
 *     public class Foo{
 *       int value;
 *     }
 *     // GenericBuilder#with で使用
 *     Foo foo = GenericBuilder.of(Foo::new)
 *               .with(Fieldsetter.of((t, u)->"value"), 12)
 *               .build();
 * </PRE>
 */
@FunctionalInterface
public interface Fieldsetter<T, U> extends Serializable{
	/**
	 * セットするフィールド名称を返す.
	 * @param t BiConsumerから渡されるセットする Class
	 * @param u BiConsumerから渡されるセットする値
	 * @return フィールド名称
	 * @throws Exception
	 */
	String get(T t, U u) throws Exception;

	/**
	 * フィールドに値セット実行するBiConsumerを返す.
	 * @param function T=セットする Class, U=セットする値 のこの関数型インターフェース
	 * @return BiConsumer
	 */
	public static <T, U> BiConsumer<T, U> of(Fieldsetter<T, U> function){
		return (t, u)->{
			try{
				String fname = function.get(t, u);
				Field f;
				try{
					f = t.getClass().getField(fname);
				}catch(NoSuchFieldException e){
					f = t.getClass().getDeclaredField(fname);
				}
				f.setAccessible(true);
				f.set(t, u);
			}catch(Throwable ex){
				throw new RuntimeException(ex);
			}
		};
	}
}
