package database.entities;

import util.Pair;
import util.Try;

interface Buildable<T> {
    Try<T> build(final Pair<Long, Iterable<Pair<String, String>>> context);
}
