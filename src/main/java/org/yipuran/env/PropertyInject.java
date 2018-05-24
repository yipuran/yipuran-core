package org.yipuran.env;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * PropertyBindModule → Properties Inject アノテーション.
 * <pre>
 * PropertyBindModule を使用したProperties変数へのインジェクトを約束する。
 *
 *      ＠Inject ＠PropertyInject private Properties prop;
 *      に対して、
 *      Injector injector = Guice.createInjector(new PropertyBindModule("aaa"));
 *      とすることで、aaa.properties を読んでPropertiesにインジェクトする。
 * propertiesファイルが見つからない場合、空のPropertiesがインジェクトされる。
 *
 * PropertyBindModuleコンストラクタ引数有無に関わらず、PropertyInject アノテーションでリソースキーを指定すると、
 *   PropertyInject で指定したリソースキーが優先される
 *       ＠Inject ＠PropertyInject("aaa") private Properties prop;
 *      → new PropertyBindModule() 実行、new PropertyBindModule("bbb") 実行でも
 *      aaa.properties を読込もうとする。
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PropertyInject{
	public String value() default "";
}
