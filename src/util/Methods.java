package util;

import datameer.com.google.common.collect.FluentIterable;
import datameer.com.google.common.collect.ImmutableSet;
import datameer.com.google.common.collect.ImmutableSet.Builder;

public class Methods {
    public static final ImmutableSet<Integer> toSet(final int[] array) {
	Builder<Integer> aggregate = ImmutableSet.<Integer> builder();
	for (int selected : array) {
	    aggregate.add(selected);
	}
	return aggregate.build();
    }

    public static final FluentIterable<Integer> toIterable(final int[] array) {
	return FluentIterable.from(toSet(array));
    }
}
