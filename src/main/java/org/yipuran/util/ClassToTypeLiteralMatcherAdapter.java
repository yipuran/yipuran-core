package org.yipuran.util;

import com.google.inject.TypeLiteral;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matcher;
/**
 * ClassToTypeLiteralMatcherAdapter.
 *
 * bindListener(new ClassToTypeLiteralMatcherAdapter(Matchers.subclassesOf(Foo.class)),typeListener);
 *
 */
public class ClassToTypeLiteralMatcherAdapter extends AbstractMatcher<TypeLiteral>{
	private final Matcher<Class> classMatcher;

	public ClassToTypeLiteralMatcherAdapter(Matcher<Class> classMatcher){
		this.classMatcher = classMatcher;
	}
	/*
	 * @see com.google.inject.matcher.Matcher#matches(java.lang.Object)
	 */
	@Override
	public boolean matches(TypeLiteral typeLiteral){
		return this.classMatcher.matches(typeLiteral.getRawType());
	}
}
