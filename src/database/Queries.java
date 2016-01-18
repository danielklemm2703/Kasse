package database;

import util.Pair;
import util.Predicates;
import datameer.com.google.common.base.Optional;
import datameer.com.google.common.base.Predicate;
import datameer.com.google.common.collect.FluentIterable;

public final class Queries {
    public static final String saveEntityQuery(final FluentIterable<Pair<String, String>> persistenceContext) {
	final String tableName = persistenceContext.first().get()._2;
	String saveEntityQuery = "INSERT INTO " + tableName + " (";
	String saveEntityQueryValues = "VALUES (";
	FluentIterable<Pair<String, String>> withoutTableName = persistenceContext.filter(Predicates.withoutSecond(tableName));
	for (Pair<String, String> pair : withoutTableName) {
	    saveEntityQuery += " " + pair._1 + ", ";
	    saveEntityQueryValues += " '" + pair._2 + "', ";
	}
	saveEntityQuery = saveEntityQuery.substring(0, saveEntityQuery.length() - 2);
	saveEntityQueryValues = saveEntityQueryValues.substring(0, saveEntityQueryValues.length() - 2);
	saveEntityQuery += ") ";
	saveEntityQueryValues += ");";
	String returnQuery = saveEntityQuery + saveEntityQueryValues;
	System.err.println(returnQuery);
	return returnQuery;
    }

    public static final String createTableQuery(final FluentIterable<String> columnNames, final String tableName) {
	FluentIterable<String> withoutTableName = columnNames.filter(Predicates.without(columnNames.first().get()));
	String createTableQuery = "CREATE TABLE " + tableName + "(ID INTEGER PRIMARY KEY AUTOINCREMENT";
	for (String columnName : withoutTableName) {
	    createTableQuery += " ," + columnName + " TEXT NOT NULL ";
	}
	String returnQuery = createTableQuery += ")";
	System.err.println(returnQuery);
	return returnQuery;
    }

    public static final String tableExistsQuery(final String tableName) {
	return String.format("SELECT name FROM sqlite_master WHERE name='%s'", tableName);
    }

    private static final Predicate<Pair<String, String>> findTableName = new Predicate<Pair<String, String>>() {
	@Override
	public boolean apply(Pair<String, String> input) {
	    return input._1.equals("TABLENAME");
	}
    };

    public static final String rowCount(final FluentIterable<Pair<String, String>> persistenceContext) {
	String tableName = persistenceContext.filter(findTableName).first().get()._2;
	return "SELECT COUNT(*) AS rowcount FROM '" + tableName + "'";
    }

    public static final String updateEntityQuery(final FluentIterable<Pair<String, String>> persistenceContext, final Long entityId) {
	final String tableName = persistenceContext.first().get()._2;
	String updateEntityQuery = "UPDATE " + tableName + " SET ";
	FluentIterable<Pair<String, String>> withoutTableName = persistenceContext.filter(Predicates.withoutSecond(tableName));
	for (Pair<String, String> pair : withoutTableName) {
	    updateEntityQuery += " " + pair._1 + "='" + pair._2 + "', ";
	}
	updateEntityQuery = updateEntityQuery.substring(0, updateEntityQuery.length() - 2);
	String returnQuery = updateEntityQuery += " WHERE ID=" + entityId + ";";
	System.err.println(returnQuery);
	return returnQuery;
    }

    public static final String deleteEntityQuery(long entityId, String tableName) {
	return String.format("DELETE from %s where ID=%s;", tableName, "" + entityId);
    }

    public static final String loadContextById(long entityId, String tableName) {
	return String.format("SELECT * FROM " + tableName + " WHERE ID=" + entityId + ";");
    }

    public static final String loadContextByParameter(final String parameter, final String value, final String tableName, final Optional<Ordering> orderBy) {
	if (!orderBy.isPresent()) {
	    System.err.println(String.format("SELECT * FROM '%s' WHERE %s='%s';", tableName, parameter, value));
	    return String.format("SELECT * FROM '%s' WHERE %s='%s';", tableName, parameter, value);
	}
	String order = orderBy.get()._orderBy;
	String direction = orderBy.get()._direction;
	System.err.println(String.format("SELECT * FROM '%s' WHERE %s='%s' ORDER BY %s %s;", tableName, parameter, value, order, direction));
	return String.format("SELECT * FROM '%s' WHERE %s='%s' ORDER BY %s %s;", tableName, parameter, value, order, direction);
    }

    public static final String loadContextByParameterStartsWith(final String parameter, final String startsWith, final String tablename,
	    final Optional<Ordering> orderBy) {
	if (!orderBy.isPresent()) {
	    System.err.println("SELECT * FROM '" + tablename + "' WHERE " + parameter + " LIKE '" + startsWith + "%';");
	    return "SELECT * FROM '" + tablename + "' WHERE " + parameter + " LIKE '" + startsWith + "%';";
	}
	String order = orderBy.get()._orderBy;
	String direction = orderBy.get()._direction;
	System.err.println("SELECT * FROM '" + tablename + "' WHERE " + parameter + " LIKE '" + startsWith + "%' ORDER BY " + order + " " + direction + ";");
	return "SELECT * FROM '" + tablename + "' WHERE " + parameter + " LIKE '" + startsWith + "%' ORDER BY " + order + " " + direction + ";";
    }

    public static final String entityIdQuery(final FluentIterable<Pair<String, String>> persistenceContext) {
	final String tableName = persistenceContext.first().get()._2;
	System.err.println("SELECT MAX(ID) AS LAST FROM " + tableName);
	return "SELECT MAX(ID) AS LAST FROM " + tableName;
    }
}
