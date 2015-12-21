package util;

import static datameer.com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import java.util.Iterator;

import javax.annotation.Nullable;

import datameer.com.google.common.base.Function;
import datameer.com.google.common.base.Supplier;
import datameer.com.google.common.collect.AbstractIterator;

/**
 * An immutable object that may contain a non-null reference to another object. Each instance of
 * this type either contains a non-null reference, or contains a failure (in which case we say that
 * the reference is "failure"); it is never said to "contain {@code null}".
 *
 * @param <T>
 *            type of containing reference
 */
public abstract class Try<T> implements Serializable {
    private static final long serialVersionUID = 0;

    Try() {
    }

    /**
     * Returns an {@code Try} instance with no contained reference.
     *
     * @param t
     *            {@link Throwable} which represents the failure content
     * @return new failure instance
     */
    public static <T> Try<T> failure(final Throwable t) {
        return new Failure<T>(t);
    }

    /**
     * Returns the value of each present instance from the supplied {@code optionals}, in order,
     * skipping over occurrences of {@link Try#failure(Throwable)}. Iterators are unmodifiable and
     * are evaluated lazily.
     *
     * @param tries
     *            {@link Iterable} of {@link Try} instances
     * @return success instances
     */
    public static <T> Iterable<Throwable> failureInstances(final Iterable<? extends Try<? extends T>> tries) {
        checkNotNull(tries);
        return new Iterable<Throwable>() {
            @Override
            public Iterator<Throwable> iterator() {
                return new AbstractIterator<Throwable>() {
                    private final Iterator<? extends Try<? extends T>> iterator = checkNotNull(tries.iterator());

                    @Override
                    protected Throwable computeNext() {
                        while (iterator.hasNext()) {
                            Try<? extends T> t = iterator.next();
                            if (!t.isSuccess()) {
                                return t.failure();
                            }
                        }
                        return endOfData();
                    }
                };
            }
        };
    }

    /**
     * Returns a {@link Success} instance if the supplier returns successfully an instance when
     * calling {@code supplier.get()}. If the supplier throws a {@link Throwable} when accessing
     * {@code supplier.get()} a {@link Failure} instance with the corresponding {@link Throwable}
     * will be returned.
     * <p>
     * If the supplier returns {@code null}, a {@link NullPointerException} is thrown.
     *
     * @param supplier
     *            side-effect operation to simulate call-by-name behavior
     * @throws NullPointerException
     *             if the supplier returns {@code null}
     * @return new instance
     */
    @SuppressWarnings("unchecked")
    public static <T> Try<T> of(final Supplier<? extends T> supplier) {
        checkNotNull(supplier);
        try {
            // safe covariant cast
            return (Try<T>) Try.of(supplier.get());
        } catch (Throwable t) {
            return new Failure<T>(t);
        }
    }

    /**
     * Returns an {@code Try} instance containing the given reference.
     *
     * @param reference
     * @return new instance
     */
    public static <T> Try<T> of(@Nullable final T reference) {
        return new Success<T>(reference);
    }

    /**
     * Returns the value of each present instance from the supplied {@code optionals}, in order,
     * skipping over occurrences of {@link Try#failure(Throwable)}. Iterators are unmodifiable and
     * are evaluated lazily.
     *
     * @param tries
     *            {@link Iterable} of {@link Try} instances
     * @return success instances
     */
    public static <T> Iterable<T> successInstances(final Iterable<? extends Try<? extends T>> tries) {
        checkNotNull(tries);
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return new AbstractIterator<T>() {
                    private final Iterator<? extends Try<? extends T>> iterator = checkNotNull(tries.iterator());

                    @Override
                    protected T computeNext() {
                        while (iterator.hasNext()) {
                            Try<? extends T> t = iterator.next();
                            if (t.isSuccess()) {
                                return t.get();
                            }
                        }
                        return endOfData();
                    }
                };
            }
        };
    }

    /**
     * Casts a {@link Failure} instance to be compatible with the given <code>type</code>.
     * 
     * @param type
     * @return this, if is an instance of {@link Failure}
     * @throws IllegalStateException
     *             if this is an instance of {@link Success}
     */
    public abstract <B> Try<B> castIfFailure(Class<B> type);

    /**
     * Returns {@code true} if {@code object} is an {@code Try} instance, and either the contained
     * references are {@linkplain Object#equals equal} to each other or both are absent. Note that
     * {@code Try} instances of differing parameterized types can be equal.
     */
    @Override
    public abstract boolean equals(@Nullable Object object);

    /**
     * Returns the contained {@link Throwable}, which must be present. If the instance might be
     * absent an exception will be thrown.
     *
     * @throws IllegalStateException
     *             if the {@link Throwable} instance is absent ({@link #isSuccess} returns
     *             {@code true})
     * @return containing exception
     */
    public abstract Throwable failure();

    /**
     * If the instance is {@link Success}, it is transformed with the given {@link Function};
     * otherwise, {@link Try#failure(Throwable)} is returned.
     *
     * @param function
     * @return transformed {@link Try}
     */
    public abstract <V> Try<V> flatMap(Function<? super T, Try<V>> function);

    /**
     * Returns the contained instance, which must be present. If the instance might be absent, use
     * {@link #or(Object)} instead.
     *
     * @throws IllegalStateException
     *             if the instance is absent ({@link #isSuccess} returns {@code false})
     * @return containing reference
     */
    public abstract T get();

    /**
     * Returns a hash code for this instance.
     */
    @Override
    public abstract int hashCode();

    /**
     * Return {@code true} if there is a failure, otherwise {@code false}.
     *
     * @return {@code true} if there is a failure, otherwise {@code false}
     */
    public abstract boolean isFailure();

    /**
     * Return {@code true} if there is no failure, otherwise {@code false}.
     *
     * @return {@code true} if there is no failure, otherwise {@code false}
     */
    public abstract boolean isSuccess();

    /**
     * If the instance is {@link Success}, it is transformed with the given {@link Function};
     * otherwise, {@link Try#failure(Throwable)} is returned. If the function returns {@code null},
     * a {@link NullPointerException} is thrown.
     *
     * @param function
     * @throws NullPointerException
     *             if the function returns {@code null}
     * @return transformed {@link Try}
     */
    public abstract <V> Try<V> map(Function<? super T, ? extends V> function);

    /**
     * Returns the contained instance if it is present; {@code defaultValue} otherwise. If no
     * default value should be required because the instance is known to be present, use
     * {@link #get()} instead.
     * <p>
     * Note about generics: The signature {@code public T or(T defaultValue)} is overly restrictive.
     * However, the ideal signature, {@code public <S super T> S or(S)}, is not legal Java. As a
     * result, some sensible operations involving subtypes are compile errors.
     *
     * @param defaultValue
     *            default value
     * @return containing reference or default value
     */
    public abstract T or(T defaultValue);

    /**
     * Returns this {@code Try} if of type {@link Success}; {@code secondChoice} otherwise.
     *
     * @param secondChoice
     *            second choice
     * @return this instance or second choice
     */
    public abstract Try<T> or(Try<? extends T> secondChoice);

    /**
     * Propagates the contained {@code throwable} as-is if it is an instance of
     * {@link RuntimeException} or {@link Error}, or else as a last resort, wraps it in a
     * {@code RuntimeException} then propagates.
     * <p>
     * This method always throws an exception. The {@code RuntimeException} return type is only for
     * client code to make Java type system happy in case a return value is required by the
     * enclosing method.
     *
     * @return nothing will ever be returned; this return type is only for your convenience
     */
    public abstract RuntimeException propagate();

    /**
     * Returns a string representation for this instance. The form of this string representation is
     * unspecified.
     */
    @Override
    public abstract String toString();
}
