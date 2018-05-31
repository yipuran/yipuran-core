package org.yipuran.util.collection;

import java.util.Comparator;
/**
 * 制限付きの Comparator 指定のTreeSet,TreeMap 生成に使用する Predicate.
 * <pre>
 *
 * 実装インスタンスの apply メソッドの戻り値 boolean値によって、
 * Comparator を指定したコンストラクタで生成する TreeSet,TreeMap へ
 * 要素を格納をするかどうかの判断がなされます。
 *
 * TreePredicate と異なり、コレクションへの追加の許可メソッド＝apply だけでなく
 * java.util.Comparator で要求される compare メソッドを実装しなければなりません。
 *
 * TreeMap の場合、apply メソッドの引数は、Map 格納時、put メソッドの Key に相当します。
 *
 * </pre>
 */
public interface ComparePredicate<E> extends Comparator{
   /**
    * 対象コレクションへの追加の許可
    * @param e 追加する要素、または、TreeMap の場合は、Key
    * @return true=追加を許可する
    */
   public boolean apply(E e);
}
