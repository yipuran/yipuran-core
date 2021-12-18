package org.yipuran.util;

import java.lang.reflect.Field;

import sun.misc.Unsafe;
/**
 * Java9 以降の Field reflection の警告を黙らせる.
 */
public final class ReflectIllegalaccess{
	/** private constructor. */
	private ReflectIllegalaccess(){}

	/**
	 * Java9 以降の Field reflection の警告を黙らせる.
	 * Field access security Log jdk.internal.module.IllegalAccessLogger waring quiet.
	 */
	@SuppressWarnings("restriction")
	public static void quiet(){
		try{
			Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
			theUnsafe.setAccessible(true);
			Unsafe u = (Unsafe)theUnsafe.get(null);
			Class<?> cls = Class.forName("jdk.internal.module.IllegalAccessLogger");
			Field logger = cls.getDeclaredField("logger");
			u.putObjectVolatile(cls, u.staticFieldOffset(logger), null);
		}catch(Exception e){
		}
	}
}
