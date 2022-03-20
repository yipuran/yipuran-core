package org.yipuran.util.collection;

/**
 * List の Diff 結果を表現する為のクラス.
 * <pre>
 * Diff実行をするクラス Listy が実行結果としてインスタンスを生成し
 * 結果としてCollectionにして返す
 * </pre>
 */
public class ListDifference<E> implements Comparable<ListDifference<E>>{
   private Long index;
   private E left;
   private E right;
   /**
    * コンストラクタ
    * @param index List上の index
    * @param left Diff結果indexが指す左側リストの要素
    * @param right Diff結果indexが指す右側リストの要素
    */
   public ListDifference(long index,E left,E right){
      this.index = index;
      this.left = left;
      this.right = right;
   }
   public long getIndex(){
      return this.index;
   }
   public E leftValue(){
      return this.left;
   }
   public E rightValue(){
      return this.right;
   }
   @Override
   public int compareTo(ListDifference<E> o){
      return this.index.compareTo(o.index);
   }
}