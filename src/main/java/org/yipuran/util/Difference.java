package org.yipuran.util;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Diff判定処理.
 */
public final class Difference{
	/**
	 * ｄｉｆｆ実行.
	 * @param t 比較元
	 * @param u 比較先
	 * @param delete 削除判定時の処理 Consumer&lt;T&gt;
	 * @param add 追加判定時の処理 Consumer&lt;U&gt;
	 * @param modified 変更判定時の処理 BiConsumer&lt;T, U&gt;
	 * @return true=差分有り、false=差分無し。
	 * @param &lt;T&gt; t
	 * @param &lt;U&gt; u
	 */
	public static <T, U> boolean parse(T t, U u, Consumer<T> delete, Consumer<U> add, BiConsumer<T, U> modified){
		if (t==null || "".equals(t)){
			if (u != null && !"".equals(u)){
				// Add
				add.accept(u);
				return true;
			}
		}else{
			if (u==null || "".equals(u)){
				// Delete
				delete.accept(t);
				return true;
			}else{
				if (!t.equals(u)){
					// Modified
					modified.accept(t, u);
					return true;
				}
			}
		}
		return false;
	}

}
