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

public class Kategorie extends Entity implements Buildable<Kategorie> {
    // Kategorie: Id,Name,Dienstleisung(bool),Verkauf(bool),aktiv
    private String _kategorieName;
    private boolean _dienstleistungsKategorie;
    private boolean _verkaufsKategorie;
    private static final ImmutableList<String> keys = ImmutableList.<String> builder().add("TABLENAME").add("KATEGORIE_NAME").add("DIENSTLEISTUNGS_KATEGORIE")
	    .add("VERKAUFS_KATEGORIE").build();
    public static final String TABLENAME = Kategorie.class.getSimpleName();

    @Override
    public Iterable<Pair<String, String>> persistenceContext() {
	LinkedList<Pair<String, String>> list = Lists.newLinkedList();
	// Table name must always be first!
	list.add(Pair.of(keys.get(0), this.getTableName()));
	list.add(Pair.of(keys.get(1), this.getKategorieName()));
	list.add(Pair.of(keys.get(2), Boolean.toString(this.isDienstleistungsKategorie())));
	list.add(Pair.of(keys.get(3), Boolean.toString(this.isVerkaufsKategorie())));
	return FluentIterable.from(list);
    }

    public Kategorie(final String kategorieName, final boolean dienstleistungsKategorie, final boolean verkaufsKategorie) {
	super(TABLENAME);
	setKategorieName(kategorieName);
	setDienstleistungsKategorie(dienstleistungsKategorie);
	setVerkaufsKategorie(verkaufsKategorie);
    }

    public Kategorie(final long entityId, final String kategorieName, final boolean dienstleistungsKategorie, final boolean verkaufsKategorie) {
	super(entityId, TABLENAME);
	setKategorieName(kategorieName);
	setDienstleistungsKategorie(dienstleistungsKategorie);
	setVerkaufsKategorie(verkaufsKategorie);
    }

    Kategorie(final Long entityId) {
	super(entityId, TABLENAME);
    }

    public String getKategorieName() {
	return _kategorieName;
    }

    public void setKategorieName(String kategorieName) {
	_kategorieName = kategorieName;
    }

    public boolean isDienstleistungsKategorie() {
	return _dienstleistungsKategorie;
    }

    public void setDienstleistungsKategorie(boolean dienstleistungsKategorie) {
	_dienstleistungsKategorie = dienstleistungsKategorie;
    }

    public boolean isVerkaufsKategorie() {
	return _verkaufsKategorie;
    }

    public void setVerkaufsKategorie(boolean verkaufsKategorie) {
	_verkaufsKategorie = verkaufsKategorie;
    }

    public static final Optional<Kategorie> loadById(final long entityId) {
	return loadFromTemplate(entityId, new Kategorie(entityId), TABLENAME, keys);
    }

    public static final Iterable<Kategorie> loadByParameter(final String parameter, final String value) {
	return loadFromParameter(parameter, value, TABLENAME, new Kategorie(0L), keys, Optional.<Ordering> absent());
    }

    public static final Iterable<Kategorie> loadByParameter(final String parameter, final String value, final Ordering orderBy) {
	return loadFromParameter(parameter, value, TABLENAME, new Kategorie(0L), keys, Optional.of(orderBy));
    }

    public Try<Kategorie> build(final Pair<Long, Iterable<Pair<String, String>>> context) {
	return Try.of(new Supplier<Kategorie>() {
	    @Override
	    public Kategorie get() {
		Kategorie kategorie = new Kategorie(context._1);
		ImmutableList<Pair<String, String>> values = ImmutableList.copyOf(FluentIterable.from(context._2).filter(Predicates.withoutSecond(TABLENAME)));
		kategorie.setKategorieName(values.get(0)._2);
		kategorie.setDienstleistungsKategorie(Boolean.parseBoolean(values.get(1)._2));
		kategorie.setVerkaufsKategorie(Boolean.parseBoolean(values.get(2)._2));
		return kategorie;
	    }
	});
    }
}
