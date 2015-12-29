package database;

import static database.DatabaseConstants.CLASS_NAME;
import static database.DatabaseConstants.NAME;
import static database.DatabaseConstants.PATH;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import util.Pair;
import util.Predicates;
import util.Try;
import util.Unit;
import datameer.com.google.common.base.Function;
import datameer.com.google.common.base.Optional;
import datameer.com.google.common.base.Predicate;
import datameer.com.google.common.base.Supplier;
import datameer.com.google.common.collect.FluentIterable;
import datameer.com.google.common.collect.ImmutableList;
import datameer.com.google.common.collect.ImmutableList.Builder;

public class DatabaseOperations {

    private static final Predicate<Pair<String, String>> findTableName = new Predicate<Pair<String, String>>() {
	@Override
	public boolean apply(final Pair<String, String> input) {
	    return input._1.equals("TABLENAME");
	}
    };

    private static final Function<Pair<String, String>, String> toColumnNames = new Function<Pair<String, String>, String>() {
	@Override
	public String apply(final Pair<String, String> input) {
	    return input._1;
	}
    };

    private static final Try<Unit> createDatabase() {
	return Try.of(new Supplier<Unit>() {
	    @Override
	    public Unit get() {
		try {
		    Class.forName(CLASS_NAME);
		    DriverManager.getConnection(PATH);
		} catch (Exception e) {
		    throw new IllegalStateException(e.getClass().getName() + ": " + e.getMessage());
		}
		System.err.println("Successfully created database '" + PATH + "'");
		return Unit.VALUE;
	    }
	});
    }

    public static final Try<Unit> createDatabaseIfNotExists() {
	return Try.of(new Supplier<Unit>() {
	    @Override
	    public Unit get() {
		Try<Boolean> databaseExists = databaseExists();
		if (databaseExists.isFailure()) {
		    System.err.println("Could not find database");
		    throw databaseExists.propagate();
		}
		if (!databaseExists.get()) {
		    System.err.println("Database '" + NAME + "' not found");
		    Try<Unit> createDatabase = createDatabase();
		    if (!createDatabase.isSuccess()) {
			System.err.println("Could not create Database");
			throw createDatabase.propagate();
		    }
		}
		return Unit.VALUE;
	    }
	});
    }

    private static final Try<Unit> createTable(final Iterable<Pair<String, String>> persistenceContext) {
	return Try.of(new Supplier<Unit>() {
	    @Override
	    public Unit get() {
		FluentIterable<String> columnNames = FluentIterable.from(persistenceContext).transform(toColumnNames);
		try {
		    Class.forName(CLASS_NAME);
		    Connection c = DriverManager.getConnection(PATH);
		    Statement stmt = c.createStatement();

		    String sql = Queries.createTableQuery(columnNames, FluentIterable.from(persistenceContext).filter(findTableName).first().get()._2);
		    stmt.executeUpdate(sql);
		    stmt.close();
		    c.close();
		} catch (Exception e) {
		    throw new IllegalStateException(e.getClass().getName() + ": " + e.getMessage());
		}
		return Unit.VALUE;
	    }
	});
    }

    public static final Try<Unit> createTableIfNotExists(final Iterable<Pair<String, String>> persistenceContext) {
	return Try.of(new Supplier<Unit>() {
	    @Override
	    public Unit get() {
		Optional<Pair<String, String>> table = FluentIterable.from(persistenceContext).filter(findTableName).first();
		if (!table.isPresent()) {
		    throw new IllegalStateException("No TableName in the persistence context:" + persistenceContext.toString());
		}

		Try<Boolean> exists = tableExists(table.get()._2);
		if (exists.isFailure()) {
		    System.err.println("An Error occured while trying to read the table " + table.get()._2);
		    throw exists.propagate();
		}
		if (!exists.get()) {
		    System.err.println("Table '" + table.get()._2 + "' does not exist.");
		    Try<Unit> createTable = createTable(persistenceContext);
		    if (createTable.isFailure()) {
			System.err.println(String.format("Could not create table '%s'", table.get()._2));
			throw createTable.propagate();
		    }
		    System.err.println(String.format("Successfully added table '%s'", table.get()._2));
		} else {
		    System.err.println(String.format("Table '%s' already exists. No need to create it.", table.get()._2));
		}
		return Unit.VALUE;
	    }
	});
    }

    public static final Try<Boolean> databaseExists() {
	return Try.of(new Supplier<Boolean>() {
	    @Override
	    public Boolean get() {
		File f = new File(NAME);
		return f.exists();
	    }
	});
    }

    public static final Try<Long> saveOrUpdate(final FluentIterable<Pair<String, String>> persistenceContext, final Optional<Long> entityId) {
	return Try.of(new Supplier<Long>() {
	    @Override
	    public Long get() {
		if (!entityId.isPresent()) {
		    System.err.println("Entity does not already exist, can be inserted");
		    Try<Long> save = saveEntity(persistenceContext);
		    if (save.isFailure()) {
			System.err.println("An error occured while inserting the entity");
			throw save.propagate();
		    }
		    System.err.println("Successfully inserted the entity");
		    return save.get();
		} else {
		    System.err.println("Entity already exists, update necessary");
		    Try<Long> update = updateEntity(persistenceContext, entityId.get());
		    if (update.isFailure()) {
			System.err.println("An error occured while updating the entity");
			throw update.propagate();
		    }
		    System.err.println("Successfully updated the entity");
		    return update.get();
		}
	    }
	});
    }

    private static final Try<Long> updateEntity(final FluentIterable<Pair<String, String>> persistenceContext, final Long entityId) {
	return Try.of(new Supplier<Long>() {
	    @Override
	    public Long get() {
		try {
		    Class.forName(CLASS_NAME);
		    Connection c = DriverManager.getConnection(PATH);
		    c.setAutoCommit(false);
		    Statement stmt = c.createStatement();

		    String saveEntityQuery = Queries.updateEntityQuery(persistenceContext, entityId);
		    stmt.executeUpdate(saveEntityQuery);
		    stmt.close();
		    c.commit();
		    c.close();
		    return entityId;
		} catch (Exception e) {
		    System.err.println(e.getClass().getName() + ": " + e.getMessage());
		    throw new IllegalStateException(e.getClass().getName() + ": " + e.getMessage());
		}
	    }
	});
    }

    public static final Try<Unit> delete(final long entityId, final String tableName) {
	return Try.of(new Supplier<Unit>() {
	    @Override
	    public Unit get() {
		try {
		    Class.forName(CLASS_NAME);
		    Connection c = DriverManager.getConnection(PATH);
		    c.setAutoCommit(false);

		    Statement stmt = c.createStatement();
		    String sql = Queries.deleteEntityQuery(entityId, tableName);
		    stmt.executeUpdate(sql);
		    c.commit();
		    stmt.close();
		    c.close();
		    return Unit.VALUE;
		} catch (Exception e) {
		    throw new IllegalStateException(e.getClass().getName() + ": " + e.getMessage());
		}

	    }
	});
    }

    private static final Try<Long> saveEntity(final Iterable<Pair<String, String>> persistenceContext) {
	return Try.of(new Supplier<Long>() {
	    @Override
	    public Long get() {
		try {
		    Class.forName(CLASS_NAME);
		    Connection c = DriverManager.getConnection(PATH);
		    c.setAutoCommit(false);
		    Statement stmt = c.createStatement();

		    Statement s = c.createStatement();
		    ResultSet r = s.executeQuery(Queries.rowCount(FluentIterable.from(persistenceContext)));
		    r.next();
		    Long entityId = r.getLong("rowcount");
		    r.close();
		    System.out.println("computed entity id is " + entityId);

		    String saveEntityQuery = Queries.saveEntityQuery(FluentIterable.from(persistenceContext), entityId);
		    stmt.executeUpdate(saveEntityQuery);
		    stmt.close();
		    c.commit();
		    c.close();
		    return entityId;
		} catch (Exception e) {
		    throw new IllegalStateException(e.getClass().getName() + ": " + e.getMessage());
		}
	    }
	});
    }

    public static final Try<Boolean> tableExists(final String tableName) {
	return Try.of(new Supplier<Boolean>() {
	    @Override
	    public Boolean get() {
		try {
		    Class.forName(CLASS_NAME);
		    Connection c = DriverManager.getConnection(PATH);
		    Statement stmt = c.createStatement();
		    String sql = Queries.tableExistsQuery(tableName);
		    ResultSet rs = stmt.executeQuery(sql);
		    boolean exists = rs.next();
		    stmt.close();
		    c.close();
		    return exists;
		} catch (Exception e) {
		    System.err.println(String.format("An error occured accessing the table '%s'", tableName));
		    throw new IllegalStateException(e.getClass().getName() + ": " + e.getMessage());
		}
	    }
	});
    }

    public static final Try<Iterable<Pair<String, String>>> loadPersistenceContext(final long entityId, final String tableName, final ImmutableList<String> keys) {
	return Try.of(new Supplier<Iterable<Pair<String, String>>>() {
	    @Override
	    public Iterable<Pair<String, String>> get() {
		try {
		    Class.forName(CLASS_NAME);
		    Connection c = DriverManager.getConnection(PATH);
		    c.setAutoCommit(false);

		    Statement stmt = c.createStatement();
		    ResultSet rs = stmt.executeQuery(Queries.loadContextById(entityId, tableName));
		    FluentIterable<String> loadableKeys = FluentIterable.from(keys).filter(Predicates.without(keys.get(0)));
		    Builder<Pair<String, String>> accumulate = ImmutableList.builder();
		    if (rs.next()) {
			for (String key : loadableKeys) {
			    String value = rs.getString(key);
			    System.out.println(key + " , " + value);
			    accumulate.add(Pair.of(key, value));
			}
		    }
		    rs.close();
		    stmt.close();
		    c.close();
		    return accumulate.build();
		} catch (Exception e) {
		    System.err.println(e.getClass().getName() + ": " + e.getMessage());
		    throw new IllegalStateException(e.getClass().getName() + ": " + e.getMessage());
		}
	    }
	});
    }

    public static final Try<FluentIterable<Pair<Long, Iterable<Pair<String, String>>>>> loadPersistenceContexts(final String parameter, final String value,
	    final String tableName, final ImmutableList<String> keys) {
	return Try.of(new Supplier<FluentIterable<Pair<Long, Iterable<Pair<String, String>>>>>() {
	    @Override
	    public FluentIterable<Pair<Long, Iterable<Pair<String, String>>>> get() {
		try {
		    Class.forName(CLASS_NAME);
		    Connection c = DriverManager.getConnection(PATH);
		    c.setAutoCommit(false);

		    Statement stmt = c.createStatement();
		    ResultSet rs = stmt.executeQuery(Queries.loadContextByParameter(parameter, value, tableName));
		    FluentIterable<String> loadableKeys = FluentIterable.from(keys).filter(Predicates.without(keys.get(0)));
		    Builder<Pair<Long, Iterable<Pair<String, String>>>> entities = ImmutableList.builder();
		    while (rs.next()) {
			Builder<Pair<String, String>> accumulate = ImmutableList.builder();
			for (String key : loadableKeys) {
			    String val = rs.getString(key);
			    System.out.println(key + " , " + val);
			    accumulate.add(Pair.of(key, val));
			}
			long entityId = rs.getLong("ID");
			Iterable<Pair<String, String>> entity = accumulate.build();
			entities.add(Pair.of(entityId, entity));
		    }
		    rs.close();
		    stmt.close();
		    c.close();
		    return FluentIterable.from(entities.build());
		} catch (Exception e) {
		    System.err.println(e.getClass().getName() + ": " + e.getMessage());
		    throw new IllegalStateException(e.getClass().getName() + ": " + e.getMessage());
		}
	    }
	});
    }
}