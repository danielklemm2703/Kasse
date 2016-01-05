package database.entities;

import java.util.LinkedList;

import util.Pair;
import util.Predicates;
import util.Try;
import backend.enums.WickelStaerke;
import backend.enums.WickelTyp;
import database.Ordering;
import datameer.com.google.common.base.Optional;
import datameer.com.google.common.base.Supplier;
import datameer.com.google.common.collect.FluentIterable;
import datameer.com.google.common.collect.ImmutableList;
import datameer.com.google.common.collect.Lists;

public class Wickel extends Entity implements Buildable<Wickel> {

    private long _einwirkZeit;
    private WickelTyp _wickelTyp;
    private WickelStaerke _wickelstaerke;
    private String _wickelFarbe;
    private long _praeparatId;
    private boolean _waerme;

    private static final ImmutableList<String> keys = ImmutableList.<String> builder()
    //
	    .add("TABLENAME")

	    .add("EINWIRKZEIT")

	    .add("WICKELTYP")

	    .add("WICKELSTAERKE")

	    .add("WICKELFARBE")

	    .add("PRAEPARAT_ID")

	    .add("WAERME")

	    .build();

    public static final String TABLENAME = Wickel.class.getSimpleName();

    @Override
    public Iterable<Pair<String, String>> persistenceContext() {
	LinkedList<Pair<String, String>> list = Lists.newLinkedList();
	// Table name must always be first!
	list.add(Pair.of(keys.get(0), this.getTableName()));
	list.add(Pair.of(keys.get(1), "" + this.getEinwirkZeit()));
	list.add(Pair.of(keys.get(2), this.getWickelTyp().toString()));
	list.add(Pair.of(keys.get(3), this.getWickelstaerke().toString()));
	list.add(Pair.of(keys.get(4), this.getWickelFarbe()));
	list.add(Pair.of(keys.get(5), "" + this.getPraeparatId()));
	list.add(Pair.of(keys.get(6), Boolean.toString(this.isWaerme())));
	return FluentIterable.from(list);
    }

    private Wickel(final Long entityId) {
	super(entityId, TABLENAME);
    }

    public Wickel(final long einwirkZeit, final WickelTyp wickeltyp, final WickelStaerke wickelstaerke, final String wickelFarbe, final long praeparatId,
	    final boolean waerme) {
	super(TABLENAME);
	setEinwirkZeit(einwirkZeit);
	setWickelTyp(wickeltyp);
	setWickelstaerke(wickelstaerke);
	setWickelFarbe(wickelFarbe);
	setPraeparatId(praeparatId);
	setWaerme(waerme);
    }

    public Wickel(Long entityId, final long einwirkZeit, final WickelTyp wickeltyp, final WickelStaerke wickelstaerke, final String wickelFarbe,
	    final long praeparatId, final boolean waerme) {
	super(entityId, TABLENAME);
	setEinwirkZeit(einwirkZeit);
	setWickelTyp(wickeltyp);
	setWickelstaerke(wickelstaerke);
	setWickelFarbe(wickelFarbe);
	setPraeparatId(praeparatId);
	setWaerme(waerme);
    }

    public static final Optional<Wickel> loadById(final long entityId) {
	return loadFromTemplate(entityId, new Wickel(entityId), TABLENAME, keys);
    }

    public static final Iterable<Wickel> loadByParameter(final String parameter, final String value) {
	return loadFromParameter(parameter, value, TABLENAME, new Wickel(0L), keys, Optional.<Ordering> absent());
    }

    public static final Iterable<Wickel> loadByParameter(final String parameter, final String value, final Ordering orderBy) {
	return loadFromParameter(parameter, value, TABLENAME, new Wickel(0L), keys, Optional.of(orderBy));
    }

    public static final Iterable<Wickel> loadByParameterStartsWith(final String parameter, final String startsWith, final Ordering orderBy) {
	return loadFromParameterStartsWith(parameter, startsWith, TABLENAME, new Wickel(0L), keys, Optional.of(orderBy));
    }

    public static final Iterable<Wickel> loadByParameterStartsWith(final String parameter, final String startsWith) {
	return loadFromParameterStartsWith(parameter, startsWith, TABLENAME, new Wickel(0L), keys, Optional.<Ordering> absent());
    }

    public final Try<Wickel> build(final Pair<Long, Iterable<Pair<String, String>>> context) {
	return Try.of(new Supplier<Wickel>() {
	    @Override
	    public Wickel get() {
		Wickel wickel = new Wickel(context._1);
		ImmutableList<Pair<String, String>> values = ImmutableList.copyOf(FluentIterable.from(context._2).filter(Predicates.withoutSecond(TABLENAME)));
		wickel.setEinwirkZeit(Long.parseLong(values.get(0)._2));
		wickel.setWickelTyp(WickelTyp.of(values.get(1)._2));
		wickel.setWickelstaerke(WickelStaerke.of(values.get(2)._2));
		wickel.setWickelFarbe(values.get(3)._2);
		wickel.setPraeparatId(Long.parseLong(values.get(4)._2));
		wickel.setWaerme(Boolean.parseBoolean(values.get(5)._2));
		return wickel;
	    }
	});
    }

    public long getEinwirkZeit() {
	return _einwirkZeit;
    }

    public void setEinwirkZeit(long einwirkZeit) {
	_einwirkZeit = einwirkZeit;
    }

    public WickelTyp getWickelTyp() {
	return _wickelTyp;
    }

    public void setWickelTyp(WickelTyp wickelTyp) {
	_wickelTyp = wickelTyp;
    }

    public WickelStaerke getWickelstaerke() {
	return _wickelstaerke;
    }

    public void setWickelstaerke(WickelStaerke wickelstaerke) {
	_wickelstaerke = wickelstaerke;
    }

    public String getWickelFarbe() {
	return _wickelFarbe;
    }

    public void setWickelFarbe(String wickelFarbe) {
	_wickelFarbe = wickelFarbe;
    }

    public long getPraeparatId() {
	return _praeparatId;
    }

    public void setPraeparatId(long praeparatId) {
	_praeparatId = praeparatId;
    }

    public boolean isWaerme() {
	return _waerme;
    }

    public void setWaerme(boolean waerme) {
	_waerme = waerme;
    }
}
