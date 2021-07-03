package org.yipuran.util;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import org.yipuran.function.ThrowableBiConsumer;
import org.yipuran.function.ThrowableSupplier;

/**
 * Snake case ⇔ Camel case 変換インターフェース.
 * <PRE>
 * 本インターフェースを実装するクラスのインスタンスは、
 * Camel caseで記述された属性全てに対応する Snake case で記述された属性を同じ型で持つクラスインスタンスを
 * 属性値コピー生成する。
 * また逆のケース、
 * Snake caseで記述された属性全てに対応する Camel case で記述された属性を同じ型で持つクラスインスタンスを
 * 属性値コピー生成する。
 * インターフェース実装と無関係に、
 * 　　Camel case → Snake case 属性コピー生成
 * 　　Snake case → Camel case 属性コピー生成
 * の static メソッドを提供する。
 *
 * 文字列の変換、文字列を対象にした static メソッドを提供するが、
 * Camel case → Snake case は、単純に "_" を挿入するだけであり大文字小文字は調整しない。
 * </PRE>
 * @since 4.27
 */
public interface SnakeCamel{
	/**
	 * Snake case 属性のインスタンス生成.
	 * @param sup インスタンス生成Supplier
	 * @return Supplierで生成しCamel case → Snake case 属性コピー生成の結果
	 */
	default <R> R toSnake(Supplier<R> sup){
		GenericBuilder<R> builder = Arrays.stream(this.getClass().getDeclaredFields())
		.collect(ThrowableSupplier.to(()->GenericBuilder.of(sup))
			, ThrowableBiConsumer.of((r, f)->{
				f.setAccessible(true);
				String snake = f.getName().replaceAll("([a-z0-9]+)([A-Z]+)", "$1_$2").toLowerCase();
				r.with(Fieldsetter.of((p, u)->snake), f.get(this));
		}), (r, s)->{});
		return builder.build();
	}
	/**
	 * Snake case 属性のインスタンス生成.
	 * @param sup インスタンス生成Supplier
	 * @return Supplierで生成しSnake case → Camel case 属性コピー生成の結果
	 */
	default <R> R toCamel(Supplier<R> sup){
		GenericBuilder<R> builder = Arrays.stream(this.getClass().getDeclaredFields())
		.collect(ThrowableSupplier.to(()->GenericBuilder.of(sup))
			, ThrowableBiConsumer.of((r, f)->{
				f.setAccessible(true);
				String camel = Optional.of(Pattern.compile("_").splitAsStream(f.getName().toLowerCase()).filter(e->e.length() > 0)
				.collect(StringBuilder::new, (rr, tt)->rr.append(tt.substring(0, 1).toUpperCase()).append(tt.substring(1))
				, (v, w)->{}).toString()).filter(e->e.length() > 1)
				.map(e->e.substring(0, 1).toLowerCase() + e.substring(1)).get();
			r.with(Fieldsetter.of((p, u)->camel), f.get(this));
		}), (r, u)->{});
		return builder.build();
	}
}
