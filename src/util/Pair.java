package util;

import java.io.Serializable;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import datameer.com.google.common.base.Function;
import datameer.com.google.common.base.MoreObjects;
import datameer.com.google.common.base.Optional;
import datameer.com.google.common.base.Preconditions;
import datameer.com.google.common.base.Predicate;
import datameer.com.google.common.collect.Iterables;
import datameer.com.google.common.collect.Maps;

/**
 * Represents a pair or tuple with two fields.
 * 
 * @param <A>
 *            type of field <code>_1</code>
 * @param <B>
 *            type of field <code>_2</code>
 */
public final class Pair<A, B> implements Serializable {

	private static final long serialVersionUID = -6007275674879799211L;
	public final A _1;
    public final B _2;

    private Pair(final A a, final B b) {
        _1 = a;
        _2 = b;
    }

    public static <A> Function<Pair<A, ?>, A> left() {
        return new Function<Pair<A, ?>, A>() {
            @Override
            public A apply(final Pair<A, ?> input) {
                return input._1;
            }
        };
    }

    public static <A> Function<Pair<A, ?>, A> left(final Class<A> returnType) {
        return left();
    }

    public static <A extends Comparable<A>> Comparator<Pair<A, ?>> leftComparator() {
        return new Comparator<Pair<A, ?>>() {
            @Override
            public int compare(final Pair<A, ?> o1, final Pair<A, ?> o2) {
                return o1._1.compareTo(o2._1);
            }
        };
    }

    public static <A extends Comparable<A>> Comparator<Pair<A, ?>> leftComparator(final Class<A> returnType) {
        return leftComparator();
    }

    public static <A, B> Pair<A, B> of(final A a, final B b) {
        Preconditions.checkNotNull(a, "Argument 'a' must not be null");
        Preconditions.checkNotNull(b, "Argument 'b' must not be null");
        return new Pair<A, B>(a, b);
    }

    public static <B> Function<Pair<?, B>, B> right() {
        return new Function<Pair<?, B>, B>() {
            @Override
            public B apply(final Pair<?, B> input) {
                return input._2;
            }
        };
    }

    public static <B> Function<Pair<?, B>, B> right(final Class<B> returnType) {
        return right();
    }

    public static <B extends Comparable<B>> Comparator<Pair<?, B>> rightComparator() {
        return new Comparator<Pair<?, B>>() {
            @Override
            public int compare(final Pair<?, B> o1, final Pair<?, B> o2) {
                return o1._2.compareTo(o2._2);
            }
        };
    }

    public static <B extends Comparable<B>> Comparator<Pair<?, B>> rightComparator(final Class<B> returnType) {
        return rightComparator();
    }

    /**
     * Creates a pair with potential <code>null</code> references.
     * <p>
     * Using <code>null</code> references is in most cases bad practice and {@link Optional} can be
     * used to transport the meaning of "something is not there".
     * 
     * @param a
     * @param b
     * @return a new instance of <code>Pair</code>
     */
    @Deprecated
    public static <A, B> Pair<A, B> withNullRefs(final A a, final B b) {
        return new Pair<A, B>(a, b);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        @SuppressWarnings("rawtypes")
        Pair other = (Pair) obj;
        return Objects.equals(_1, other._1) && Objects.equals(_2, other._2);
    }

    @Override
    public int hashCode() {
        Object[] objects = { _1, _2 };
        return Objects.hash(objects);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(getClass()).add("_1", _1).add("_2", _2).toString();
    }

    /**
     * Returns an {@link Optional} containing the first element in <code>pairs</code> that satisfies
     * the given firstField
     * 
     * @param firstField
     *            the firstField to find the value
     * @param pairs
     *            an iterable of pairs
     * @return the second field of the pair
     */
    public static final Optional<String> tryFind(final Iterable<Pair<String, String>> pairs, final String firstField) {
        Preconditions.checkNotNull(pairs, "The argument 'pairs' must not be null.");
        Preconditions.checkNotNull(firstField, "The argument 'firstField' must not be null.");

        Optional<Pair<String, String>> propertyPair = Iterables.tryFind(pairs, new Predicate<Pair<String, String>>() {
            @Override
            public boolean apply(Pair<String, String> input) {
                return input._1.equals(firstField);
            }
        });
        return propertyPair.isPresent() ? Optional.of(propertyPair.get()._2) : Optional.<String> absent();
    }

    /**
     * Aggregates all pairs into a {@link Map}.
     * 
     * @param pairs
     *            key value pairs
     * @return map of aggregated pairs
     */
    public static <K, V> Map<K, V> aggregateToMap(Iterable<Pair<K, V>> pairs) {
        LinkedHashMap<K, V> newMap = Maps.newLinkedHashMap();
        for (Pair<K, V> pair : pairs) {
            newMap.put(pair._1, pair._2);
        }
        return newMap;
    }

    /**
     * Returns a {@link datameer.com.google.common.base.Function} to convert a
     * {@link java.util.Map.Entry} to a {@link datameer.dap.sdk.util.Pair}.
     * 
     * @return a function to convert a given entry to a pair
     */
    public static <K, V> Function<Entry<K, V>, Pair<K, V>> entryToPair() {
        return new Function<Entry<K, V>, Pair<K, V>>() {
            @Override
            public Pair<K, V> apply(Entry<K, V> input) {
                return Pair.of(input.getKey(), input.getValue());
            }
        };
    }

    /**
     * An simulated type alias to improve readability. The downside is that we introduce a new type
     * which limits the composability and reusability.
     * 
     * @param <K>
     *            type of field <code>_1</code>
     * @param <V>
     *            type of field <code>_2</code>
     */
    public static interface PairFunction<K, V> extends Function<Pair<K, V>, Pair<K, V>> {
    }
}
