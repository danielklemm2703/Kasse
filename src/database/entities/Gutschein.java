package database.entities;

import java.util.LinkedList;

import util.Pair;
import util.Predicates;
import util.Preis;
import util.Try;
import util.Unit;
import database.Ordering;
import datameer.com.google.common.base.Optional;
import datameer.com.google.common.base.Supplier;
import datameer.com.google.common.collect.FluentIterable;
import datameer.com.google.common.collect.ImmutableList;
import datameer.com.google.common.collect.Lists;

public class Gutschein extends Entity implements Buildable<Gutschein> {

    private long _transaktionId;
    private String _kundeName;
    private Preis _restWert;
    private static final ImmutableList<String> keys = ImmutableList.<String> builder()
    //
	    .add("TABLENAME")

	    .add("TRANSAKTION_ID")

	    .add("KUNDE_NAME")

	    .add("RESTWERT")

	    .build();
    public static final String TABLENAME = Gutschein.class.getSimpleName();

    @Override
    public Iterable<Pair<String, String>> persistenceContext() {
	LinkedList<Pair<String, String>> list = Lists.newLinkedList();
	// Table name must always be first!
	list.add(Pair.of(keys.get(0), this.getTableName()));
	list.add(Pair.of(keys.get(1), "" + this.getTransaktionId()));
	list.add(Pair.of(keys.get(2), "" + this.getKundeName()));
	list.add(Pair.of(keys.get(3), this.getRestWert().toString()));
	return FluentIterable.from(list);
    }

    public Gutschein(final long transaktionId, final String kundeName, final Preis restWert) {
	super(TABLENAME);
	_transaktionId = transaktionId;
	_kundeName = kundeName;
	_restWert = restWert;
    }

    public Gutschein(final long entityId, final long transaktionId, final String kundeName, final Preis restWert) {
	super(entityId, TABLENAME);
	_transaktionId = transaktionId;
	_kundeName = kundeName;
	_restWert = restWert;
    }

    Gutschein(Long entityId) {
	super(entityId, TABLENAME);
    }

    public long getTransaktionId() {
	return _transaktionId;
    }

    public void setTransaktionId(long transaktionId) {
	_transaktionId = transaktionId;
    }

    public String getKundeName() {
	return _kundeName;
    }

    public void setKundeName(String kundeName) {
	_kundeName = kundeName;
    }

    public Preis getRestWert() {
	return _restWert;
    }

    public void setRestWert(Preis restWert) {
	_restWert = restWert;
    }

    @Override
    public Try<Unit> delete() {
	return Try.failure(new IllegalStateException("Gutschein darf nicht gelöscht werden, aufgrund von Umsatznachvollziehbarkeit."));
    }

    public static final Optional<Gutschein> loadById(long entityId) {
	return loadFromTemplate(entityId, new Gutschein(entityId), TABLENAME, keys);
    }

    public static final Iterable<Gutschein> loadByParameter(final String parameter, final String value) {
	return loadFromParameter(parameter, value, TABLENAME, new Gutschein(0L), keys, Optional.<Ordering> absent());
    }

    public static final Iterable<Gutschein> loadByParameter(final String parameter, final String value, final Ordering orderBy) {
	return loadFromParameter(parameter, value, TABLENAME, new Gutschein(0L), keys, Optional.of(orderBy));
    }

    public static final Iterable<Gutschein> loadByParameterStartsWith(final String parameter, final String startsWith, final Ordering orderBy) {
	return loadFromParameterStartsWith(parameter, startsWith, TABLENAME, new Gutschein(0L), keys, Optional.of(orderBy));
    }

    public static final Iterable<Gutschein> loadByParameterStartsWith(final String parameter, final String startsWith) {
	return loadFromParameterStartsWith(parameter, startsWith, TABLENAME, new Gutschein(0L), keys, Optional.<Ordering> absent());
    }

    public final Try<Gutschein> build(final Pair<Long, Iterable<Pair<String, String>>> context) {
	return Try.of(new Supplier<Gutschein>() {
	    @Override
	    public Gutschein get() {
		Gutschein gutschein = new Gutschein(context._1);
		ImmutableList<Pair<String, String>> values = ImmutableList.copyOf(FluentIterable.from(context._2).filter(Predicates.withoutSecond(TABLENAME)));
		gutschein.setTransaktionId(Long.parseLong(values.get(0)._2));
		gutschein.setKundeName(values.get(1)._2);
		gutschein.setRestWert(Preis.of(values.get(2)._2));
		return gutschein;
	    }
	});
    }
}
