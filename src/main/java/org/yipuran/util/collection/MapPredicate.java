package org.yipuran.util.collection;

/**
 * 制限付きの HashMap 生成に使用する Predicate.
 * <pre>
 *
 * 実装インスタンスの apply メソッドの戻り値 boolean値によって、
 * HashMap への Key & Value の格納をするかどうかの判断がなされます。
 *
 * </pre>
 */
public interface MapPredicate<K,V>{
   /**
    * 対象コレクションへの追加の許可
    * @param K 追加する要素のKey
    * @param V 追加する要素のValue
    * @return true=追加を許可する。即ち、put(K k,V v)が実行される。
    */
   public boolean apply(K k,V v);

}
