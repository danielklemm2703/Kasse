package util;

import datameer.com.google.common.base.Function;

public class Functions {

    public static final Function<String, Long> toLong = new Function<String, Long>() {
	@Override
	public Long apply(String input) {
	    return Long.parseLong(input.trim());
	}
    };
}
