package org.yipuran.env;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;

/**
 * 環境変数をセットするユーティリティ.
 *
 * Enviroment.ignoreJava9Warning(); を実行した後で、Enviroment.serEnv(String key, String value)
 * Enviroment.setEnv(Map<String, String> envmap); を実行すべき。
 */
public final class Enviroment {
	private Enviroment(){}

	/**
	 * １つの環境変数をセット
	 * @param key 環境変数キー
	 * @param value 値
	 */
	@SuppressWarnings("unchecked")
	public static void setEnv(String key, String value) {
		try{
			Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
			Field theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
			theEnvironmentField.setAccessible(true);
			Map<String, String> env = (Map<String, String>)theEnvironmentField.get(null);
			env.put(key, value);
			Field theCaseInsensitiveEnvironmentField = processEnvironmentClass.getDeclaredField("theCaseInsensitiveEnvironment");
			theCaseInsensitiveEnvironmentField.setAccessible(true);
			Map<String, String> cienv = (Map<String, String>) theCaseInsensitiveEnvironmentField.get(null);
			cienv.put(key, value);
		}catch(Exception e){
			try{
				Class<?>[] classes = Collections.class.getDeclaredClasses();
				Map<String, String> env = System.getenv();
				for(Class<?> cl : classes){
					if ("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
						Field field = cl.getDeclaredField("m");
						field.setAccessible(true);
						Object obj = field.get(env);
						Map<String, String> map = (Map<String, String>)obj;
						map.put(key, value);
					}
				}
			}catch(Exception x){
				x.printStackTrace();
			}
		}
	}
	/**
	 * マップで環境変数をセット.
	 * @param envmap key=環境変数キー、value=値 のマップ
	 */
	@SuppressWarnings("unchecked")
	public static void setEnv(Map<String, String> envmap) {
		try{
			Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
			Field theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
			theEnvironmentField.setAccessible(true);
			Map<String, String> env = (Map<String, String>)theEnvironmentField.get(null);
			env.putAll(envmap);
			Field theCaseInsensitiveEnvironmentField = processEnvironmentClass.getDeclaredField("theCaseInsensitiveEnvironment");
			theCaseInsensitiveEnvironmentField.setAccessible(true);
			Map<String, String> cienv = (Map<String, String>) theCaseInsensitiveEnvironmentField.get(null);
			cienv.putAll(envmap);
		}catch(Exception e){
			try{
				Class<?>[] classes = Collections.class.getDeclaredClasses();
				Map<String, String> env = System.getenv();
				for(Class<?> cl : classes){
					if ("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
						Field field = cl.getDeclaredField("m");
						field.setAccessible(true);
						Object obj = field.get(env);
						Map<String, String> map = (Map<String, String>) obj;
						map.putAll(envmap);
					}
				}
			}catch(Exception x){
				x.printStackTrace();
			}
		}
	}
	/**
	 * WARNING: All illegal access operations  警告メッセージを抑制 for Java9
	 */
	@SuppressWarnings("restriction")
	public static void ignoreJava9Warning(){
		try{
			Field theUnsafe = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
			theUnsafe.setAccessible(true);
			sun.misc.Unsafe u = (sun.misc.Unsafe) theUnsafe.get(null);
			Class<?> cls = Class.forName("jdk.internal.module.IllegalAccessLogger");
			Field logger = cls.getDeclaredField("logger");
			u.putObjectVolatile(cls, u.staticFieldOffset(logger), null);
		}catch(Exception e){
			// Java8 Exception
		}
	}
}
