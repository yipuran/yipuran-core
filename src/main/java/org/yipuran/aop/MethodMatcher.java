package org.yipuran.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matcher;
//import com.google.inject.util.Objects;

/**
 * メソッドインターセプトMatcher .
 * <pre>
 * com.google.inject.Module 実装クラスのBinder#bindInterceptor の２番目の引数で
 * 指定するアノテーションのMatcherバインド定義で使用する。
 *
 * メソッドに指定アノテーションでが付いていてアノテーションで指定する value 値と指定文字列が一致する時
 * または、メソッドに付けたアノテーションが指定アノテーションと一致した時にインターセプトする。
 *
 * 例）クラス の中のメソッドに、文字列引数を持つアノテーションが指定されている場合で、
 * 　  メソッドを、ALogicIntercepter というインターセプタを実行したい場合は、以下のようにする。
 *             ＠Retention(RetentionPolicy.RUNTIME)
 *             ＠Target(ElementType.METHOD)
 *             public ＠interface Mxservice{
 *                String value();
 *             }
 *
 *             ＠Mxservice("aaa")  public void foo(){ .... }
 *
 *
 *     binder.bindInterceptor(Matchers.inPackage(ALogic.class.getPackage())
 *                         　,MethodMatcher.annotatedWith(Mxservice.class,"aaa")
 *                           ,new ALogicIntercepter());
 *     この場合、foo() が、ALogicIntercepter でインターセプトされる。
 *
 * 例のように、メソッドに付けたアノテーションが文字列キー引数を持たずに、
 * 指定するアノテーションだけの一致で、インターセプトをバインドする場合は、
 * 　　public static Matcher&lt;AnnotatedElement&gt; annotatedWith(final Class&lt;? extends Annotation&gt; annotation)
 * を使用する。
 *
 * </pre>
 */
public final class MethodMatcher {
	private MethodMatcher(){}
	/**
	 * Keyワード指定アノテーションMatcher
	 * <br>
	 * 任意アノテーションで指定する value 値と指定文字列が一致する時にインターセプトする。
	 * @param annotation メソッドに付けた任意アノテーション
	 * @param reqStr 指定文字列 JVM内でユニークなKey
	 * @return Matcher<AnnotatedElement> bindInterceptorの第２引数に与えるもの
	 */
	public static Matcher<AnnotatedElement> annotatedWith(final Class<? extends Annotation> annotation, final String reqStr){
		//Objects.nonNull(annotation,"annotation type");
		return new AbstractMatcher<AnnotatedElement>(){
			@Override
			public boolean matches(AnnotatedElement element) {
				Annotation an = element.getAnnotation(annotation);
				if (an==null){
					return false;
				}
				try{
					Method m = Class.forName(annotation.getName()).getMethod("value");
					return ((String)m.invoke(an)).equals(reqStr);
				}catch(Exception e){
					return false;
				}
			}
			@Override
			public String toString() {
				return "annotatedWith("+annotation.getSimpleName()+".class,"+reqStr+")";
			}
		};
	}
	/**
	 * 指定アノテーションMatcher.
	 * <br>
	 * 指定アノテーションと一致する時にインターセプトする。
	 * @param annotation メソッドに付けた任意アノテーション
	 * @return Matcher<AnnotatedElement> bindInterceptorの第２引数に与えるもの
	 */
	public static Matcher<AnnotatedElement> annotatedWith(final Class<? extends Annotation> annotation){
		//Objects.nonNull(annotation,"annotation type");
		return new AbstractMatcher<AnnotatedElement>(){
			@Override
			public boolean matches(AnnotatedElement element) {
				Annotation an = element.getAnnotation(annotation);
				if (an==null){
					return false;
				}
				return an.annotationType().equals(annotation);
			}
			@Override
			public String toString() {
				return "annotatedWith("+annotation.getSimpleName()+".class)";
			}
		};
	}

	public static Matcher onMethod(final Method method){
		return new AbstractMatcher<Method>(){
			@Override
			public boolean matches(Method m){
				return m.equals(method);
			}
		};
	}
}
