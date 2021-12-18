package org.yipuran.util.sort;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.TreeSet;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * 自動ソートされる java.util.Set 実装の TreeSet 作成.
 * <pre>
 * Comparator 実装クラスを書かずに自動ソートされる TreeSet 作成するファクトリです。
 * Set に格納されるオブジェクトは、ソート対象キーが、Comparable を実装したインスタンス
 * であること、ソート対象キーを取得するメソッドが public であることが条件である。
 * Comparator 実装クラスは、内部で非 static で用意され compareTo が実行される。
 *
 * 用意されたファクトリメソッドは以下のとおり。
 * 　// 昇順（ソートキー重複許可）
 * 　public static TreeSet createASC(final Class<?> cls,final String getterName)
 * 　// 昇順（ソートキー重複禁止、先行が残る）
 * 　public static TreeSet createUniqueASC(final Class<?> cls,final String getterName)
 * 　// 降順（ソートキー重複許可）
 * 　public static TreeSet createDESC(final Class<?> cls,final String getterName)
 * 　// 降順（ソートキー重複禁止、先行が残る）
 * 　public static TreeSet createUniqueDESC(final Class<?> cls,final String getterName)
 *
 * </pre>
 */
public final class TreeSetFactory{
   private TreeSetFactory(){}

   /**
    * 昇順（ソートキー重複許可）
    * @param cls TreeSetに格納するインスタンスクラス
    * @param getterName ソートキーを取得するメソッド名、Comparable を実装したものを返すメソッドであること
    * @return TreeSet
    */
   @SuppressWarnings("unchecked")
   public static <T> TreeSet<T> createASC(final Class<T> cls,final String getterName){
      Injector injector = Guice.createInjector(new AbstractModule(){
            @Override
            protected void configure(){
               try{
               binder().bind(Method.class).toInstance(cls.getMethod(getterName));
               }catch(Exception e){
                  throw new RuntimeException(e);
               }
               binder().bind(Comparator.class).to(AscComparator.class);
            }
         }
      );
      return injector.getInstance(StencilTreeSet.class);
   }
   /**
    * 昇順（ソートキー重複禁止、先行が残る）
    * @param cls TreeSetに格納するインスタンスクラス
    * @param getterName ソートキーを取得するメソッド名、Comparable を実装したものを返すメソッドであること
    * @return TreeSet
    */
   @SuppressWarnings("unchecked")
   public static <T> TreeSet<T> createUniqueASC(final Class<?> cls,final String getterName){
      Injector injector = Guice.createInjector(new AbstractModule(){
            @Override
            protected void configure(){
               try{
               binder().bind(Method.class).toInstance(cls.getMethod(getterName));
               }catch(Exception e){
                  throw new RuntimeException(e);
               }
               binder().bind(Comparator.class).to(AscUniqueComparator.class);
            }
         }
      );
      return injector.getInstance(StencilTreeSet.class);
   }
   /**
    * 降順（ソートキー重複許可）
    * @param cls TreeSetに格納するインスタンスクラス
    * @param getterName ソートキーを取得するメソッド名、Comparable を実装したものを返すメソッドであること
    * @return TreeSet
    */
   @SuppressWarnings("unchecked")
   public static <T> TreeSet<T> createDESC(final Class<T> cls,final String getterName){
      Injector injector = Guice.createInjector(new AbstractModule(){
            @Override
            protected void configure(){
               try{
               binder().bind(Method.class).toInstance(cls.getMethod(getterName));
               }catch(Exception e){
                  throw new RuntimeException(e);
               }
               binder().bind(Comparator.class).to(DescComparator.class);
            }
         }
      );
      return injector.getInstance(StencilTreeSet.class);
   }
   /**
    * 降順（ソートキー重複禁止、先行が残る）
    * @param cls TreeSetに格納するインスタンスクラス
    * @param getterName ソートキーを取得するメソッド名、Comparable を実装したものを返すメソッドであること
    * @return TreeSet
    */
   @SuppressWarnings("unchecked")
   public static <T> TreeSet<T> createUniqueDESC(final Class<T> cls,final String getterName){
      Injector injector = Guice.createInjector(new AbstractModule(){
            @Override
            protected void configure(){
               try{
               binder().bind(Method.class).toInstance(cls.getMethod(getterName));
               }catch(Exception e){
                  throw new RuntimeException(e);
               }
               binder().bind(Comparator.class).to(DescUniqueComparator.class);
            }
         }
      );
      return injector.getInstance(StencilTreeSet.class);
   }
}
