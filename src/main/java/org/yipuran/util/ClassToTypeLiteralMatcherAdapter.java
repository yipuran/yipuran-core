package org.yipuran.util;

import com.google.inject.TypeLiteral;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matcher;
/**
 * ClassToTypeLiteralMatcherAdapter.
 * <PRE>
 * Matchers.subclassesOf で指定するインターフェースの実装またはサブクラスで
 * TypeListener実装インスタンス＝typeListener をインジェクトさせるために、
 * Google guice Module の bindListener で、以下に記述する。
 *
 * bindListener(new ClassToTypeLiteralMatcherAdapter(Matchers.subclassesOf(Foo.class)), typeListener);
 * </PRE>
 */
@SuppressWarnings("rawtypes")
public class ClassToTypeLiteralMatcherAdapter extends AbstractMatcher<TypeLiteral>{
	private final Matcher<Class> classMatcher;

	/**
	 * コンストラクタ.
	 * @param classMatcher クラスMatcher
	 */
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
