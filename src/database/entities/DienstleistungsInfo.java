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

public class DienstleistungsInfo extends Entity implements Buildable<DienstleistungsInfo> {

    private String _kundeName;
    private String _friseurName;
    private String _dienstleistungName;
    private Preis _preis;

    private static final ImmutableList<String> keys = ImmutableList.<String> builder()
    //
	    .add("TABLENAME")

	    .add("KUNDE_NAME")

	    .add("FRISEUR_NAME")

	    .add("DIENSTLEISTUNG_NAME")

	    .add("PREIS")

	    .build();

    public static final String TABLENAME = DienstleistungsInfo.class.getSimpleName();

    private DienstleistungsInfo(final Long entityId) {
	super(entityId, TABLENAME);
    }

    public DienstleistungsInfo(final String kundeName, final String friseurName, final String dienstleistungName, final Preis preis) {
	super(TABLENAME);
	_kundeName = kundeName;
	_friseurName = friseurName;
	_dienstleistungName = dienstleistungName;
	_preis = preis;
    }

    public DienstleistungsInfo(Long entityId, final String kundeName, final String friseurName, final String dienstleistungName, final Preis preis) {
	super(entityId, TABLENAME);
	_kundeName = kundeName;
	_friseurName = friseurName;
	_dienstleistungName = dienstleistungName;
	_preis = preis;
    }

    @Override
    public Try<DienstleistungsInfo> build(final Pair<Long, Iterable<Pair<String, String>>> context) {
	return Try.of(new Supplier<DienstleistungsInfo>() {
	    @Override
	    public DienstleistungsInfo get() {
		DienstleistungsInfo dienstleistungsInfo = new DienstleistungsInfo(context._1);
		ImmutableList<Pair<String, String>> values = ImmutableList.copyOf(FluentIterable.from(context._2).filter(Predicates.withoutSecond(TABLENAME)));
		dienstleistungsInfo.setKundeName(values.get(0)._2);
		dienstleistungsInfo.setFriseurName(values.get(1)._2);
		dienstleistungsInfo.setDienstleistungName(values.get(2)._2);
		dienstleistungsInfo.setPreis(Preis.of(values.get(3)._2));
		return dienstleistungsInfo;
	    }
	});
    }

    @Override
    public Iterable<Pair<String, String>> persistenceContext() {
	LinkedList<Pair<String, String>> list = Lists.newLinkedList();
	// Table name must always be first!
	list.add(Pair.of(keys.get(0), getTableName()));
	list.add(Pair.of(keys.get(1), getKundeName()));
	list.add(Pair.of(keys.get(2), getFriseurName()));
	list.add(Pair.of(keys.get(3), getDienstleistungName()));
	list.add(Pair.of(keys.get(4), getPreis().toString()));
	return FluentIterable.from(list);
    }

    public static final Optional<DienstleistungsInfo> loadById(final long entityId) {
	return loadFromTemplate(entityId, new DienstleistungsInfo(entityId), TABLENAME, keys);
    }

    public static final Iterable<DienstleistungsInfo> loadByParameter(final String parameter, final String value) {
	return loadFromParameter(parameter, value, TABLENAME, new DienstleistungsInfo(0L), keys, Optional.<Ordering> absent());
    }

    public static final Iterable<DienstleistungsInfo> loadByParameter(final String parameter, final String value, final Ordering orderBy) {
	return loadFromParameter(parameter, value, TABLENAME, new DienstleistungsInfo(0L), keys, Optional.of(orderBy));
    }

    public static final Iterable<DienstleistungsInfo> loadByParameterStartsWith(final String parameter, final String startsWith, final Ordering orderBy) {
	return loadFromParameterStartsWith(parameter, startsWith, TABLENAME, new DienstleistungsInfo(0L), keys, Optional.of(orderBy));
    }

    public static final Iterable<DienstleistungsInfo> loadByParameterStartsWith(final String parameter, final String startsWith) {
	return loadFromParameterStartsWith(parameter, startsWith, TABLENAME, new DienstleistungsInfo(0L), keys, Optional.<Ordering> absent());
    }

    public String getKundeName() {
	return _kundeName;
    }

    public void setKundeName(String kundeName) {
	_kundeName = kundeName;
    }

    public String getFriseurName() {
	return _friseurName;
    }

    public void setFriseurName(String friseurName) {
	_friseurName = friseurName;
    }

    public String getDienstleistungName() {
	return _dienstleistungName;
    }

    public void setDienstleistungName(String dienstleistungName) {
	_dienstleistungName = dienstleistungName;
    }

    public Preis getPreis() {
	return _preis;
    }

    public void setPreis(Preis preis) {
	_preis = preis;
    }
}
