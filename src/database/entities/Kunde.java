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

public class Kunde extends Entity implements Buildable<Kunde> {

    private String _vorname;
    private String _nachname;
    private long _ortId;
    private String _telefon;

    private static final ImmutableList<String> keys = ImmutableList.<String> builder()
    //
	    .add("TABLENAME")

	    .add("VORNAME")

	    .add("NACHNAME")

	    .add("ORT_ID")

	    .add("TELEFON")

	    .build();

    public static final String TABLENAME = Kunde.class.getSimpleName();

    @Override
    public Iterable<Pair<String, String>> persistenceContext() {
	LinkedList<Pair<String, String>> list = Lists.newLinkedList();
	// Table name must always be first!
	list.add(Pair.of(keys.get(0), this.getTableName()));
	list.add(Pair.of(keys.get(1), this.getVorname()));
	list.add(Pair.of(keys.get(2), this.getNachname()));
	list.add(Pair.of(keys.get(3), "" + this.getOrtId()));
	list.add(Pair.of(keys.get(4), this.getTelefon()));
	return FluentIterable.from(list);
    }

    private Kunde(final Long entityId) {
	super(entityId, TABLENAME);
    }

    public Kunde(final String vorname, final String nachname, final long ortId, final String telefon) {
	super(TABLENAME);
	setVorname(vorname);
	setNachname(nachname);
	setOrtId(ortId);
	setTelefon(telefon);
    }

    public Kunde(Long entityId, final String vorname, final String nachname, final long ortId, final String telefon) {
	super(entityId, TABLENAME);
	setVorname(vorname);
	setNachname(nachname);
	setOrtId(ortId);
	setTelefon(telefon);
    }

    public static final Optional<Kunde> loadById(final long entityId) {
	return loadFromTemplate(entityId, new Kunde(entityId), TABLENAME, keys);
    }

    public static final Iterable<Kunde> loadByParameter(final String parameter, final String value) {
	return loadFromParameter(parameter, value, TABLENAME, new Kunde(0L), keys, Optional.<Ordering> absent());
    }

    public static final Iterable<Kunde> loadByParameter(final String parameter, final String value, final Ordering orderBy) {
	return loadFromParameter(parameter, value, TABLENAME, new Kunde(0L), keys, Optional.of(orderBy));
    }

    public static final Iterable<Kunde> loadByParameterStartsWith(final String parameter, final String startsWith, final Ordering orderBy) {
	return loadFromParameterStartsWith(parameter, startsWith, TABLENAME, new Kunde(0L), keys, Optional.of(orderBy));
    }

    public static final Iterable<Kunde> loadByParameterStartsWith(final String parameter, final String startsWith) {
	return loadFromParameterStartsWith(parameter, startsWith, TABLENAME, new Kunde(0L), keys, Optional.<Ordering> absent());
    }

    public final Try<Kunde> build(final Pair<Long, Iterable<Pair<String, String>>> context) {
	return Try.of(new Supplier<Kunde>() {
	    @Override
	    public Kunde get() {
		Kunde kunde = new Kunde(context._1);
		ImmutableList<Pair<String, String>> values = ImmutableList.copyOf(FluentIterable.from(context._2).filter(Predicates.withoutSecond(TABLENAME)));
		kunde.setVorname(values.get(0)._2);
		kunde.setNachname(values.get(1)._2);
		kunde.setOrtId(Long.parseLong(values.get(2)._2));
		kunde.setTelefon(values.get(3)._2);
		return kunde;
	    }
	});
    }

    public String getVorname() {
	return _vorname;
    }

    public void setVorname(String vorname) {
	_vorname = vorname;
    }

    public String getNachname() {
	return _nachname;
    }

    public void setNachname(String nachname) {
	_nachname = nachname;
    }

    public long getOrtId() {
	return _ortId;
    }

    public void setOrtId(long ortId) {
	_ortId = ortId;
    }

    public String getTelefon() {
	return _telefon;
    }

    public void setTelefon(String telefon) {
	_telefon = telefon;
    }
}
