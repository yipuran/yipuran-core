package org.yipuran.util;

import java.util.Arrays;
import java.util.Optional;

/**
 * Generic enum interface.
 */
public interface EnumBase<E extends Enum<E>> {
	/**
	 * コード値を返却.
	 * @return コード値
	 */
	public Object getValue();

	static <E> E[] getArray(Class<E> clz) {
		return clz.getEnumConstants();
	}

	@SuppressWarnings("unchecked")
	static <E extends Enum<E>> E valueOf(Class<? extends  Enum<E>> cls, String value) {
		return (E)Arrays.stream(cls.getEnumConstants())
				.filter(e->e.name().equals(value))
				.findAny()
				.orElse(null);
	}

	@SuppressWarnings("unchecked")
	static <E extends Enum<E>> Optional<E> parseOf(Class<? extends  Enum<E>> cls, String value) {
		return (Optional<E>) Arrays.stream(cls.getEnumConstants())
				.filter(e->e.name().equals(value))
				.findAny();
	}

	@SuppressWarnings("unchecked")
	static <E extends Enum<E>> Optional<E> parseCode(Class<? extends  EnumBase<E>> cls, Object code) {
		return (Optional<E>) Arrays.stream(cls.getEnumConstants())
				.filter(e->e.getValue().equals(code))
				.findAny();
	}

	@SuppressWarnings("unchecked")
	static <E extends Enum<E>> E codeOf(Class<? extends  EnumBase<E>> cls, Object code) {
		return (E) Arrays.stream(cls.getEnumConstants())
				.filter(e->e.getValue().equals(code))
				.findAny().orElse(null);
	}
}
