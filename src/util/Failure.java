package util;

import static datameer.com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nullable;

import datameer.com.google.common.base.Function;
import datameer.com.google.common.base.Throwables;

/**
 * Implementation of an {@link Try} containing a {@link Throwable}.
 *
 * @param <T>
 *            type of the containing reference
 */
final class Failure<T> extends Try<T> {

	private static final long serialVersionUID = 3881183015124055280L;
	private final Throwable _throwable;

    Failure(final Throwable t) {
        _throwable = checkNotNull(t);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <B> Try<B> castIfFailure(final Class<B> type) {
        return (Try<B>) this;
    }

    @Override
    public boolean equals(@Nullable final Object object) {
        if (object instanceof Failure) {
            Failure<?> other = (Failure<?>) object;
            return _throwable.equals(other._throwable);
        }
        return false;
    }

    @Override
    public Throwable failure() {
        return _throwable;
    }

    @Override
    public <V> Try<V> flatMap(final Function<? super T, Try<V>> function) {
        checkNotNull(function);
        return new Failure<V>(_throwable);
    }

    @Override
    public T get() {
        throw Throwables.propagate(_throwable);
    }

    @Override
    public int hashCode() {
        return 0x598df91c + _throwable.hashCode();
    }

    @Override
    public boolean isFailure() {
        return true;
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public <V> Try<V> map(final Function<? super T, ? extends V> function) {
        checkNotNull(function);
        return new Failure<V>(_throwable);
    }

    @Override
    public T or(final T defaultValue) {
        checkNotNull(defaultValue, "Argument 'defaultValue' must not be null");
        return defaultValue;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Try<T> or(final Try<? extends T> secondChoice) {
        // safe covariant cast
        return (Try<T>) checkNotNull(secondChoice);
    }

    @Override
    public RuntimeException propagate() {
        return Throwables.propagate(_throwable);
    }

    @Override
    public String toString() {
        return "Try.failure(" + _throwable + ")";
    }
}
