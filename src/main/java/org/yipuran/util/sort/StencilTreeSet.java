package org.yipuran.util.sort;

import java.util.Comparator;
import java.util.TreeSet;

import javax.inject.Inject;

class StencilTreeSet<T> extends TreeSet<T>{
	private static final long serialVersionUID = 1L;
	@Inject
	protected StencilTreeSet(Comparator<T> c){
		super(c);
	}
}
