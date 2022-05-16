package org.yipuran.util;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Consumer;
/**
 * FieldListCopy. 指定属性（リスト）フィールドのコピー
 * <PRE>
 * static メソッド of で取得する Consumer が、ofメソッドの第２引数で指定するコピー先に、
 * リフレクションでフィールドに値をセットする。
 * コピー元と先の型が同じ前提である。
 * （使用例）
 *     public class Foo{
 *       int value;
 *
 *     }
 *     FieldCopy.of(t->List.of( "value", "name" ), toFoo).accept(fromFoo1);
 * </PRE>
 *
 * @since 4.33
 */
@FunctionalInterface
public interface FieldListCopy<T>{
	List<String> get(T t) throws Exception;

	/**
	 * フィールドコピー実行Consumerの生成
	 * @param function コピー対象フィールド名のリストを返す関数型インターフェース FieldListCopy
	 * @param t コピー先Object
	 * @return コピー元を指定する Consumer
	 */
	public static <T> Consumer<T> of(FieldListCopy<T> function, T t){
		return u->{
			try{
				for(String fname:function.get(t)){
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
				}
			}catch(Throwable ex){
				throw new RuntimeException(ex);
			}
		};
	}
}
