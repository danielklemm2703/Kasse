package database.entities;

import java.util.LinkedList;

import util.Pair;
import util.Predicates;
import util.Preis;
import util.Try;
import datameer.com.google.common.base.Optional;
import datameer.com.google.common.base.Supplier;
import datameer.com.google.common.collect.FluentIterable;
import datameer.com.google.common.collect.ImmutableList;
import datameer.com.google.common.collect.Lists;

public class Dienstleistung extends Entity {

    private String _dienstleistungsName;
    private long _kategorieId;
    private Preis _preis;
    private boolean _rezepturplichtig;
    private static final ImmutableList<String> keys = ImmutableList.<String> builder().add("TABLENAME").add("NAME").add("KATEGORIE_ID").add("PREIS")
	    .add("REZEPTURPFLICHTIG").build();
    private static final String TABLENAME = Dienstleistung.class.getSimpleName();

    @Override
    public Iterable<Pair<String, String>> persistenceContext() {
	LinkedList<Pair<String, String>> list = Lists.newLinkedList();
	// Table name must always be first!
	list.add(Pair.of(keys.get(0), this.getTableName()));
	list.add(Pair.of(keys.get(1), this.getDienstleistungsName()));
	list.add(Pair.of(keys.get(2), "" + this.getKategorieId()));
	list.add(Pair.of(keys.get(3), this.getPreis().toString()));
	list.add(Pair.of(keys.get(4), Boolean.toString(this.isRezepturplichtig())));
	return FluentIterable.from(list);
    }

    public Dienstleistung(final String name, final long kategorieId, final Preis preis, final boolean rezepturpflichtig) {
	super(TABLENAME);
	_dienstleistungsName = name;
	_kategorieId = kategorieId;
	_preis = preis;
	_rezepturplichtig = rezepturpflichtig;
    }

    public Dienstleistung(final long entityId, final String name, final long kategorieId, final Preis preis, final boolean rezepturpflichtig) {
	super(entityId, TABLENAME);
	_dienstleistungsName = name;
	_kategorieId = kategorieId;
	_preis = preis;
	_rezepturplichtig = rezepturpflichtig;
    }

    Dienstleistung(Long entityId) {
	super(entityId, TABLENAME);
    }

    public Preis getPreis() {
	return _preis;
    }

    public void setPreis(final Preis preis) {
	_preis = preis;
    }
    
    public void setPreis(final Double preis) {
	_preis = Preis.of(preis);
    }
    
    public void setPreis(final String preis) {
	_preis = Preis.of(preis);
    }

    public String getDienstleistungsName() {
	return _dienstleistungsName;
    }

    public void setDienstleistungsName(String kategorieName) {
	_dienstleistungsName = kategorieName;
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
		dienstleistung.setDienstleistungsName(values.get(0)._2);
		dienstleistung.setKategorieId(Long.parseLong(values.get(1)._2));
		dienstleistung.setPreis(Preis.of(values.get(2)._2));
		dienstleistung.setRezepturplichtig(Boolean.parseBoolean(values.get(3)._2));
		return dienstleistung;
	    }
	});
    }

    public long getKategorieId() {
	return _kategorieId;
    }

    public void setKategorieId(long kategorieId) {
	_kategorieId = kategorieId;
    }

    public boolean isRezepturplichtig() {
	return _rezepturplichtig;
    }

    public void setRezepturplichtig(boolean rezepturplichtig) {
	_rezepturplichtig = rezepturplichtig;
    }
}
