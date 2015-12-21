package util;

import datameer.com.google.common.base.Predicate;

public class Predicates {
    public static final Predicate<String> without(final String forbidden) {
	return new Predicate<String>() {
	    @Override
	    public boolean apply(String input) {
		return !input.equals(forbidden);
	    }
	};
    }

    public static final Predicate<Pair<String, String>> withoutSecond(final String forbidden) {
	return new Predicate<Pair<String, String>>() {
	    @Override
	    public boolean apply(Pair<String, String> input) {
		return !input._2.equals(forbidden);
	    }
	};
    }
}
