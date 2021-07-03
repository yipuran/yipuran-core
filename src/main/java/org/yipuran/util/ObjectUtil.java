package org.yipuran.util;

import java.lang.reflect.Method;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

/**
 * ObjectUtil
 */
public final class ObjectUtil{

	/**
	 * equalsメソッドを override しているかを判定する。
	 * @param cls 調査するクラス
	 * @return true=override済
	 */
	public static boolean isEqualsOverride(Class<?> cls){
		try{
			Method equalsMethod = cls.getMethod("equals", Object.class);
			Class<?> declaringClass = equalsMethod.getDeclaringClass();
			if (declaringClass.equals(Object.class)){
				return false;
			}
			try{
				declaringClass.getSuperclass().getMethod("equals", Object.class);
				return true;
			}catch(NoSuchMethodException e){
				for(Class<?> iface : declaringClass.getInterfaces()){
					try{
						iface.getMethod("equals", Object.class);
						return true;
					}catch(NoSuchMethodException e2){
					}
				}
				return false;
			}
		}catch(NoSuchMethodException | SecurityException e){
			throw new RuntimeException(e);
		}
	}

	/**
	 * hashCpdeメソッドを override しているかを判定する。
	 * @param cls 調査するクラス
	 * @return true=override済
	 */
	public static boolean isHashCodeOverride(Class<?> cls){
		try{
			Method hashCodeMethod = cls.getMethod("hashCode");
			Class<?> declaringClass = hashCodeMethod.getDeclaringClass();
			if (declaringClass.equals(Object.class)){
				return false;
			}
			try{
				declaringClass.getSuperclass().getMethod("hashCode");
				return true;
			}catch(NoSuchMethodException e){
				for(Class<?> iface : declaringClass.getInterfaces()){
					try{
						iface.getMethod("hashCode");
						return true;
					}catch(NoSuchMethodException e2){
					}
				}
				return false;
			}
		}catch(NoSuchMethodException | SecurityException e){
			throw new RuntimeException(e);
		}
	}
	/**
	 * equalsメソッドとhashCodeメソッド両方を override しているかを判定する。
	 * @param cls 調査するクラス
	 * @return true=override済
	 */
	public static boolean isEqualsHashCodeOverride(Class<?> cls){
		try{
			Method equalsMethod = cls.getMethod("equals", Object.class);
			Class<?> declarEqualsClass = equalsMethod.getDeclaringClass();
			if (declarEqualsClass.equals(Object.class)){
				return false;
			}
			Method hashCodeMethod = cls.getMethod("hashCode");
			Class<?> declarHashCodeClass = hashCodeMethod.getDeclaringClass();
			if (declarHashCodeClass.equals(Object.class)){
				return false;
			}
			try{
				declarEqualsClass.getSuperclass().getMethod("equals", Object.class);
				declarHashCodeClass.getSuperclass().getMethod("hashCode");
				return true;
			}catch(NoSuchMethodException e){
				boolean b = false;
				for(Class<?> iface : declarEqualsClass.getInterfaces()){
					try{
						iface.getMethod("equals", Object.class);
						b = true;
					}catch(NoSuchMethodException e2){
					}
					if (b) break;
				}
				if (b){
					for(Class<?> iface : declarHashCodeClass.getInterfaces()){
						try{
							iface.getMethod("hashCode");
							return true;
						}catch(NoSuchMethodException e2){
						}
					}
				}
				return false;
			}
		}catch(NoSuchMethodException | SecurityException e){
			throw new RuntimeException(e);
		}
	}
	/**
	 * 親クラス探索Stream.
	 * 自クラスを含めて親クラスまで（Object.classの前まで）のStreamを返す
	 * @param cls 起点クラス
	 * @return 自クラスを含めて親クラスまでのStream
	 * @since 4.29
	 */
	public static Stream<Class<?>> superStream(Class<?> cls){
		UnaryOperator<Class<?>> superFind = c->c.getSuperclass();
		Builder<Class<?>> builder = Stream.builder();
		builder.add(cls);
		Class<?> c = cls;
		while(!(c=superFind.apply(c)).equals(Object.class)) {
			builder.add(c);
		}
		return builder.build();
	}
}
