package org.yipuran.util.pch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
/**
 * デカルト積（直積）生成.
 * <PRE>
 * 同じ型の Listを複数渡して、デカルト積を求める。
 * </PRE>
 */
public class Cartesian<T> {
    private List<T>[] lists;

    /**
     * 複数List →インスタンス生成.
     * @param lists 要素のリスト
     * @return インスタンス
     */
    @SuppressWarnings("unchecked")
    public static <T> Cartesian<T> of(List<T>...lists){
        if (lists.length < 1) throw new IllegalArgumentException("require argument List");
        return new Cartesian<T>(lists);
    }

    @SuppressWarnings("unchecked")
    private Cartesian(List<T>...lists){
        this.lists = lists;
    }

    /**
     * デカルト積生成
     * @return Listの入れ子
     */
    public List<List<T>> product(){
        int total = 1;
        int[] max = new int[lists.length];
        for(int i=0; i < lists.length; i++){
            max[i] = lists[i].size();
        }
        int[] initProduct = new int[lists.length];
        Arrays.fill(initProduct, 1);
        for(List<T> list:lists){
            total *= list.size();
        }
        List<List<T>> rlist = new ArrayList<>();
        int finalTotal = total;
        Iterator<List<T>> itr = new Iterator<List<T>>() {
            int index = -1;
            int[] presentProduct;
            @Override
            public boolean hasNext(){
                index++;
                return index < finalTotal;
            }
            @Override
            public List<T> next() {
                if (index == 0){
                    presentProduct = initProduct;
                }else{
                    presentProduct = generateNextProduct(presentProduct, max);
                }
                List<T> result = new ArrayList<>();
                for(int i=0; i < presentProduct.length; i++){
                    result.add(lists[i].get(presentProduct[i] - 1));
                }
                return result;
            }
        };
        itr.forEachRemaining(rlist::add);
        return rlist;
    }

    /**
     * デカルト積リストのIterable生成
     * @return List の Iterable
     */
    public Iterable<List<T>> iterable(){
        int total = 1;
        int[] max = new int[lists.length];
        for(int i=0; i < lists.length; i++){
            max[i] = lists[i].size();
        }
        int[] initProduct = new int[lists.length];
        Arrays.fill(initProduct, 1);
        for(List<T> list:lists){
            total *= list.size();
        }
        int finalTotal = total;
        return () -> new Iterator<List<T>>() {
            int index = -1;
            int[] presentProduct;
            @Override
            public boolean hasNext(){
                index++;
                return index < finalTotal;
            }
            @Override
            public List<T> next() {
                if (index == 0){
                    presentProduct = initProduct;
                }else{
                    presentProduct = generateNextProduct(presentProduct, max);
                }
                List<T> result = new ArrayList<>();
                for(int i=0; i < presentProduct.length; i++){
                    result.add(lists[i].get(presentProduct[i] - 1));
                }
                return result;
            }
        };
    }

    /**
     * Predicate で抑制したデカルト積リストのIterable生成
     * @param pred ListのPredicate を指定して生成するIterableを抑制する
     * @return List の Iterable
     */
    public Iterable<List<T>> iterable(Predicate<List<T>> pred){
        return () -> new Iterator<List<T>>() {
            Iterator<List<T>> sourceIterator = iterable().iterator();
            List<T> current;
            boolean hasCurrent = false;
            @Override
            public boolean hasNext() {
                while(!hasCurrent){
                    if (!sourceIterator.hasNext()) {
                        return false;
                    }
                    List<T> next = sourceIterator.next();
                    if (pred.test(next)) {
                        current = next;
                        hasCurrent = true;
                    }
                }
                return true;
            }
            @Override
            public List<T> next() {
                if(!hasNext()) throw new NoSuchElementException();
                hasCurrent = false;
                return current;
            }
        };
    }

    private int[] generateNextProduct(int[] curr, int[] max){
        int n = curr.length - 1;
        curr[n]++;
        for(int i=n; i > 0; i--){
            if (curr[i] > max[i]){
                curr[i] = 1;
                curr[i-1]++;
            }
        }
        return curr;
    }
}
