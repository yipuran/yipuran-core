package org.yipuran.util.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
/**
 * 挿入時制限付きコレクション生成.
 * <pre>
 * コレクションへの追加の制限インターフェースを実装したインスタンスを指定して挿入時制限付きコレクションを
 * 生成する。
 *     public interface ListPredicate&lt;E&gt;     →  ArrayList 生成に使用する Predicate
 *     public interface MapPredicate&lt;K, V&gt;    →  HashMap 生成に使用する Predicate
 *     public interface TreePredicate&lt;E&gt;     →  TreeSet,TreeMap 生成に使用する Predicate
 *     public interface ComparePredicate&lt;E&gt;  →  Comparator 指定のTreeSet,TreeMap 生成に使用する Predicate
 * これら Predicate を引数に持つコレクション生成メソッドを提供する。
 *     public static &lt;E&gt;   List&lt;E&gt;  createArrayList(ListPredicate&lt;E&gt; predicate)        → 制限付き ArrayList 生成
 *     public static &lt;K, V&gt; Map&lt;K, V&gt; createHashMap(MapPredicate&lt;K, V&gt; predicate)         → 制限付き HashMap 生成
 *     public static &lt;E&gt;   Set&lt;E&gt;   createTreeSet(final TreePredicate&lt;E&gt; predicate)    → 制限付き TreeSet 生成
 *     public static &lt;K, V&gt; Map&lt;K, V&gt; createTreeMap(final TreePredicate<K> predicate)    → 制限付き TreeMap 生成
 *     public static &lt;E&gt;   Set&lt;E&gt;   createTreeSet(final ComparePredicate&lt;E&gt; predicate) → 制限付き Comparator 指定の TreeSet 生成
 *     public static &lt;K, V&gt; Map&lt;K, V&gt; createTreeMap(final ComparePredicate<K> predicate) → 制限付き Comparator 指定の TreeMap 生成
 * </pre>
 */
public final class LimitCollections{
   private LimitCollections(){}

   /**
    * 挿入時制限付きArrayList作成.
    * @param predicate ListPredicate実装インスタンスを指定
    * @return List&lt;E&gt;
    */
   public static <E> List<E> createArrayList(final ListPredicate<E> predicate){
      return new ArrayList<E>(){
         private static final long serialVersionUID = 1L;
         @Override
         public void add(int index,E e){
            if (predicate.apply(e)){
               super.add(index,e);
            }
         }
         @Override
         public boolean add(E e){
            return predicate.apply(e) ? super.add(e) : false;
         }
         @Override
         public boolean addAll(Collection<? extends E> c){
            boolean rtn = false;
            for(E e : c){
               rtn |= this.add(e);
            }
            return rtn;
         }
         @Override
         public boolean addAll(int index,Collection<? extends E> c){
            if (c.size() < 1) return false;
            int ix = index;
            for(E e : c){
               if (predicate.apply(e)){
                  this.add(ix,e);
                  ix++;
               }
            }
            return true;
         }
      };
   }
   /**
    * 挿入時制限付きHashMap作成.
    * @param predicate MapPredicate実装インスタンスを指定
    * @return Map&lt;K, V&gt;
    */
   public static <K,V> Map<K,V> createHashMap(final MapPredicate<K,V> predicate){
      return new HashMap<K,V>(){
         private static final long serialVersionUID = 1L;
         @Override
         public V put(K key,V value){
            if (predicate.apply(key,value)){
               return super.put(key,value);
            }
            return null;
         }
         @Override
         public void putAll(Map<? extends K,? extends V> m){
            for(K k : m.keySet()){
               V v = m.get(k);
               if (predicate.apply(k,v)){
                  super.put(k,v);
               }
            }
         }
      };
   }
   /**
    * 挿入時制限付きTreeSet作成.
    * <br/>  Comparator を指定しません。要素 E の java.lang.Comparable 実装に従います。
    * @param predicate TreePredicate実装インスタンスを指定
    * @return Set&lt;E&gt;
    */
   public static <E> Set<E> createTreeSet(final TreePredicate<E> predicate){
      return new TreeSet<E>(){
         private static final long serialVersionUID = 1L;
         @Override
         public boolean add(E e){
            return predicate.apply(e) ? super.add(e) : false;
         }
         @Override
         public boolean addAll(Collection<? extends E> c){
            boolean rtn = false;
            for(E e : c){
               rtn |= this.add(e);
            }
            return rtn;
         }
      };
   }
   /**
    * 挿入時制限付き Comparator が指定されたTreeSet作成.
    * @param predicate ComparePredicate実装インスタンスを指定
    * @return Set&lt;E&gt;
    */
   public static <E> Set<E> createTreeSet(final ComparePredicate<E> predicate){
     return new TreeSet<E>(new Comparator<E>(){
            @SuppressWarnings("unchecked")
            @Override
            public int compare(E e1,E e2){
               if (!predicate.apply(e1)) return 0;
               return predicate.compare(e1,e2);
            }
         }
      );
   }
   /**
    * 挿入時制限付きTreeMap作成.
    * <br/>  Comparator を指定しません。Key K の java.lang.Comparable 実装に従います。
    * @param predicate TreePredicate実装インスタンスを指定
    * @return Map&lt;K, V&gt;
    */
   public static <K,V> Map<K,V> createTreeMap(final TreePredicate<K> predicate){
      return new TreeMap<K,V>(new Comparator<K>(){
            @SuppressWarnings("unchecked")
            @Override
            public int compare(K k1,K k2){
               if (!predicate.apply(k1)) return 0;
               return ((Comparable)k1).compareTo(k2);
            }
         }
      );
   }
   /**
    * 挿入時制限付き Comparator が指定されたTreeMap作成.
    * @param predicate ComparePredicate実装インスタンスを指定
    * @return Map&lt;K, V&gt;
    */
   public static <K,V> Map<K,V> createTreeMap(final ComparePredicate<K> predicate){
      return new TreeMap<K,V>(new Comparator<K>(){
            @SuppressWarnings("unchecked")
            @Override
            public int compare(K k1,K k2){
               if (!predicate.apply(k1)) return 0;
               return predicate.compare(k1,k2);
            }
         }
      );
   }
}
