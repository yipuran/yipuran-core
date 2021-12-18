package org.yipuran.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * インターセプターを約束する。.
 * <br>
 * MethodInterceptorを継承したクラスにインターセプトすることを目的に、
 * インターセプトするメソッドに、この注釈を付加する。
 * <pre>
 * アプリで用意する Module 実装クラスの com.google.inject.Module#configure(com.google.inject.Binder)
 * で、定義するインターセプタのバインドは、
 * 例として、クラス の中のメソッドに、＠Intercept("aaa") を付けた
 * メソッドを、ALogicIntercepter というインターセプタを実行したい場合は、以下のようにする。
 * binder.bindInterceptor(Matchers.inPackage(ALogic.class.getPackage())
 *                       ,MethodMatcher.annotatedWith(Intercept.class,"aaa")
 *                       ,new ALogicIntercepter());
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Intercept{
	String value();
}
