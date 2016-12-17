package database.entities;

import java.util.LinkedList;

import util.Pair;
import util.Predicates;
import util.Try;
import database.Ordering;
import database.enums.FaerbeTechnik;
import datameer.com.google.common.base.Optional;
import datameer.com.google.common.base.Supplier;
import datameer.com.google.common.collect.FluentIterable;
import datameer.com.google.common.collect.ImmutableList;
import datameer.com.google.common.collect.Lists;

public class Farbe extends Entity implements Buildable<Farbe> {

    private static final ImmutableList<String> keys = ImmutableList.<String> builder().add("TABLENAME").add("PRAEPARAT_ID").add("FAERBETECHNIK").add("FARBE")
	    .add("OXYD").build();
    public static final String TABLENAME = Farbe.class.getSimpleName();
    private long _praeparatId;
    private FaerbeTechnik _faerbeTechnik;
    private String _farbe;
    private String _oxyd;

    public Farbe(final long entityId, final long praeparatId, final FaerbeTechnik technik, final String farbe, final String oxyd) {
	super(entityId, TABLENAME);
	setPraeparatId(praeparatId);
	setFaerbeTechnik(technik);
	setFarbe(farbe);
	setOxyd(oxyd);
    }

    Farbe(final Long entityId) {
	super(entityId, TABLENAME);
    }

    public Farbe(final long praeparatId, final FaerbeTechnik technik, final String farbe, final String oxyd) {
	super(TABLENAME);
	setPraeparatId(praeparatId);
	setFaerbeTechnik(technik);
	setFarbe(farbe);
	setOxyd(oxyd);
    }

    public static final Optional<Farbe> loadById(final long entityId) {
	return loadFromTemplate(entityId, new Farbe(entityId), TABLENAME, keys);
    }

    public static final Iterable<Farbe> loadByParameter(final String parameter, final String value) {
	return loadFromParameter(parameter, value, TABLENAME, new Farbe(0L), keys, Optional.<Ordering> absent());
    }

    public static final Iterable<Farbe> loadByParameter(final String parameter, final String value, final Ordering orderBy) {
	return loadFromParameter(parameter, value, TABLENAME, new Farbe(0L), keys, Optional.of(orderBy));
    }

    public static final Iterable<Farbe> loadByParameterStartsWith(final String parameter, final String startsWith, final Ordering orderBy) {
	return loadFromParameterStartsWith(parameter, startsWith, TABLENAME, new Farbe(0L), keys, Optional.of(orderBy));
    }

    public static final Iterable<Farbe> loadByParameterStartsWith(final String parameter, final String startsWith) {
	return loadFromParameterStartsWith(parameter, startsWith, TABLENAME, new Farbe(0L), keys, Optional.<Ordering> absent());
    }

    @Override
    public final Try<Farbe> build(final Pair<Long, Iterable<Pair<String, String>>> context) {
	return Try.of(new Supplier<Farbe>() {
	    @Override
	    public Farbe get() {
		Farbe farbe = new Farbe(context._1);
		ImmutableList<Pair<String, String>> values = ImmutableList.copyOf(FluentIterable.from(context._2).filter(Predicates.withoutSecond(TABLENAME)));
		farbe.setPraeparatId(Long.parseLong(values.get(0)._2));
		farbe.setFaerbeTechnik(FaerbeTechnik.of(values.get(1)._2));
		farbe.setFarbe(values.get(2)._2);
		farbe.setOxyd(values.get(3)._2);
		return farbe;
	    }
	});
    }

    @Override
    public Iterable<Pair<String, String>> persistenceContext() {
	LinkedList<Pair<String, String>> list = Lists.newLinkedList();
	// Table name must always be first!
	list.add(Pair.of(keys.get(0), getTableName()));
	list.add(Pair.of(keys.get(1), "" + getPraeparatId()));
	list.add(Pair.of(keys.get(2), getFaerbeTechnik().toString()));
	list.add(Pair.of(keys.get(3), getFarbe()));
	list.add(Pair.of(keys.get(4), getOxyd()));
	return FluentIterable.from(list);
    }

    public long getPraeparatId() {
	return _praeparatId;
    }

    public void setPraeparatId(long praeparatId) {
	_praeparatId = praeparatId;
    }

    public FaerbeTechnik getFaerbeTechnik() {
	return _faerbeTechnik;
    }

    public void setFaerbeTechnik(FaerbeTechnik faerbeTechnik) {
	_faerbeTechnik = faerbeTechnik;
    }

    public String getFarbe() {
	return _farbe;
    }

    public void setFarbe(String farbe) {
	_farbe = farbe;
    }

    public String getOxyd() {
	return _oxyd;
    }

    public void setOxyd(String oxyd) {
	_oxyd = oxyd;
    }
}
