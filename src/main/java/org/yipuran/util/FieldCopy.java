package org.yipuran.util;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.function.Consumer;
/**
 * FieldCopy. 指定属性フィールドのコピー
 * <PRE>
 * static メソッド of で取得する Consumer が、ofメソッドの第２引数で指定するコピー先に、
 * リフレクションでフィールドに値をセットする。
 * コピー元と先の型が同じ前提である。
 * （使用例）
 *     public class Foo{
 *       int value;
 *     }
 *     FieldCopy.of(t->"value", toFoo).accept(fromFoo1);
 * </PRE>
 *
 * @since 4.33
 */
@FunctionalInterface
public interface FieldCopy<T> extends Serializable{
	String get(T t) throws Exception;

	/**
	 * フィールドコピー実行Consumerの生成
	 * @param function コピー対象フィールド名を返す関数型インターフェース FieldCopy
	 * @param t コピー先Object
	 * @return コピー元を指定する Consumer
	 */
	public static <T> Consumer<T> of(FieldCopy<T> function, T t){
		return u->{
			try{
				String fname = function.get(t);
				Field f = null;
				Class<?> cls = t.getClass();
				while(cls != null){
					try{
						f = cls.getDeclaredField(fname);
						break;
					}catch(NoSuchFieldException e){
						cls = cls.getSuperclass();
					}
				}
				f.setAccessible(true);
				f.set(u, f.get(t));
			}catch(Throwable ex){
				throw new RuntimeException(ex);
			}
		};
	}
}
