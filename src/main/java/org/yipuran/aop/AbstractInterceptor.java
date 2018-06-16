package org.yipuran.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * メソッドインターセプタ抽象クラス.
 * @since 1.1.2
 */
public abstract class AbstractInterceptor implements MethodInterceptor{
	private Class<?> cls;

	/**
	 * コンストラクタ.
	 * @param cls インターセプトされるメソッドを含むクラス定義
	 */
	public AbstractInterceptor(Class <?> cls){
		this.cls = cls;
	}

	/**
	 * インターセプト対象メソッドの引数から、指定アノテーションが付いた引数の値を取得する.
	 * @param m MethodInvocation
	 * @param a 探したいアノテーション
	 * @param type アノテーションが付いた引数のタイプ、String[] なら、"[Ljava.lang.String;" である。
	 * @return 引数の値の配列、NULLはそのまま返す。
	 */
	public Object[] getAnotatedParamValue(MethodInvocation m,Class<? extends Annotation> a,String type){
		Object[] args = m.getArguments();
		Annotation[][] anos = m.getMethod().getParameterAnnotations();
		List<Object> list = new ArrayList<Object>();
		for(int n=0;n < anos.length;n++){
			for(int k=0;k < anos[n].length;k++){
				if (anos[n][k].annotationType().equals(a)){
					if (args[n]==null){
						list.add(null);
					}else if(args[n].getClass().getName().equals(type)){
						list.add(args[n]);
					}
				}
			}
		}
		Object[] rtns=new Object[list.size()];
		for(int i=0;i < list.size();i++){
			rtns[i] = list.get(i);
		}
		return rtns;
	}
	/**
	 * 指定アノテーションが付くフィールドを取得.
	 * <pre>
	 * 取得したフィールドに値をセットしたい場合は、取得先で、Filed#setAccessible(true); を実行すること
	 * </pre>
	 * @param a アノテーションクラス
	 * @return Field[]  存在しないと空の配列
	 */
	public Field[] getAnotatedFields(Class<? extends Annotation> a){
		List<Field> list = new ArrayList<Field>();
		Field[] fls = this.cls.getDeclaredFields();
		for(int i=0;i < fls.length;i++){
			Annotation[] as = fls[i].getAnnotations();
			if (as != null){
				for(int k=0;k < as.length;k++){
					if (as[k].annotationType().equals(a)){
						list.add(fls[i]);
					}
				}
			}
		}
		Field[] rtns=new Field[list.size()];
		for(int i=0;i < list.size();i++){
			rtns[i] = list.get(i);
		}
		return rtns;
	}
}
