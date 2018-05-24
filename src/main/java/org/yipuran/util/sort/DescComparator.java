package org.yipuran.util.sort;

import java.lang.reflect.Method;
import java.util.Comparator;

import javax.inject.Inject;

class DescComparator<T> implements Comparator<T>{
	@Inject private Method getterMethod;

	@SuppressWarnings("unchecked")
	@Override
	public int compare(T o1,T o2){
		try{
			return ((Comparable<T>)this.getterMethod.invoke(o1)).compareTo((T)this.getterMethod.invoke(o2)) >= 0 ? -1 : 1;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
}
