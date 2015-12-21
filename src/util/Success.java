package util;

import static datameer.com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nullable;

import datameer.com.google.common.base.Function;
import datameer.com.google.common.base.VerifyException;

/**
 * Implementation of an {@link Try} containing a reference.
 *
 * @param <T>
 *            type of the containing reference
 */
final class Success<T> extends Try<T> {

	private static final long serialVersionUID = 4040164202958484955L;
	private final T _reference;

    Success(final T reference) {
        _reference = reference;
    }

    @Override
    public <B> Try<B> castIfFailure(Class<B> type) {
        throw new IllegalStateException(String.format("Cannot convert an instance of Success to Try<%s>", type.getSimpleName()));
    }

    @Override
    public boolean equals(@Nullable final Object object) {
        if (object instanceof Success) {
            Success<?> other = (Success<?>) object;
            return _reference.equals(other._reference);
        }
        return false;
    }

    @Override
    public Throwable failure() {
        throw new IllegalStateException("throwables instance absent");
    }

    @Override
    public <V> Try<V> flatMap(final Function<? super T, Try<V>> function) {
        checkNotNull(function, "Argument 'function' must not be null");
        try {
            return function.apply(_reference);
        } catch (Throwable t) {
            return new Failure<V>(t);
        }
    }

    @Override
    public T get() {
        return _reference;
    }

    @Override
    public int hashCode() {
        return 0x598df91c + _reference.hashCode();
    }

    @Override
    public boolean isFailure() {
        return false;
    }

    @Override
    public boolean isSuccess() {
        return true;
    }

    @Override
    public <V> Try<V> map(final Function<? super T, ? extends V> function) {
        checkNotNull(function, "Argument 'function' must not be null");
        try {
            return new Success<V>(function.apply(_reference));
        } catch (Throwable t) {
            return new Failure<V>(t);
        }
    }

    @Override
    public T or(final T defaultValue) {
        checkNotNull(defaultValue, "use Try.orNull() instead of Try.or(null)");
        return _reference;
    }

    @Override
    public Try<T> or(final Try<? extends T> secondChoice) {
        checkNotNull(secondChoice);
        return this;
    }

    @Override
    public RuntimeException propagate() {
        throw new VerifyException("Cannot propagate a throwable, it is an instance of Success");
    }

    @Override
    public String toString() {
        return "Try.of(" + _reference + ")";
    }
}
