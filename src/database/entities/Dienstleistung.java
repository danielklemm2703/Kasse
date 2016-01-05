package database.entities;

import java.util.LinkedList;

import util.Pair;
import util.Predicates;
import util.Preis;
import util.Try;
import database.Ordering;
import datameer.com.google.common.base.Optional;
import datameer.com.google.common.base.Supplier;
import datameer.com.google.common.collect.FluentIterable;
import datameer.com.google.common.collect.ImmutableList;
import datameer.com.google.common.collect.Lists;

public class Dienstleistung extends Entity implements Buildable<Dienstleistung> {

    private static final ImmutableList<String> keys = ImmutableList.<String> builder().add("TABLENAME").add("NAME").add("KATEGORIE_ID").add("PREIS")
	    .add("REZEPTURPFLICHTIG").build();
    public static final String TABLENAME = Dienstleistung.class.getSimpleName();
    private String _dienstleistungsName;
    private long _kategorieId;
    private Preis _preis;
    private boolean _rezepturplichtig;

    public Dienstleistung(final long entityId, final String name, final long kategorieId, final Preis preis, final boolean rezepturpflichtig) {
	super(entityId, TABLENAME);
	_dienstleistungsName = name;
	_kategorieId = kategorieId;
	_preis = preis;
	_rezepturplichtig = rezepturpflichtig;
    }

    Dienstleistung(final Long entityId) {
	super(entityId, TABLENAME);
    }

    public Dienstleistung(final String name, final long kategorieId, final Preis preis, final boolean rezepturpflichtig) {
	super(TABLENAME);
	_dienstleistungsName = name;
	_kategorieId = kategorieId;
	_preis = preis;
	_rezepturplichtig = rezepturpflichtig;
    }

    public static final Optional<Dienstleistung> loadById(final long entityId) {
	return loadFromTemplate(entityId, new Dienstleistung(entityId), TABLENAME, keys);
    }

    public static final Iterable<Dienstleistung> loadByParameter(final String parameter, final String value) {
	return loadFromParameter(parameter, value, TABLENAME, new Dienstleistung(0L), keys, Optional.<Ordering> absent());
    }

    public static final Iterable<Dienstleistung> loadByParameter(final String parameter, final String value, final Ordering orderBy) {
	return loadFromParameter(parameter, value, TABLENAME, new Dienstleistung(0L), keys, Optional.of(orderBy));
    }

    public static final Iterable<Dienstleistung> loadByParameterStartsWith(final String parameter, final String startsWith, final Ordering orderBy) {
	return loadFromParameterStartsWith(parameter, startsWith, TABLENAME, new Dienstleistung(0L), keys, Optional.of(orderBy));
    }

    public static final Iterable<Dienstleistung> loadByParameterStartsWith(final String parameter, final String startsWith) {
	return loadFromParameterStartsWith(parameter, startsWith, TABLENAME, new Dienstleistung(0L), keys, Optional.<Ordering> absent());
    }

    @Override
    public final Try<Dienstleistung> build(final Pair<Long, Iterable<Pair<String, String>>> context) {
	return Try.of(new Supplier<Dienstleistung>() {
	    @Override
	    public Dienstleistung get() {
		Dienstleistung dienstleistung = new Dienstleistung(context._1);
		ImmutableList<Pair<String, String>> values = ImmutableList.copyOf(FluentIterable.from(context._2).filter(Predicates.withoutSecond(TABLENAME)));
		dienstleistung.setDienstleistungsName(values.get(0)._2);
		dienstleistung.setKategorieId(Long.parseLong(values.get(1)._2));
		dienstleistung.setPreis(Preis.of(values.get(2)._2));
		dienstleistung.setRezepturplichtig(Boolean.parseBoolean(values.get(3)._2));
		return dienstleistung;
	    }
	});
    }

    public String getDienstleistungsName() {
	return _dienstleistungsName;
    }

    public long getKategorieId() {
	return _kategorieId;
    }

    public Preis getPreis() {
	return _preis;
    }

    public boolean isRezepturplichtig() {
	return _rezepturplichtig;
    }

    @Override
    public Iterable<Pair<String, String>> persistenceContext() {
	LinkedList<Pair<String, String>> list = Lists.newLinkedList();
	// Table name must always be first!
	list.add(Pair.of(keys.get(0), getTableName()));
	list.add(Pair.of(keys.get(1), getDienstleistungsName()));
	list.add(Pair.of(keys.get(2), "" + getKategorieId()));
	list.add(Pair.of(keys.get(3), getPreis().toString()));
	list.add(Pair.of(keys.get(4), Boolean.toString(isRezepturplichtig())));
	return FluentIterable.from(list);
    }

    public void setDienstleistungsName(final String kategorieName) {
	_dienstleistungsName = kategorieName;
    }

    public void setKategorieId(final long kategorieId) {
	_kategorieId = kategorieId;
    }

    public void setPreis(final Double preis) {
	_preis = Preis.of(preis);
    }

    public void setPreis(final Preis preis) {
	_preis = preis;
    }

    public void setPreis(final String preis) {
	_preis = Preis.of(preis);
    }

    public void setRezepturplichtig(final boolean rezepturplichtig) {
	_rezepturplichtig = rezepturplichtig;
    }
}
