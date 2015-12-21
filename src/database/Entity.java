package database;

import static database.DatabaseOperations.createDatabaseIfNotExists;
import static database.DatabaseOperations.createTableIfNotExists;
import static database.DatabaseOperations.databaseExists;
import static database.DatabaseOperations.loadPersistenceContext;
import static database.DatabaseOperations.saveOrUpdate;
import static database.DatabaseOperations.tableExists;
import util.Pair;
import util.Try;
import util.Unit;
import datameer.com.google.common.base.Optional;
import datameer.com.google.common.base.Supplier;
import datameer.com.google.common.collect.FluentIterable;
import datameer.com.google.common.collect.ImmutableList;

abstract class Entity {

    private Optional<Long> _entityId;
    private String _tableName;

    public static Try<Entity> delete(final long entityId) {
	return null;
    }

    static final Try<Pair<Long, Iterable<Pair<String, String>>>> loadContext(final long entityId, final String tableName, final ImmutableList<String> keys) {
	return Try.of( new Supplier<Pair<Long, Iterable<Pair<String, String>>>>() {
	    @Override
	    public Pair<Long, Iterable<Pair<String, String>>> get() {
		if (!databaseExists()) {
		    System.err.println("Database does not exist.");
		    throw new IllegalStateException("Database does not exist. Cannot load Entity");
		}
		Try<Boolean> tableExists = tableExists(tableName);
		if (tableExists.isFailure()) {
		    System.err.println(String.format("An error occured accessing the table '%s'", tableName));
		    throw tableExists.propagate();
		}
		if (!tableExists.get()) {
		    System.err.println(String.format("Table '%s' does not exist", tableName));
		    throw new IllegalStateException(String.format("Table '%s' does not exist", tableName));
		}

		Try<Iterable<Pair<String, String>>> persistenceContext = loadPersistenceContext(entityId, tableName, keys);
		if(persistenceContext.isFailure()){
		    System.err.println(String.format("Could not load Entity with Id '%s' from table '%s'", ""+entityId, tableName));
		    throw persistenceContext.propagate();
		}
		return Pair.of(entityId, persistenceContext.get());
	    }
	});
    }

    public static Try<Entity> loadByParameter(final String parameter, final String value) {
	return null;
    }

    public Try<Entity> delete() {
	return null;
    }

    Entity(final String tableName) {
	_entityId = Optional.absent();
	_tableName = tableName;
    }

    Entity(final Long entityId, final String tableName) {
	_entityId = Optional.of(entityId);
	_tableName = tableName;
    }

    /**
     * @return context of the parameters that should be persisted
     */
    public abstract Iterable<Pair<String, String>> persistenceContext();

    private static Supplier<Long> save(final Iterable<Pair<String, String>> persistenceContext, final Optional<Long> entityId) {
	return new Supplier<Long>() {
	    @Override
	    public Long get() {
		// check if DB exists / create DB
		Try<Unit> dataBase = createDatabaseIfNotExists();
		if (!dataBase.isSuccess()) {
		    throw dataBase.propagate();
		}

		// check if table exists / create table
		Try<Unit> tableResult = createTableIfNotExists(persistenceContext);
		if (tableResult.isFailure()) {
		    throw tableResult.propagate();
		}

		// check if entity exists / create entity / update entity
		Try<Long> saveResult = saveOrUpdate(FluentIterable.from(persistenceContext), entityId);
		if (saveResult.isFailure()) {
		    throw saveResult.propagate();
		}
		return saveResult.get();
	    }
	};
    }

    public Try<Long> save() {
	return Try.of(save(persistenceContext(), _entityId));
    }

    public Optional<Long> getEntityId() {
	return _entityId;
    }

    public String getTableName() {
	return _tableName;
    }
}
