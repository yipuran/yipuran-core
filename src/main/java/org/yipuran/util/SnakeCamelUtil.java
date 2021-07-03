package org.yipuran.util;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import org.yipuran.function.ThrowableBiConsumer;
import org.yipuran.function.ThrowableSupplier;

/**
 * Snake case ⇔ Camel case 変換ユーティリティ.
 * @since 4.27
 */
public final class SnakeCamelUtil{
	private SnakeCamelUtil(){}
	/**
	 * Camel case属性のインスタンス → Snake case属性のインスタンス生成.
	 * @param t Camel case属性のインスタンス
	 * @param sup Snake case属性のインスタンス生成Supplier
	 * @return Snake case属性のインスタンス
	 */
	public static <R, T> R toSnake(T t, Supplier<R> sup){
		GenericBuilder<R> builder = Arrays.stream(t.getClass().getDeclaredFields())
		.collect(ThrowableSupplier.to(()->GenericBuilder.of(sup))
			, ThrowableBiConsumer.of((r, f)->{
				f.setAccessible(true);
				String snake = f.getName().replaceAll("([a-z0-9]+)([A-Z]+)", "$1_$2").toLowerCase();
				r.with(Fieldsetter.of((p, u)->snake), f.get(t));
		}), (r, s)->{});
		return builder.build();
	}
	/**
	 * Snake case属性のインスタンス → Camel case属性のインスタンス生成.
	 * @param t Snake case属性のインスタンス
	 * @param sup Camel case属性のインスタンス生成Supplier
	 * @return Camel case属性のインスタンス
	 */
	public static <R, T> R toCamel(T t, Supplier<R> sup){
		GenericBuilder<R> builder = Arrays.stream(t.getClass().getDeclaredFields())
		.collect(ThrowableSupplier.to(()->GenericBuilder.of(sup))
			, ThrowableBiConsumer.of((r, f)->{
				f.setAccessible(true);
				String camel = Optional.of(Pattern.compile("_").splitAsStream(f.getName().toLowerCase()).filter(e->e.length() > 0)
				.collect(StringBuilder::new, (rr, tt)->rr.append(tt.substring(0, 1).toUpperCase()).append(tt.substring(1))
				, (v, w)->{}).toString()).filter(e->e.length() > 1)
				.map(e->e.substring(0, 1).toLowerCase() + e.substring(1)).get();
			r.with(Fieldsetter.of((p, u)->camel), f.get(t));
		}), (r, u)->{});
		return builder.build();
	}
	/**
	 * Camel case文字列 → Snake case文字列変換.
	 * @param camelstr Camel case文字列
	 * @return Snake case文字列
	 *
	 */
	public static String toSnake(String camelstr){
		return camelstr.replaceAll("([a-z0-9]+)([A-Z]+)", "$1_$2");
	}
	/**
	 * Snake case文字列 → Camel case文字列変換.
	 * @param snakestr Snake case文字列
	 * @return Camel case文字列
	 */
	public static String toCamel(String snakestr){
		return Optional.of(Pattern.compile("_").splitAsStream(snakestr.toLowerCase())
			.filter(e->e.length() > 0).collect(StringBuilder::new
			, (r, t)->r.append(t.substring(0, 1).toUpperCase()).append(t.substring(1))
			, (r, t)->{}).toString())
			.map(e->e.substring(0, 1).toLowerCase() + e.substring(1)).get();
	}
}
