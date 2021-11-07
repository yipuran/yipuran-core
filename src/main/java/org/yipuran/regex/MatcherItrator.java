package org.yipuran.regex;

import java.util.Spliterators.AbstractSpliterator;
import java.util.function.Consumer;
import java.util.regex.Matcher;

/**
 * MatcherItrator.
 *
 * <PRE>
 * (Example)
 *
 * String string = " abc_def_ghi_..";
 * StreamSupport.stream(new MatcherItrator(Pattern.compile("[a-z]+").matcher(string)), false)
 * .forEach(e->{
 *   System.out.println(e);
 * });
 *
 * </PRE>
 */
public final class MatcherItrator extends AbstractSpliterator<CharSequence>{
	private final Matcher matcher;

	public MatcherItrator(Matcher m){
		super(m.regionEnd() - m.regionStart(), ORDERED|NONNULL);
		matcher = m;
	}
	@Override
	public boolean tryAdvance(Consumer<? super CharSequence> c){
		if (!matcher.find()) return false;
		c.accept(matcher.group());
		return true;
	}
}
