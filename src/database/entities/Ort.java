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

public class Ort extends Entity implements Buildable<Ort> {
    private String _ortName;
    private static final ImmutableList<String> keys = ImmutableList.<String> builder().add("TABLENAME").add("ORT_NAME").build();
    public static final String TABLENAME = Ort.class.getSimpleName();

    @Override
    public Iterable<Pair<String, String>> persistenceContext() {
	LinkedList<Pair<String, String>> list = Lists.newLinkedList();
	// Table name must always be first!
	list.add(Pair.of(keys.get(0), this.getTableName()));
	list.add(Pair.of(keys.get(1), "" + this.getOrtName()));
	return FluentIterable.from(list);
    }

    private Ort(final Long entityId) {
	super(entityId, TABLENAME);
    }

    public Ort(final String ortName) {
	super(TABLENAME);
	_ortName = ortName;
    }

    public Ort(Long entityId, final String ortName) {
	super(entityId, TABLENAME);
	_ortName = ortName;
    }

    public final Try<Ort> build(final Pair<Long, Iterable<Pair<String, String>>> context) {
	return Try.of(new Supplier<Ort>() {
	    @Override
	    public Ort get() {
		Ort ort = new Ort(context._1);
		ImmutableList<Pair<String, String>> values = ImmutableList.copyOf(FluentIterable.from(context._2).filter(Predicates.withoutSecond(TABLENAME)));
		ort.setOrtName(values.get(0)._2);
		return ort;
	    }
	});
    }

    public String getOrtName() {
	return _ortName;
    }

    public void setOrtName(String ortName) {
	_ortName = ortName;
    }

    public static final Optional<Ort> loadById(final long entityId) {
	return loadFromTemplate(entityId, new Ort(entityId), TABLENAME, keys);
    }

    public static final Iterable<Ort> loadByParameter(final String parameter, final String value) {
	return loadFromParameter(parameter, value, TABLENAME, new Ort(0L), keys, Optional.<Ordering> absent());
    }

    public static final Iterable<Ort> loadByParameter(final String parameter, final String value, final Ordering orderBy) {
	return loadFromParameter(parameter, value, TABLENAME, new Ort(0L), keys, Optional.of(orderBy));
    }

    public static final Iterable<Ort> loadByParameterStartsWith(final String parameter, final String startsWith, final Ordering orderBy) {
	return loadFromParameterStartsWith(parameter, startsWith, TABLENAME, new Ort(0L), keys, Optional.of(orderBy));
    }

    public static final Iterable<Ort> loadByParameterStartsWith(final String parameter, final String startsWith) {
	return loadFromParameterStartsWith(parameter, startsWith, TABLENAME, new Ort(0L), keys, Optional.<Ordering> absent());
    }
}
