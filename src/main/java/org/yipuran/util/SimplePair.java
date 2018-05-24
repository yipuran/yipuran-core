package org.yipuran.util;

import java.io.Serializable;
import java.util.AbstractMap;

/**
 * SimplePair.
 * @param <K>
 * @param <V>
 */
public class SimplePair<K, V> extends AbstractMap.SimpleEntry<K, V> implements Serializable{
	/**
	 * コンストラクタ.
	 * @param key key
	 * @param value value
	 */
	public SimplePair(K key, V value){
		super(key, value);
	}
}