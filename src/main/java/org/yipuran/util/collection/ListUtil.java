package org.yipuran.util.collection;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.function.Function;

/**
 * リストユーティリティ.
 */
public final class ListUtil{

	/** private constructor. */
	private ListUtil(){}

	/**
	 * リスト→グループ開始index：カウントのマップ生成.
	 * @param list リスト
	 * @param getKey 要素からグルーピングするキー文字列を取得する Function
	 * @return key=グループ開始index , value=グルーピングしたカウント
	 */
	public static <E> Map<Integer, Integer> groupingCountIndexMap(List<E> list, Function<E, String> getKey){
		Map<Integer, Integer> map = new HashMap<>();
		if (list.size()==0) return map;
		int index = 0;
		int count = 1;
		int row = 0;
		ListIterator<E> it = list.listIterator();
		while(it.hasNext()){
			String s = getKey.apply(it.next());
			if (it.hasNext()){
				if (s.equals(getKey.apply(it.next()))){
					count++;
				}else{
					map.put(row, count);
					count = 1;
					row = index + 1;
				}
				it.previous();
			}
			index++;
		}
		map.put(row, count);
		return map;
	}

}
