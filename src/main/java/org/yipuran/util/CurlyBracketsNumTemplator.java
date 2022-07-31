package org.yipuran.util;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Curly brackets Number Templator.
 * <PRE>
 *
 * String tempate = "abc {0} def {1} 123 {0} {2}";
 *
 * CurlyBracketsNumTemplator temp = ()->template;
 * String str = temp.replace("A", "B", "C");
 * // "abc A def B 123 A C"
 *
 * String str = CurlyBracketsNumTemplator.replace(tempate, "A", "B", "C");
 * // "abc A def B 123 A C"
 *
 * </PFE>
 */
@FunctionalInterface
public interface CurlyBracketsNumTemplator {
	String get();

	default String replace(Object...reps) {
		return replace(get(), reps);
	}

	public static String replace(String template, Object...reps) {
		Objects.requireNonNull(template);
		String s = template;
		Matcher m = Pattern.compile("\\{\\d+\\}").matcher(template);
		while(m.find()){
			int n = Integer.parseInt(m.group().substring(1, m.group().length()-1));
			if (n < reps.length) {
				s = s.replace(m.group(), reps[n].toString());
			}
		}
		return s;
	}
}
