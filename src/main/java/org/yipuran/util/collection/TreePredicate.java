package org.yipuran.util.collection;

/**
 * 制限付きの TreeSet,TreeMap 生成に使用する Predicate.
 * <pre>
 *
 * 実装インスタンスの apply メソッドの戻り値 boolean値によって、
 * TreeSet,TreeMap へ要素を格納をするかどうかの判断がなされます。
 * TreeMap の場合、apply メソッドの引数は、Map 格納時、put メソッドの Key に相当します。
 * Comparator を指定する場合、ComparePredicate を使ってください。
 *
 * </pre>
 */
public interface TreePredicate<E>{
   /**
    * 対象コレクションへの追加の許可
    * @param e 追加する要素、または、TreeMap の場合は、Key
    * @return true=追加を許可する
    */
   public boolean apply(E e);
}