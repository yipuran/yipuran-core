package org.yipuran.util.collection;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * 隣接ペアリスト作成.
 * <PRE>
 * List&lt;String&gt; list = Arrays.asList("a","b","c","d","e","f","g","h");
 * AbstractMap.SimpleEntry で、
 * 以下のリストを作成する
 *   key=a value=b
 *   key=b value=c
 *   key=c value=d
 *   key=d value=e
 *   key=e value=f
 *   key=f value=g
 *   key=g value=h
 * </PRE>
 */
public final class AdjacentPairList{
	private AdjacentPairList(){}

	/**
	 * 隣接ペアリスト作成.
	 * （注）リストが１個しかない時は、作らない。
	 * @param list リスト
	 * @return List&lt;AbstractMap.SimpleEntry&lt;E, E>>
	 */
	public <E> List<AbstractMap.SimpleEntry<E, E>> create(List<E> list){
		List<AbstractMap.SimpleEntry<E, E>> plist = new ArrayList<>();
		if (list==null) return plist;
		if (list.size() < 2) return plist;
		for(ListIterator<E> it=list.listIterator(1); it.hasNext();){
		   plist.add(new AbstractMap.SimpleEntry<E, E>(list.get(it.nextIndex()-1), it.next()));
		}
		return plist;
	}

}
