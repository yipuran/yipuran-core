package org.yipuran.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.aopalliance.intercept.MethodInvocation;

/**
 * インターセプタで利用するリフレクションユーティリティ.
 * <pre>
 * 主に、org.aopalliance.intercept.MethodInterceptor 実装で利用する
 * org.aopalliance.intercept.MethodInvocation からメソッド実行時の引数の値をリフレクト、
 * 等を提供する。
 * </pre>
 * @since 1.1.2
 */
public final class AOPUtils{
	private AOPUtils(){}

	/**
	 * インターセプト対象メソッドの引数から、指定アノテーションが付いた引数の値を取得する.
	 * @param m MethodInvocation
	 * @param a 探したいアノテーション
	 * @param type アノテーションが付いた引数のタイプ、String[] なら、"[Ljava.lang.String;" である。
	 * @return 引数の値の配列、NULLはそのまま返す。
	 */
	public static Object[] getAnotatedParamValues(MethodInvocation m,Class<? extends Annotation> a,String type){
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
	 * インターセプト対象メソッドの引数から、指定アノテーションが付いた引数１個のみの値を取得する.
	 * <pre>
	 * Object[] getAnotatedParamValues と異なり、指定するアノテーションが見つからない場合、
	 * RuntimeException をスローします。
	 * </pre>
	 * @param m MethodInvocation
	 * @param a 探したいアノテーション
	 * @param type アノテーションが付いた引数のタイプ、String[] なら、"[Ljava.lang.String;" である。
	 * @return 引数の値、NULLはそのまま返す。
	 * @throws RuntimeException 指定するアノテーションが見つからない場合
	 */
	public static Object getAnotatedParamValue(MethodInvocation m,Class<? extends Annotation> a,String type){
		Object rtn=null;
		Object[] args = m.getArguments();
		Annotation[][] anos = m.getMethod().getParameterAnnotations();
		boolean unfind=true;
		for(int n=0;n < anos.length;n++){
			for(int k=0;k < anos[n].length;k++){
				if (anos[n][k].annotationType().equals(a)){
					if (args[n]!=null){
						if (args[n].getClass().getName().equals(type)){
							rtn = args[n];
							unfind = false;
							n = anos.length;
							break;
						}
					}
				}
			}
		}
		if (unfind){
			throw new RuntimeException("Not found anotation parameter "+a.getName()+" type="+type);
		}
		return rtn;
	}

	/**
	 * 指定アノテーションが付くフィールドを配列で取得.
	 * <pre>
	 * 取得したフィールドに値をセットしたい場合は、取得先で、Filed#setAccessible(true); を実行すること
	 * </pre>
	 * @param a アノテーションクラス
	 * @param cls 検索するクラス
	 * @return Field[] 存在しないと空の配列
	 */
	public static Field[] getAnotatedFields(Class<? extends Annotation> a,Class<?> cls){
		List<Field> list = new ArrayList<Field>();
		Field[] fls = cls.getDeclaredFields();
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

	/**
	 * 指定アノテーションが付くフィールドを配列で取得.
	 * <pre>
	 * 取得したフィールドに値をセットしたい場合は、取得先で、Filed#setAccessible(true); を実行すること
	 * </pre>
	 * @param a アノテーションクラス
	 * @param cls 検索するクラス
	 * @return Field 存在しないと NULL を返す
	 */
	public static Field getAnotatedField(Class<? extends Annotation> a,Class<?> cls){
		Field rtn=null;
		Field[] fls = cls.getDeclaredFields();
		for(int i=0;i < fls.length;i++){
			Annotation[] as = fls[i].getAnnotations();
			if (as != null){
				for(int k=0;k < as.length;k++){
					if (as[k].annotationType().equals(a)){
						rtn = fls[i];
						i = fls.length;
						break;
					}
				}
			}
	   }
	   return rtn;
	}
	/**
	 * アノテーション リストからアノテーションを探す
	 * @param cls アノテーションクラス
	 * @param list 対象のアノテーションリスト
	 * @return 検索結果アノテーション
	 */
	public static Annotation findAnnotation(Class<? extends Annotation> cls,List<Annotation> list){
		Annotation rtn = null;
		for(Annotation a : list){
			if (cls.equals(a.annotationType())){
				rtn = a;
			}
		}
		return rtn;
	}
}
