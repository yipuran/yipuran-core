package org.yipuran.util.collection;

/**
 * 制限付きの ArrayList 生成に使用する Predicate.
 * <pre>
 *
 * 実装インスタンスの apply メソッドの戻り値 boolean値によって、
 * ArrayList へ要素を格納をするかどうかの判断がなされます。
 *
 * </pre>
 */
public interface ListPredicate<E>{

   /**
    * 対象コレクションへの追加の許可
    * @param e 追加する要素
    * @return true=追加を許可する
    */
   public boolean apply(E e);
}
