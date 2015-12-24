package database.entities;

import static database.DatabaseOperations.createDatabaseIfNotExists;
import static database.DatabaseOperations.databaseExists;
import util.Pair;
import util.Try;
import util.Unit;
import database.DatabaseOperations;
import datameer.com.google.common.base.Function;
import datameer.com.google.common.base.Optional;
import datameer.com.google.common.base.Supplier;
import datameer.com.google.common.collect.FluentIterable;
import datameer.com.google.common.collect.ImmutableList;

abstract class Entity {

    private final Optional<Long> _entityId;
    private final String _tableName;

    Entity(final Long entityId, final String tableName) {
	_entityId = Optional.of(entityId);
	_tableName = tableName;
    }

    Entity(final String tableName) {
	_entityId = Optional.absent();
	_tableName = tableName;
    }

    private static final Function<Unit, Try<Unit>> createTableIfNotExists(final Iterable<Pair<String, String>> persistenceContext) {
	return new Function<Unit, Try<Unit>>() {
	    @Override
	    public Try<Unit> apply(final Unit input) {
		return DatabaseOperations.createTableIfNotExists(persistenceContext);
	    }
	};
    }

    public static Try<Unit> delete(final long entityId, final String tableName) {
	return Try.of(new Supplier<Unit>() {
	    @Override
	    public Unit get() {
		Try<Unit> delete = databaseExists()
			//
			.flatMap(tableExists(tableName))

			.flatMap(deleteEntity(entityId, tableName));
		if (delete.isFailure()) {
		    System.err.println(String.format("Could not delete Entity with Id '%s' from table '%s', reason:", "" + entityId, tableName));
		    System.err.println(delete.propagate().getMessage());
		    throw delete.propagate();
		}
		System.err.println(String.format("Successfully deleted Entity with id '%s' from table '%s'", "" + entityId, tableName));
		return delete.get();
	    }
	});
    }

    private static final Function<Boolean, Try<Unit>> deleteEntity(final long entityId, final String tableName) {
	return new Function<Boolean, Try<Unit>>() {
	    @Override
	    public Try<Unit> apply(final Boolean tableExists) {
		if (!tableExists) {
		    System.err.println(String.format("Table '%s' does not exist", tableName));
		    throw new IllegalStateException(String.format("Table '%s' does not exist", tableName));
		}
		return DatabaseOperations.delete(entityId, tableName);
	    }
	};
    }

    static final Try<Entity> loadByParameter(final String parameter, final String value) {
	return null;
    }

    static final Try<Pair<Long, Iterable<Pair<String, String>>>> loadContext(final long entityId, final String tableName, final ImmutableList<String> keys) {
	return Try.of(new Supplier<Pair<Long, Iterable<Pair<String, String>>>>() {
	    @Override
	    public Pair<Long, Iterable<Pair<String, String>>> get() {
		Try<Iterable<Pair<String, String>>> persistenceContext = databaseExists()
			//
			.flatMap(tableExists(tableName))

			.flatMap(loadPersistenceContext(entityId, tableName, keys));
		if (persistenceContext.isFailure()) {
		    System.err.println(String.format("Could not load Entity with Id '%s' from table '%s'", "" + entityId, tableName));
		    throw persistenceContext.propagate();
		}
		return Pair.of(entityId, persistenceContext.get());
	    }
	});
    }

    private static final Function<Boolean, Try<Iterable<Pair<String, String>>>> loadPersistenceContext(final long entityId, final String tableName,
	    final ImmutableList<String> keys) {
	return new Function<Boolean, Try<Iterable<Pair<String, String>>>>() {
	    @Override
	    public Try<Iterable<Pair<String, String>>> apply(final Boolean tableExists) {
		if (!tableExists) {
		    System.err.println(String.format("Table '%s' does not exist", tableName));
		    throw new IllegalStateException(String.format("Table '%s' does not exist", tableName));
		}
		return DatabaseOperations.loadPersistenceContext(entityId, tableName, keys);
	    }
	};
    }

    private static Supplier<Long> save(final Iterable<Pair<String, String>> persistenceContext, final Optional<Long> entityId) {
	return new Supplier<Long>() {
	    @Override
	    public Long get() {
		Try<Long> save = createDatabaseIfNotExists()
		//
			.flatMap(createTableIfNotExists(persistenceContext))

			.flatMap(saveOrUpdate(persistenceContext, entityId));
		if (!save.isSuccess()) {
		    throw save.propagate();
		}
		return save.get();
	    }
	};
    }

    private static final Function<Unit, Try<Long>> saveOrUpdate(final Iterable<Pair<String, String>> persistenceContext, final Optional<Long> entityId) {
	return new Function<Unit, Try<Long>>() {
	    @Override
	    public Try<Long> apply(final Unit input) {
		return DatabaseOperations.saveOrUpdate(FluentIterable.from(persistenceContext), entityId);
	    }
	};
    }

    private static final Function<Boolean, Try<Boolean>> tableExists(final String tableName) {
	return new Function<Boolean, Try<Boolean>>() {
	    @Override
	    public Try<Boolean> apply(final Boolean databaseExists) {
		if (!databaseExists) {
		    System.err.println("Database does not exist.");
		    throw new IllegalStateException("Database does not exist. Cannot load Entity");
		}
		return DatabaseOperations.tableExists(tableName);
	    }
	};
    }

    public Try<Unit> delete() {
	if (getEntityId().isPresent()) {
	    return delete(getEntityId().get(), getTableName());
	} else {
	    String errorString = String.format("Could not delete entity from table '%s', is the entity even persisted?", getTableName());
	    System.err.println(errorString);
	    return Try.failure(new IllegalStateException(errorString));
	}
    }

    public Optional<Long> getEntityId() {
	return _entityId;
    }

    public String getTableName() {
	return _tableName;
    }

    /**
     * @return context of the parameters that should be persisted
     */
    public abstract Iterable<Pair<String, String>> persistenceContext();

    public Try<Long> save() {
	return Try.of(save(persistenceContext(), _entityId));
    }

    static final <T> Optional<T> loadFromTemplate(final long entityId, final Buildable<T> template, final String tableName, final ImmutableList<String> keys) {
	Try<T> entity = loadContext(entityId, tableName, keys).flatMap(buildEntity(entityId, template, tableName));
	if (entity.isFailure()) {
	    System.err.println(String.format("could not load Entity with id '%s', from table '%s', reason:", "" + entityId, tableName));
	    System.err.println(entity.failure().getLocalizedMessage());
	    return Optional.absent();
	}
	return Optional.of(entity.get());
    }

    static final <T> Function<Pair<Long, Iterable<Pair<String, String>>>, Try<T>> buildEntity(final long entityId, final Buildable<T> template,
	    final String tableName) {
	return new Function<Pair<Long, Iterable<Pair<String, String>>>, Try<T>>() {
	    @Override
	    public Try<T> apply(Pair<Long, Iterable<Pair<String, String>>> input) {
		if (FluentIterable.from(input._2).isEmpty()) {
		    String error = String.format("could not find Entity with id '%s', in table '%s', is it even persisted?", "" + entityId, tableName);
		    System.err.println(error);
		    return Try.failure(new IllegalStateException(error));
		}
		return template.build(input);
	    }
	};
    }
}
