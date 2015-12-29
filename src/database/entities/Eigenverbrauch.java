package database.entities;

import java.util.LinkedList;

import org.joda.time.DateTime;

import util.Pair;
import util.Predicates;
import util.Try;
import datameer.com.google.common.base.Optional;
import datameer.com.google.common.base.Supplier;
import datameer.com.google.common.collect.FluentIterable;
import datameer.com.google.common.collect.ImmutableList;
import datameer.com.google.common.collect.Lists;

public class Eigenverbrauch extends Entity implements Buildable<Eigenverbrauch> {

    private long _friseurId;
    private DateTime _datum;
    private static final ImmutableList<String> keys = ImmutableList.<String> builder().add("TABLENAME").add("FRISEUR_ID").add("DATUM").build();
    public static final String TABLENAME = Eigenverbrauch.class.getSimpleName();

    @Override
    public Iterable<Pair<String, String>> persistenceContext() {
	LinkedList<Pair<String, String>> list = Lists.newLinkedList();
	// Table name must always be first!
	list.add(Pair.of(keys.get(0), this.getTableName()));
	list.add(Pair.of(keys.get(1), "" + this.getFriseurId()));
	list.add(Pair.of(keys.get(2), "" + this.getDatum().toString()));
	return FluentIterable.from(list);
    }

    private Eigenverbrauch(final Long entityId) {
	super(entityId, TABLENAME);
    }

    public Eigenverbrauch(final long friseurId, final DateTime datum) {
	super(TABLENAME);
	_friseurId = friseurId;
	_datum = datum;
    }

    public Eigenverbrauch(Long entityId, final long friseurId, final DateTime datum) {
	super(entityId, TABLENAME);
	_friseurId = friseurId;
	_datum = datum;
    }

    public long getFriseurId() {
	return _friseurId;
    }

    public void setFriseurId(long friseurId) {
	_friseurId = friseurId;
    }

    public DateTime getDatum() {
	return _datum;
    }

    public void setDatum(DateTime datum) {
	_datum = datum;
    }

    public static final Optional<Eigenverbrauch> loadById(long entityId) {
	return loadFromTemplate(entityId, new Eigenverbrauch(entityId), TABLENAME, keys);
    }

    public static final Iterable<Eigenverbrauch> loadByParameter(final String parameter, final String value) {
	return loadFromParameter(parameter, value, TABLENAME, new Eigenverbrauch(0L), keys);
    }

    public final Try<Eigenverbrauch> build(final Pair<Long, Iterable<Pair<String, String>>> context) {
	return Try.of(new Supplier<Eigenverbrauch>() {
	    @Override
	    public Eigenverbrauch get() {
		Eigenverbrauch eigenverbrauch = new Eigenverbrauch(context._1);
		ImmutableList<Pair<String, String>> values = ImmutableList.copyOf(FluentIterable.from(context._2).filter(Predicates.withoutSecond(TABLENAME)));
		eigenverbrauch.setFriseurId(Long.parseLong(values.get(0)._2));
		eigenverbrauch.setDatum(DateTime.parse(values.get(1)._2));
		return eigenverbrauch;
	    }
	});
    }
}
