package org.yipuran.util;

import java.util.Arrays;
import java.util.Optional;

public final class EnumBaseTool {

	private EnumBaseTool() {}


	@SuppressWarnings("unchecked")
	public  static  <E extends Enum<E>> Optional<E> parseCode(Class<? extends  EnumBase<E>> cls, Object code) {
		return (Optional<E>) Arrays.stream(cls.getEnumConstants())
				.filter(e->e.getValue().equals(code))
				.findAny();
	}

	@SuppressWarnings("unchecked")
	public static <E extends Enum<E>> E codeOf(Class<? extends  EnumBase<E>> cls, Object code) {
		return (E) Arrays.stream(cls.getEnumConstants())
				.filter(e->e.getValue().equals(code))
				.findAny().orElse(null);
	}

}
