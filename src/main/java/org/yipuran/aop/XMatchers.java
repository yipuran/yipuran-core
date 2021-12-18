package org.yipuran.aop;

import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.Collectors;

import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matcher;

/**
 * Matcher論理和 OR を提供する。
 * <PRE>
 * Google guice bindInterceptor を実行する時のMatcher で OR を指定するためのメソッドを提供。
 *
 * サンプル
 * binder().bindInterceptor(
 * 		XMatchers.or(Matchers.inPackage(Apple.class.getPackage()),
 * 			Matchers.inPackage(PTestmain.class.getPackage()),
 * 			Matchers.inPackage(Test1.class.getPackage())
 * 		),
 * 		XMatchers.or(
 * 			Matchers.annotatedWith(Names.named("A")),
 * 			Matchers.annotatedWith(Nanes.named("B"))
 * 		),
 * 		new MyInterceptor()
 * );
 * </PRE>
 * @since 4.31
 */
public class XMatchers{
	private XMatchers(){}

	/**
	 * マッチの OR
	 * @param m Matcherを指定する可変長引数
	 * @return 論理和の結果の Matcher
	 */
	public static Matcher<Object> or(Object...m) {
		 return new OR(m);
	}

	private static class OR extends AbstractMatcher<Object> implements Serializable {
		private static final long serialVersionUID = 1L;
		Object[] ms;

		public OR(Object[] m){
			ms = m;
		}
		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		public boolean matches(Object t){
			boolean rtn = false;
			for(int n=0;n < ms.length; n++){
				rtn = rtn | ((Matcher)ms[n]).matches(t);
			}
			return rtn;
		}
		@Override
		public String toString(){
			StringBuilder sb = new StringBuilder("or(");
			sb.append(Arrays.stream(ms).map(e->e.toString()).collect(Collectors.joining(",")));
			sb.append(")");
			return sb.toString();
		}
	}
}
