package database.entities;

import java.util.LinkedList;

import util.Functions;
import util.Pair;
import util.Predicates;
import util.Try;
import database.Ordering;
import datameer.com.google.common.base.Optional;
import datameer.com.google.common.base.Supplier;
import datameer.com.google.common.collect.FluentIterable;
import datameer.com.google.common.collect.ImmutableList;
import datameer.com.google.common.collect.Lists;

public class Rezeptur extends Entity implements Buildable<Rezeptur> {

    private long _transaktionId;
    private String _kundeName;
    private Optional<FluentIterable<Long>> _farbIds;
    private Optional<Long> _wickelId;
    private String _ergebnis;
    private boolean _bereitsEingetragen;

    private static final ImmutableList<String> keys = ImmutableList.<String> builder()
    //
	    .add("TABLENAME")

	    .add("TRANSAKTION_ID")

	    .add("KUNDE_NAME")

	    .add("FARB_IDS")

	    .add("WICKEL_ID")

	    .add("ERGEBNIS")

	    .add("BEREITS_EINGETRAGEN")

	    .build();

    public static final String TABLENAME = Rezeptur.class.getSimpleName();

    @Override
    public Iterable<Pair<String, String>> persistenceContext() {
	LinkedList<Pair<String, String>> list = Lists.newLinkedList();
	// Table name must always be first!
	list.add(Pair.of(keys.get(0), getTableName()));
	list.add(Pair.of(keys.get(1), "" + getTransaktionId()));
	list.add(Pair.of(keys.get(2), "" + getKundeName()));
	String string = (this.getFarbIds().or(FluentIterable.from(ImmutableList.<Long> of()))).toString();
	list.add(Pair.of(keys.get(3), string));
	list.add(Pair.of(keys.get(4), "" + getWickelId().or(0L)));
	list.add(Pair.of(keys.get(5), getErgebnis()));
	list.add(Pair.of(keys.get(6), Boolean.toString(isBereitsEingetragen())));
	return FluentIterable.from(list);
    }

    private Rezeptur(final Long entityId) {
	super(entityId, TABLENAME);
    }

    public Rezeptur(final long transaktionId, String kundeName, Optional<FluentIterable<Long>> farbIds, Optional<Long> wickelId, final String ergebnis,
	    final boolean bereitsEingetragen) {
	super(TABLENAME);
	setTransaktionId(transaktionId);
	_kundeName = kundeName;
	setFarbIds(farbIds);
	setWickelId(wickelId);
	setErgebnis(ergebnis);
	setBereitsEingetragen(bereitsEingetragen);
    }

    public Rezeptur(Long entityId, final long transaktionId, String kundeName, Optional<FluentIterable<Long>> farbIds, Optional<Long> wickelId,
	    final String ergebnis,
	    final boolean bereitsEingetragen) {
	super(entityId, TABLENAME);
	setTransaktionId(transaktionId);
	_kundeName = kundeName;
	setFarbIds(farbIds);
	setWickelId(wickelId);
	setErgebnis(ergebnis);
	setBereitsEingetragen(bereitsEingetragen);
    }

    public static final Optional<Rezeptur> loadById(final long entityId) {
	return loadFromTemplate(entityId, new Rezeptur(entityId), TABLENAME, keys);
    }

    public static final Iterable<Rezeptur> loadByParameter(final String parameter, final String value) {
	return loadFromParameter(parameter, value, TABLENAME, new Rezeptur(0L), keys, Optional.<Ordering> absent());
    }

    public static final Iterable<Rezeptur> loadByParameter(final String parameter, final String value, final Ordering orderBy) {
	return loadFromParameter(parameter, value, TABLENAME, new Rezeptur(0L), keys, Optional.of(orderBy));
    }

    public static final Iterable<Rezeptur> loadByParameterStartsWith(final String parameter, final String startsWith, final Ordering orderBy) {
	return loadFromParameterStartsWith(parameter, startsWith, TABLENAME, new Rezeptur(0L), keys, Optional.of(orderBy));
    }

    public static final Iterable<Rezeptur> loadByParameterStartsWith(final String parameter, final String startsWith) {
	return loadFromParameterStartsWith(parameter, startsWith, TABLENAME, new Rezeptur(0L), keys, Optional.<Ordering> absent());
    }

    public final Try<Rezeptur> build(final Pair<Long, Iterable<Pair<String, String>>> context) {
	return Try.of(new Supplier<Rezeptur>() {
	    @Override
	    public Rezeptur get() {
		Rezeptur rezeptur = new Rezeptur(context._1);
		ImmutableList<Pair<String, String>> values = ImmutableList.copyOf(FluentIterable.from(context._2).filter(Predicates.withoutSecond(TABLENAME)));

		rezeptur.setTransaktionId(Long.parseLong(values.get(0)._2));
		rezeptur.setKundeName(values.get(1)._2);
		String farbIds = values.get(2)._2.replace("[", "");
		farbIds = farbIds.replace("]", "");
		FluentIterable<String> farben = FluentIterable.from(ImmutableList.copyOf(farbIds.split(",")));
		if(farben.isEmpty()){
		    rezeptur.setFarbIds(Optional.<FluentIterable<Long>> absent());
		} else {
		    rezeptur.setFarbIds(Optional.of(farben.transform(Functions.toLong)));
		}
		long wickelId = Long.parseLong(values.get(3)._2);
		if (wickelId == 0) {
		    rezeptur.setWickelId(Optional.<Long> absent());
		} else {
		    rezeptur.setWickelId(Optional.of(wickelId));
		}
		rezeptur.setErgebnis(values.get(4)._2);
		rezeptur.setBereitsEingetragen(Boolean.parseBoolean(values.get(5)._2));
		return rezeptur;
	    }
	});
    }

    public long getTransaktionId() {
	return _transaktionId;
    }

    public void setTransaktionId(long transaktionId) {
	_transaktionId = transaktionId;
    }

    public Optional<FluentIterable<Long>> getFarbIds() {
	return _farbIds;
    }

    public void setFarbIds(Optional<FluentIterable<Long>> farbIds) {
	_farbIds = farbIds;
    }

    public Optional<Long> getWickelId() {
	return _wickelId;
    }

    public void setWickelId(Optional<Long> wickelId) {
	_wickelId = wickelId;
    }

    public String getErgebnis() {
	return _ergebnis;
    }

    public void setErgebnis(String ergebnis) {
	_ergebnis = ergebnis;
    }

    public String getKundeName() {
	return _kundeName;
    }

    public void setKundeName(String kundeName) {
	_kundeName = kundeName;
    }

    public boolean isBereitsEingetragen() {
	return _bereitsEingetragen;
    }

    public void setBereitsEingetragen(boolean bereitsEingetragen) {
	_bereitsEingetragen = bereitsEingetragen;
    }

}
