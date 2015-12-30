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
    private Optional<FluentIterable<Long>> _farbIds;
    private Optional<Long> _wickelId;
    private String _ergebnis;
    private boolean _bereitsEingetragen;

    private static final ImmutableList<String> keys = ImmutableList.<String> builder()
    //
	    .add("TABLENAME")

	    .add("TRANSAKTION-ID")

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
	list.add(Pair.of(keys.get(0), this.getTableName()));
	list.add(Pair.of(keys.get(1), "" + this.getTransaktionId()));
	// TODO remove later
	String string = (this.getFarbIds().or(FluentIterable.from(ImmutableList.<Long> of()))).toString();
	System.err.println(string);
	list.add(Pair.of(keys.get(2), string));
	list.add(Pair.of(keys.get(3), "" + this.getWickelId().or(0L)));
	list.add(Pair.of(keys.get(4), this.getErgebnis()));
	list.add(Pair.of(keys.get(5), Boolean.toString(this.isBereitsEingetragen())));
	return FluentIterable.from(list);
    }

    private Rezeptur(final Long entityId) {
	super(entityId, TABLENAME);
    }

    public Rezeptur(final long transaktionId, Optional<FluentIterable<Long>> farbIds, Optional<Long> wickelId, final String ergebnis,
	    final boolean bereitsEingetragen) {
	super(TABLENAME);
	setTransaktionId(transaktionId);
	setFarbIds(farbIds);
	setWickelId(wickelId);
	setErgebnis(ergebnis);
	setBereitsEingetragen(bereitsEingetragen);
    }

    public Rezeptur(Long entityId, final long transaktionId, Optional<FluentIterable<Long>> farbIds, Optional<Long> wickelId, final String ergebnis,
	    final boolean bereitsEingetragen) {
	super(entityId, TABLENAME);
	setTransaktionId(transaktionId);
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

    public final Try<Rezeptur> build(final Pair<Long, Iterable<Pair<String, String>>> context) {
	return Try.of(new Supplier<Rezeptur>() {
	    @Override
	    public Rezeptur get() {
		Rezeptur rezeptur = new Rezeptur(context._1);
		ImmutableList<Pair<String, String>> values = ImmutableList.copyOf(FluentIterable.from(context._2).filter(Predicates.withoutSecond(TABLENAME)));

		rezeptur.setTransaktionId(Long.parseLong(values.get(0)._2));
		FluentIterable<String> farben = FluentIterable.from(ImmutableList.copyOf(values.get(1)._2.split(",")));
		if(farben.isEmpty()){
		    rezeptur.setFarbIds(Optional.<FluentIterable<Long>> absent());
		} else {
		    rezeptur.setFarbIds(Optional.of(farben.transform(Functions.toLong)));
		}
		long wickelId = Long.parseLong(values.get(2)._2);
		if (wickelId == 0) {
		    rezeptur.setWickelId(Optional.<Long> absent());
		} else {
		    rezeptur.setWickelId(Optional.of(wickelId));
		}
		rezeptur.setErgebnis(values.get(3)._2);
		rezeptur.setBereitsEingetragen(Boolean.parseBoolean(values.get(4)._2));
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

    public boolean isBereitsEingetragen() {
	return _bereitsEingetragen;
    }

    public void setBereitsEingetragen(boolean bereitsEingetragen) {
	_bereitsEingetragen = bereitsEingetragen;
    }

}
