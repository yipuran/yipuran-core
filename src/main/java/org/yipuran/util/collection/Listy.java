package org.yipuran.util.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.google.common.collect.MapDifference.ValueDifference;

/**
 * Listy.
 * <pre>
 * use com.google.common.collect.MapDifference
 * List の Diff を実行して結果を List １要素単位でDiff結果表現する ListDifference の
 * Collection を返す。
 * Diff 結果として返すものは、
 *   共通行である ListDifference の Collection
 *   左と右で異なる行（同一index）である ListDifference の Collection
 *   左側にしか存在しない行の ListDifference の Collection
 *   右側にしか存在しない行の ListDifference の Collection
 * である。
 * 右側、左側しか存在しない行がある＝ List のサイズが異なるものである。
 * これら、Diff結果を求めるための方法として
 * static メソッドとコンストラクタでインスタンスを生成してから結果を取得する方法、
 * 効率性を考えて２通りを用意している。
 * Diff結果全てを求める場合はコンストラクタでインスタンスを生成した方が良い。
 * </pre>
 */
public final class Listy<E>{
   private Map<ListyDiffType,Collection<?>> map;
   /**
    * コンストラクタ
    * @param left 左側 java.util.List
    * @param right 右側 java.util.List
    */
   public Listy(List<E> left,List<E> right){
      this.map = new HashMap<ListyDiffType,Collection<?>>();
      Set<ListDifference<E>> setDiff = new TreeSet<ListDifference<E>>();
      Set<ListDifference<E>> setCommon = new TreeSet<ListDifference<E>>();
      Set<ListDifference<E>> setLeft = new TreeSet<ListDifference<E>>();
      Set<ListDifference<E>> setRight = new TreeSet<ListDifference<E>>();
      Map<Long,E> lmap = new HashMap<Long,E>();
      Map<Long,E> rmap = new HashMap<Long,E>();
      long ix = 0;
      for(E e : left){
         lmap.put(new Long(ix),e);
         ix++;
      }
      ix = 0;
      for(E e : right){
         rmap.put(new Long(ix),e);
         ix++;
      }
      MapDifference<Long,E> mdiff = Maps.difference(lmap,rmap);
      Map<Long,ValueDifference<E>> vmap = mdiff.entriesDiffering();
      for(final Long lx : vmap.keySet()){
         ValueDifference<E> vdiff = vmap.get(lx);
         setDiff.add(new ListDifference<E>(lx,vdiff.leftValue(),vdiff.rightValue()));
      }
      Map<Long,E> vlmap = mdiff.entriesOnlyOnLeft();
      for(Long lx : vlmap.keySet()){
         setLeft.add(new ListDifference<E>(lx,vlmap.get(lx),null));
      }
      Map<Long,E> vrmap = mdiff.entriesOnlyOnRight();
      for(Long lx : vrmap.keySet()){
         setRight.add(new ListDifference<E>(lx,null,vrmap.get(lx)));
      }
      Map<Long,E> mapCommon = mdiff.entriesInCommon();
      for(final Long lx : mapCommon.keySet()){
         setCommon.add(new ListDifference<E>(lx,mapCommon.get(lx),mapCommon.get(lx)));
      }
      this.map.put(ListyDiffType.DIFFERING,setDiff);
      this.map.put(ListyDiffType.COMMON,setCommon);
      this.map.put(ListyDiffType.ONLY_LEFT,setLeft);
      this.map.put(ListyDiffType.ONLY_RIGHT,setRight);
   }

   enum ListyDiffType{
      DIFFERING,COMMON,ONLY_LEFT,ONLY_RIGHT
   }

   /**
    * List 差が発生した行の結果Collection
    * @return Collection<ListDifference<E>>
    */
   @SuppressWarnings("unchecked")
   public Collection<ListDifference<E>> differing(){
      return (Collection<ListDifference<E>>)this.map.get(ListyDiffType.DIFFERING);
   }

   /**
    * List 共通行の結果Collection
    * @return Collection<ListDifference<E>>
    */
   @SuppressWarnings("unchecked")
   public Collection<ListDifference<E>> getCommons(){
      return (Collection<ListDifference<E>>)this.map.get(ListyDiffType.COMMON);
   }

   /**
    * 左側Only List の結果Collection
    * @return Collection<ListDifference<E>>
    */
   @SuppressWarnings("unchecked")
   public Collection<ListDifference<E>> getOnlyLeft(){
      return (Collection<ListDifference<E>>)this.map.get(ListyDiffType.ONLY_LEFT);
   }

   /**
    * 右側Only List の結果Collection
    * @return Collection<ListDifference<E>>
    */
   @SuppressWarnings("unchecked")
   public Collection<ListDifference<E>> getOnlyRight(){
      return (Collection<ListDifference<E>>)this.map.get(ListyDiffType.ONLY_RIGHT);
   }

   /**
    * 全てのListDifference List index順
    * @return
    */
   @SuppressWarnings("unchecked")
   public Collection<ListDifference<E>> allDifference(){
      Set<ListDifference<E>> rtn = new TreeSet<ListDifference<E>>();
      rtn.addAll((Collection<ListDifference<E>>)this.map.get(ListyDiffType.COMMON));
      rtn.addAll((Collection<ListDifference<E>>)this.map.get(ListyDiffType.DIFFERING));
      rtn.addAll((Collection<ListDifference<E>>)this.map.get(ListyDiffType.ONLY_LEFT));
      rtn.addAll((Collection<ListDifference<E>>)this.map.get(ListyDiffType.ONLY_RIGHT));
      return rtn;
   }

   /**
    * 左 List → 右 List マージ.
    * 右 List size ＞ 左 List size で実行時に意味がある。
    * 戻り値 左 List size になることを期待する場合、本メソッドを実行する必要がない
    * @return
    */
   @SuppressWarnings("unchecked")
   public List<E> mergeLeftToRight(){
      List<E> rtn = new ArrayList<E>();
      Set<ListDifference<E>> vset = new TreeSet<ListDifference<E>>();
      for(Object o : this.map.get(ListyDiffType.COMMON)){
         vset.add((ListDifference<E>)o);
      }
      for(Object o : this.map.get(ListyDiffType.DIFFERING)){
         ListDifference<E> ld = (ListDifference<E>)o;
         vset.add(new ListDifference<E>(ld.getIndex(),ld.leftValue(),ld.leftValue()));
      }
      for(Object o : this.map.get(ListyDiffType.ONLY_RIGHT)){
         vset.add((ListDifference<E>)o);
      }
      for(ListDifference<E> ld : vset){
         rtn.add(ld.rightValue());
      }
      return rtn;
   }

   /**
    * 右 List → 左 List マージ.
    * 左 List size ＞ 右 List size で実行時に意味がある。
    * 戻り値 右 List size になることを期待する場合、本メソッドを実行する必要がない
    * @return
    */
   @SuppressWarnings("unchecked")
   public List<E> mergeRightToLeft(){
      List<E> rtn = new ArrayList<E>();
      Set<ListDifference<E>> vset = new TreeSet<ListDifference<E>>();
      for(Object o : this.map.get(ListyDiffType.COMMON)){
         vset.add((ListDifference<E>)o);
      }
      for(Object o : this.map.get(ListyDiffType.DIFFERING)){
         ListDifference<E> ld = (ListDifference<E>)o;
         vset.add(new ListDifference<E>(ld.getIndex(),ld.rightValue(),ld.rightValue()));
      }
      for(Object o : this.map.get(ListyDiffType.ONLY_LEFT)){
         vset.add((ListDifference<E>)o);
      }
      for(ListDifference<E> ld : vset){
         rtn.add(ld.leftValue());
      }
      return rtn;
   }

   /**
    * List 差分を求める
    * @param left 左側 java.util.List
    * @param right 右側 java.util.List
    * @return Collection<ListDifference<E>>
    */
   public static <E> Collection<ListDifference<E>> differing(List<E> left,List<E> right){
      Set<ListDifference<E>> setDiff = new TreeSet<ListDifference<E>>();
      Map<Long,E> lmap = new HashMap<Long,E>();
      Map<Long,E> rmap = new HashMap<Long,E>();
      long ix = 0;
      for(E e : left){
         lmap.put(new Long(ix),e);
         ix++;
      }
      ix = 0;
      for(E e : right){
         rmap.put(new Long(ix),e);
         ix++;
      }
      MapDifference<Long,E> mdiff = Maps.difference(lmap,rmap);
      Map<Long,ValueDifference<E>> vmap = mdiff.entriesDiffering();
      for(final Long lx : vmap.keySet()){
         ValueDifference<E> vdiff = vmap.get(lx);
         setDiff.add(new ListDifference<E>(lx,vdiff.leftValue(),vdiff.rightValue()));
      }
      return setDiff;
   }

   /**
    * List 共通行を求める
    * @param left 左側 java.util.List
    * @param right 右側 java.util.List
    * @return Collection<ListDifference<E>>
    */
   public static <E> Collection<ListDifference<E>> getCommons(List<E> left,List<E> right){
      Set<ListDifference<E>> setCommon = new TreeSet<ListDifference<E>>();
      Map<Long,E> lmap = new HashMap<Long,E>();
      Map<Long,E> rmap = new HashMap<Long,E>();
      long ix = 0;
      for(E e : left){
         lmap.put(new Long(ix),e);
         ix++;
      }
      ix = 0;
      for(E e : right){
         rmap.put(new Long(ix),e);
         ix++;
      }
      MapDifference<Long,E> mdiff = Maps.difference(lmap,rmap);
      Map<Long,E> vmap = mdiff.entriesInCommon();
      for(final Long lx : vmap.keySet()){
         setCommon.add(new ListDifference<E>(lx,vmap.get(lx),vmap.get(lx)));
      }
      return setCommon;
   }

   /**
    * List 左側だけに存在する行を求める
    * @param left 左側 java.util.List
    * @param right 右側 java.util.List
    * @return Collection<ListDifference<E>>
    */
   public static <E> Collection<ListDifference<E>> getOnlyLeft(List<E> left,List<E> right){
      Set<ListDifference<E>> setLeft = new TreeSet<ListDifference<E>>();
      Map<Long,E> lmap = new HashMap<Long,E>();
      Map<Long,E> rmap = new HashMap<Long,E>();
      long ix = 0;
      for(E e : left){
         lmap.put(new Long(ix),e);
         ix++;
      }
      ix = 0;
      for(E e : right){
         rmap.put(new Long(ix),e);
         ix++;
      }
      MapDifference<Long,E> mdiff = Maps.difference(lmap,rmap);
      Map<Long,E> vmap = mdiff.entriesOnlyOnLeft();
      for(final Long lx : vmap.keySet()){
         setLeft.add(new ListDifference<E>(lx,vmap.get(lx),null));
      }
      return setLeft;
   }

   /**
    * List 右側だけに存在する行を求める
    * @param left 左側 java.util.List
    * @param right 右側 java.util.List
    * @return Collection<ListDifference<E>>
    */
   public static <E> Collection<ListDifference<E>> getOnlyRight(List<E> left,List<E> right){
      Set<ListDifference<E>> setRight = new TreeSet<ListDifference<E>>();
      Map<Long,E> lmap = new HashMap<Long,E>();
      Map<Long,E> rmap = new HashMap<Long,E>();
      long ix = 0;
      for(E e : left){
         lmap.put(new Long(ix),e);
         ix++;
      }
      ix = 0;
      for(E e : right){
         rmap.put(new Long(ix),e);
         ix++;
      }
      MapDifference<Long,E> mdiff = Maps.difference(lmap,rmap);
      Map<Long,E> vmap = mdiff.entriesOnlyOnRight();
      for(final Long lx : vmap.keySet()){
         setRight.add(new ListDifference<E>(lx,null,vmap.get(lx)));
      }
      return setRight;
   }

}
