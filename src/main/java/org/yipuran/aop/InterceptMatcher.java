package org.yipuran.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matcher;

/**
 * インターセプトMatcher .
 * <pre>
 * ＠Intercept限定のメソッドの Matcher
 * com.google.inject.Module 実装クラスのBinder#bindInterceptor の２番目の引数で
 * 指定するアノテーションのMatcher  バインド定義で使用する。
 *
 * ＠Intercept アノテーションで指定する value 値と指定文字列が一致する時にインターセプトする。
 *
 * 例）クラスの中のメソッドに、＠Intercept("aaa") を付けた
 * メソッドを、ALogicIntercepter というインターセプタを実行したい場合は、以下のようにする。
 * binder.bindInterceptor(Matchers.inPackage(ALogic.class.getPackage())
 *                       ,InterceptMatcher.annotatedWith("aaa")
 *                       ,new ALogicIntercepter());
 *
 * InterceptMatcher を使用する代わりに、MethodMatcherの
 *        public static Matcher<AnnotatedElement> annotatedWith(final Class<? extends Annotation> annotation
 *                                                             ,final String reqStr)
 * を使っても同じことである。
 * </pre>
 */
public final class InterceptMatcher {
	private InterceptMatcher(){}
	/**
	 * ＠Intercept アノテーションで指定する value 値と指定文字列が一致する時にインターセプトする。
	 * @param annotationType
	 * @param reqStr 指定文字列 JVM内でユニークなKey
	 * @return Matcher<AnnotatedElement>
	 */
	public static Matcher<AnnotatedElement> annotatedWith(final String reqStr){
		//Objects.nonNull(annotationType,"annotation type");
		return new AbstractMatcher<AnnotatedElement>(){
			@Override
			public boolean matches(AnnotatedElement element) {
				Annotation annotation = element.getAnnotation(Intercept.class);
				if (annotation==null){
					return false;
				}
				return ((Intercept)annotation).value().equals(reqStr);
			}
			@Override
		   public String toString() {
				return "annotatedWith(Intercept.class,"+reqStr+")";
			}
		};
	}
}
