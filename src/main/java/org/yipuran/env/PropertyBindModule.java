package org.yipuran.env;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.MembersInjector;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

/**
 * プロパティ → Properties 変数へのバインド定義.
 * <pre>
 * Properties変数で、@Inject @PropertyInject が付いてるものを
 * コンストラクタで指定するリソース Key.properties を読込みインジェクトする。
 *
 * ＠PropertyInject を使用するのが約束で、＠PropertyInject の value 有無で
 *
 *  value 無し→ PropertyBindModule コンストラクタで指定する Properties だけ Properties を読み込める
 *  value 有り→ PropertyBindModule コンストラクタ指定に関係なく、＠PropertyInject の valueが指定する Properties を読み込める
 *
 *  ＠Inject ＠PropertyInject private Properties prop;
 *   に対して、
 *   Injector injector = Guice.createInjector(new PropertyBindModule("aaa"));
 *   とすることで、aaa.properties を読んでPropertiesにインジェクトする。
 *   propertiesファイルが見つからない場合、空のPropertiesがインジェクトされる。
 *
 *   コンストラクタ引数有無に関わらず、PropertyInject アノテーションでリソースキーを指定すると、
 *   PropertyInject で指定したリソースキーが優先される
 *        ＠Inject ＠PropertyInject("aaa") private Properties prop;
 *      → new PropertyBindModule() 実行、new PropertyBindModule("bbb") 実行でも
 *      aaa.properties を読込もうとする。
 * </pre>
 */
public class PropertyBindModule extends AbstractModule{
	String resourceKey;
	/**
	 * デフォルトコンストラクタ.
	 * <br>PropertyInject アノテーションでリソースキーを指定する必要がある
	 */
	public PropertyBindModule(){}
	public PropertyBindModule(String resourceKey){
	   this.resourceKey = resourceKey;
	}
	/* (非 Javadoc)
	 * @see com.google.inject.AbstractModule#configure()
	 */
	@Override
	protected void configure(){
		bindListener(Matchers.any(), new TypeListener(){
			@Override
			public <T> void hear(TypeLiteral<T> typeLiteral,TypeEncounter<T> typeEncounter){
				for(Field field : typeLiteral.getRawType().getDeclaredFields()){
					if (field.getType()==Properties.class && field.isAnnotationPresent(PropertyInject.class)){
						// Properties変数で、@PropertyInject が付いてるもの
						typeEncounter.register(new _PropertyInjector<T>(field));
					}
				}
			}
			class _PropertyInjector<T> implements MembersInjector<T>{
				private final Field field;
				_PropertyInjector(Field field){
					this.field = field;
					this.field.setAccessible(true);
				}
				/*
				 * @see com.google.inject.MembersInjector#injectMembers(java.lang.Object)
				 */
				@Override
				public void injectMembers(T t){
					try{
						Properties p = new Properties();
						String value_PropertyInject = this.field.getAnnotation(PropertyInject.class).value();
						String resourcekey = value_PropertyInject.length()==0
										? PropertyBindModule.this.resourceKey
										: value_PropertyInject;
						if (resourcekey != null && resourcekey.length() > 0){
							ResourceBundle rs = ResourceBundle.getBundle(resourcekey, new ResourceBundle.Control(){
								@Override
								public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
								throws IllegalAccessException, InstantiationException, IOException{
									String bundleName = toBundleName(baseName, locale);
									String resourceName = toResourceName(bundleName, "properties");
									try(InputStreamReader sr = new InputStreamReader(loader.getResourceAsStream(resourceName), "UTF-8");
											BufferedReader reader = new BufferedReader(sr)){
										return new PropertyResourceBundle(reader);
									}
								}
							});
							for(String key : rs.keySet()){
								p.setProperty(key,rs.getString(key));
							}
						}
						this.field.set(t,p);
					}catch (MissingResourceException e){
						LoggerFactory.getLogger(this.getClass()).warn("## MissingResourceException : "+e.getMessage(),e);
					}catch(IllegalAccessException e){
						throw new RuntimeException(e);
					}
				}
			}
		});
	}
}
