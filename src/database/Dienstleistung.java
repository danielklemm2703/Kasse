package database;

import java.util.LinkedList;

import util.Pair;
import util.Predicates;
import util.Try;
import datameer.com.google.common.base.Optional;
import datameer.com.google.common.base.Supplier;
import datameer.com.google.common.collect.FluentIterable;
import datameer.com.google.common.collect.ImmutableList;
import datameer.com.google.common.collect.Lists;

public class Dienstleistung extends Entity {

    private String _name;
    private Float _preis;
    private static final ImmutableList<String> keys = ImmutableList.<String> builder().add("TABLENAME").add("NAME").add("PREIS").build();
    private static final String TABLENAME = Dienstleistung.class.getSimpleName();

    @Override
    public Iterable<Pair<String, String>> persistenceContext() {
	LinkedList<Pair<String, String>> list = Lists.newLinkedList();
	// Table name must always be first!
	list.add(Pair.of(keys.get(0), this.getTableName()));
	list.add(Pair.of(keys.get(1), this.getName()));
	list.add(Pair.of(keys.get(2), this.getPreis().toString()));
	return FluentIterable.from(list);
    }

    public Dienstleistung(final String name, final Float preis) {
	super(TABLENAME);
	_name = name;
	_preis = preis;
    }

    public Dienstleistung(final long entityId, final String name, final Float preis) {
	super(entityId, TABLENAME);
	_name = name;
	_preis = preis;
    }

    Dienstleistung(Long entityId) {
	super(entityId, TABLENAME);
    }

    public Float getPreis() {
	return _preis;
    }

    public void setPreis(Float preis) {
	_preis = preis;
    }

    public String getName() {
	return _name;
    }

    public void setName(String name) {
	_name = name;
    }

    public static final Optional<Dienstleistung> loadById(long entityId) {
	Try<Pair<Long, Iterable<Pair<String, String>>>> loadContext = loadContext(entityId, TABLENAME, keys);
	if (loadContext.isFailure()) {
	    System.err.println(String.format("could not load Entity with id '%s', from table '%s', reason:", "" + entityId, TABLENAME));
	    System.err.println(loadContext.failure().getLocalizedMessage());
	    return Optional.absent();
	}
	Pair<Long, Iterable<Pair<String, String>>> context = loadContext.get();
	if (FluentIterable.from(context._2).isEmpty()) {
	    System.err.println(String.format("could not find Entity with id '%s', in table '%s', is it even persisted?", "" + entityId, TABLENAME));
	    return Optional.absent();
	}
	Try<Dienstleistung> dienstleistung = build(context);
	if (dienstleistung.isFailure()) {
	    System.err.println(String.format("could not parse Entity with id '%s', from table '%s', reason:", "" + entityId, TABLENAME));
	    System.err.println(dienstleistung.failure().getLocalizedMessage());
	    return Optional.absent();
	}
	return Optional.of(dienstleistung.get());
    }

    private static final Try<Dienstleistung> build(final Pair<Long, Iterable<Pair<String, String>>> context) {
	return Try.of(new Supplier<Dienstleistung>() {
	    @Override
	    public Dienstleistung get() {
		Dienstleistung dienstleistung = new Dienstleistung(context._1);
		ImmutableList<Pair<String, String>> values = ImmutableList.copyOf(FluentIterable.from(context._2).filter(Predicates.withoutSecond(TABLENAME)));
		dienstleistung.setName(values.get(0)._2);
		dienstleistung.setPreis(Float.parseFloat(values.get(1)._2));
		return dienstleistung;
	    }
	});
    }
}
