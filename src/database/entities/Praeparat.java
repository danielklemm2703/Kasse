package database.entities;

import java.util.LinkedList;

import util.Pair;
import util.Predicates;
import util.Try;
import database.Ordering;
import datameer.com.google.common.base.Optional;
import datameer.com.google.common.base.Supplier;
import datameer.com.google.common.collect.FluentIterable;
import datameer.com.google.common.collect.ImmutableList;
import datameer.com.google.common.collect.Lists;

public class Praeparat extends Entity implements Buildable<Praeparat> {

    private String _name;
    private static final ImmutableList<String> keys = ImmutableList.<String> builder().add("TABLENAME").add("NAME").build();
    public static final String TABLENAME = Praeparat.class.getSimpleName();

    @Override
    public Iterable<Pair<String, String>> persistenceContext() {
	LinkedList<Pair<String, String>> list = Lists.newLinkedList();
	// Table name must always be first!
	list.add(Pair.of(keys.get(0), this.getTableName()));
	list.add(Pair.of(keys.get(1), this.getName()));
	return FluentIterable.from(list);
    }

    private Praeparat(final Long entityId) {
	super(entityId, TABLENAME);
    }

    public Praeparat(final String name) {
	super(TABLENAME);
	_name = name;
    }

    public Praeparat(Long entityId, final String name) {
	super(entityId, TABLENAME);
	_name = name;
    }

    public static final Optional<Praeparat> loadById(long entityId) {
	return loadFromTemplate(entityId, new Praeparat(entityId), TABLENAME, keys);
    }

    public static final Iterable<Praeparat> loadByParameter(final String parameter, final String value) {
	return loadFromParameter(parameter, value, TABLENAME, new Praeparat(0L), keys, Optional.<Ordering> absent());
    }

    public static final Iterable<Praeparat> loadByParameter(final String parameter, final String value, final Ordering orderBy) {
	return loadFromParameter(parameter, value, TABLENAME, new Praeparat(0L), keys, Optional.of(orderBy));
    }
    public final Try<Praeparat> build(final Pair<Long, Iterable<Pair<String, String>>> context) {
	return Try.of(new Supplier<Praeparat>() {
	    @Override
	    public Praeparat get() {
		Praeparat Praeparat = new Praeparat(context._1);
		ImmutableList<Pair<String, String>> values = ImmutableList.copyOf(FluentIterable.from(context._2).filter(Predicates.withoutSecond(TABLENAME)));
		Praeparat.setName(values.get(0)._2);
		return Praeparat;
	    }
	});
    }

    public String getName() {
	return _name;
    }

    public void setName(String name) {
	_name = name;
    }
}
