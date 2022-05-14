package org.yipuran.util;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.function.Consumer;
/**
 * FieldArrayCopy. 指定属性（配列）フィールドのコピー
 * <PRE>
 * static メソッド of で取得する Consumer が、ofメソッドの第２引数で指定するコピー先に、
 * リフレクションでフィールドに値をセットする。
 * コピー元と先の型が同じ前提である。
 * （使用例）
 *     public class Foo{
 *       int value;
 *
 *     }
 *     FieldCopy.of(t->new String[]{ "value", "name" }, toFoo).accept(fromFoo1);
 * </PRE>
 *
 * @since 4.33
 */
@FunctionalInterface
public interface FieldArrayCopy<T> extends Serializable{
	String[] get(T t) throws Exception;

	/**
	 * フィールドコピー実行Consumerの生成
	 * @param function コピー対象フィールド名の配列を返す関数型インターフェース FieldArrayCopy
	 * @param t コピー先Object
	 * @return コピー元を指定する Consumer
	 */
	public static <T> Consumer<T> of(FieldArrayCopy<T> function, T t){
		return u->{
			try{
				for(String fname:function.get(t)){
					Field f;
					try{
						f = t.getClass().getField(fname);
					}catch(NoSuchFieldException e){
						f = t.getClass().getDeclaredField(fname);
					}
					f.setAccessible(true);
					f.set(u, f.get(t));
				}
			}catch(Throwable ex){
				throw new RuntimeException(ex);
			}
		};
	}
}
