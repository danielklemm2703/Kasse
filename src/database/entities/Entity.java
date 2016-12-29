package database.entities;

import static database.DatabaseOperations.createDatabaseIfNotExists;
import static database.DatabaseOperations.databaseExists;
import util.Pair;
import util.Try;
import util.Unit;
import database.DatabaseOperations;
import database.Ordering;
import datameer.com.google.common.base.Function;
import datameer.com.google.common.base.Optional;
import datameer.com.google.common.base.Predicate;
import datameer.com.google.common.base.Supplier;
import datameer.com.google.common.collect.FluentIterable;
import datameer.com.google.common.collect.ImmutableList;
import datameer.com.google.common.collect.ImmutableList.Builder;

public abstract class Entity {

    private Optional<Long> _entityId;
    private final String _tableName;

    Entity(final Long entityId, final String tableName) {
	_entityId = Optional.of(entityId);
	_tableName = tableName;
    }

    Entity(final String tableName) {
	_entityId = Optional.absent();
	_tableName = tableName;
    }

    private static final <T> Function<Pair<Long, Iterable<Pair<String, String>>>, Try<T>> buildEntity(final Buildable<T> template) {
	return new Function<Pair<Long, Iterable<Pair<String, String>>>, Try<T>>() {
	    @Override
	    public Try<T> apply(final Pair<Long, Iterable<Pair<String, String>>> input) {
		if (FluentIterable.from(input._2).isEmpty()) {
		    String error = String.format("could not find Entity, is it even persisted?");
		    System.err.println(error);
		    return Try.failure(new IllegalStateException(error));
		}
		return template.build(input);
	    }
	};
    }

    private static final Function<Unit, Try<Unit>> createTableIfNotExists(final Iterable<Pair<String, String>> persistenceContext) {
	return new Function<Unit, Try<Unit>>() {
	    @Override
	    public Try<Unit> apply(final Unit input) {
		return DatabaseOperations.createTableIfNotExists(persistenceContext);
	    }
	};
    }

    private static Try<Unit> delete(final long entityId, final String tableName) {
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

    private static final FluentIterable<Pair<Long, Iterable<Pair<String, String>>>> loadContextsStartsWith(final String parameter, final String startsWith,
	    final String tablename,
	    final ImmutableList<String> keys, final Optional<Ordering> orderBy) {
	Try<FluentIterable<Pair<Long, Iterable<Pair<String, String>>>>> contexts = databaseExists()
	//
		.flatMap(tableExists(tablename))

		.flatMap(loadPersistenceContextsStartsWith(parameter, startsWith, tablename, keys, orderBy));
	if (contexts.isFailure()) {
	    System.err.println("could not load Entities with '" + parameter + "' LIKE '" + startsWith + "%', from table '" + tablename + "', reason:");
	    System.err.println(contexts.failure().getLocalizedMessage());
	    // return empty iterable in error case
	    Builder<Pair<Long, Iterable<Pair<String, String>>>> builder = ImmutableList.builder();
	    return FluentIterable.from(builder.build());
	}
	return contexts.get();
    }

    private static final FluentIterable<Pair<Long, Iterable<Pair<String, String>>>> loadContexts(final String parameter, final String value,
	    final String tableName, final ImmutableList<String> keys, final Optional<Ordering> orderBy) {
	Try<FluentIterable<Pair<Long, Iterable<Pair<String, String>>>>> contexts = databaseExists()
	//
		.flatMap(tableExists(tableName))

		.flatMap(loadPersistenceContexts(parameter, value, tableName, keys, orderBy));
	if (contexts.isFailure()) {
	    System.err.println(String.format("could not load Entities with '%s' = '%s', from table '%s', reason:", "" + parameter, value, tableName));
	    System.err.println(contexts.failure().getLocalizedMessage());
	    // return empty iterable in error case
	    Builder<Pair<Long, Iterable<Pair<String, String>>>> builder = ImmutableList.builder();
	    return FluentIterable.from(builder.build());
	}
	return contexts.get();
    }

    private static Function<Boolean, Try<FluentIterable<Pair<Long, Iterable<Pair<String, String>>>>>> loadPersistenceContextsStartsWith(final String parameter,
	    final String startsWith, final String tablename, final ImmutableList<String> keys, final Optional<Ordering> orderBy) {
	return new Function<Boolean, Try<FluentIterable<Pair<Long, Iterable<Pair<String, String>>>>>>() {
	    @Override
	    public Try<FluentIterable<Pair<Long, Iterable<Pair<String, String>>>>> apply(final Boolean tableExists) {
		if (!tableExists) {
		    System.err.println(String.format("Table '%s' does not exist", tablename));
		    throw new IllegalStateException(String.format("Table '%s' does not exist", tablename));
		}
		return DatabaseOperations.loadPersistenceContextsStartsWith(parameter, startsWith, tablename, keys, orderBy);
	    }
	};
    }

    private static Function<Boolean, Try<FluentIterable<Pair<Long, Iterable<Pair<String, String>>>>>> loadPersistenceContexts(final String parameter,
	    final String value, final String tableName, final ImmutableList<String> keys, final Optional<Ordering> orderBy) {
	return new Function<Boolean, Try<FluentIterable<Pair<Long, Iterable<Pair<String, String>>>>>>() {
	    @Override
	    public Try<FluentIterable<Pair<Long, Iterable<Pair<String, String>>>>> apply(final Boolean tableExists) {
		if (!tableExists) {
		    System.err.println(String.format("Table '%s' does not exist", tableName));
		    throw new IllegalStateException(String.format("Table '%s' does not exist", tableName));
		}
		return DatabaseOperations.loadPersistenceContexts(parameter, value, tableName, keys, orderBy);
	    }
	};
    }

    static final <T> Iterable<T> loadFromParameterStartsWith(final String parameter, final String startsWith, final String tablename,
	    final Buildable<T> template, final ImmutableList<String> keys,
 Optional<Ordering> orderBy) {
	Function<Try<T>, T> toTemplate = new Function<Try<T>, T>() {
	    @Override
	    public T apply(final Try<T> input) {
		return input.get();
	    }
	};
	Predicate<Try<T>> success = new Predicate<Try<T>>() {
	    @Override
	    public boolean apply(final Try<T> input) {
		return input.isSuccess();
	    }
	};
	return loadContextsStartsWith(parameter, startsWith, tablename, keys, orderBy)
	//
		.transform(buildEntity(template))

		.filter(success)

		.transform(toTemplate);
    }

    static final <T> Iterable<T> loadFromParameter(final String parameter, final String value, final String tableName, final Buildable<T> template,
	    final ImmutableList<String> keys, final Optional<Ordering> orderBy) {
	Function<Try<T>, T> toTemplate = new Function<Try<T>, T>() {
	    @Override
	    public T apply(final Try<T> input) {
		return input.get();
	    }
	};
	Predicate<Try<T>> success = new Predicate<Try<T>>() {
	    @Override
	    public boolean apply(final Try<T> input) {
		return input.isSuccess();
	    }
	};
	return loadContexts(parameter, value, tableName, keys, orderBy)
		//
		.transform(buildEntity(template))

		.filter(success)

		.transform(toTemplate);
    }

    static final <T> Optional<T> loadFromTemplate(final long entityId, final Buildable<T> template, final String tableName, final ImmutableList<String> keys) {
	Try<T> entity = loadContext(entityId, tableName, keys).flatMap(buildEntity(template));
	if (entity.isFailure()) {
	    System.err.println(String.format("could not load Entity with id '%s', from table '%s', reason:", "" + entityId, tableName));
	    System.err.println(entity.failure().getLocalizedMessage());
	    return Optional.absent();
	}
	return Optional.of(entity.get());
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
	Try<Long> save = Try.of(save(persistenceContext(), _entityId));
	if(save.isSuccess()){
	    _entityId = Optional.of(save.get());
	}
	return save;
    }
}
