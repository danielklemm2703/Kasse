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

public class VerkaufsInfo extends Entity implements Buildable<VerkaufsInfo> {
    private String _kundeName;
    private String _friseurName;
    private String _verkaufName;
    private Preis _preis;

    private static final ImmutableList<String> keys = ImmutableList.<String> builder()
    //
	    .add("TABLENAME")

	    .add("KUNDE_NAME")

	    .add("FRISEUR_NAME")

	    .add("VERKAUF_NAME")

	    .add("PREIS")

	    .build();

    public static final String TABLENAME = VerkaufsInfo.class.getSimpleName();

    private VerkaufsInfo(final Long entityId) {
	super(entityId, TABLENAME);
    }

    public VerkaufsInfo(final String kundeName, final String friseurName, final String verkaufName, final Preis preis) {
	super(TABLENAME);
	_kundeName = kundeName;
	_friseurName = friseurName;
	_verkaufName = verkaufName;
	_preis = preis;
    }

    public VerkaufsInfo(Long entityId, final String kundeName, final String friseurName, final String verkaufName, final Preis preis) {
	super(entityId, TABLENAME);
	_kundeName = kundeName;
	_friseurName = friseurName;
	_verkaufName = verkaufName;
	_preis = preis;
    }

    @Override
    public Try<VerkaufsInfo> build(final Pair<Long, Iterable<Pair<String, String>>> context) {
	return Try.of(new Supplier<VerkaufsInfo>() {
	    @Override
	    public VerkaufsInfo get() {
		VerkaufsInfo verkaufsInfo = new VerkaufsInfo(context._1);
		ImmutableList<Pair<String, String>> values = ImmutableList.copyOf(FluentIterable.from(context._2).filter(Predicates.withoutSecond(TABLENAME)));
		verkaufsInfo.setKundeName(values.get(0)._2);
		verkaufsInfo.setFriseurName(values.get(1)._2);
		verkaufsInfo.setVerkaufName(values.get(2)._2);
		verkaufsInfo.setPreis(Preis.of(values.get(3)._2));
		return verkaufsInfo;
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
	list.add(Pair.of(keys.get(3), getVerkaufName()));
	list.add(Pair.of(keys.get(4), getPreis().toString()));
	return FluentIterable.from(list);
    }

    public static final Optional<VerkaufsInfo> loadById(final long entityId) {
	return loadFromTemplate(entityId, new VerkaufsInfo(entityId), TABLENAME, keys);
    }

    public static final Iterable<VerkaufsInfo> loadByParameter(final String parameter, final String value) {
	return loadFromParameter(parameter, value, TABLENAME, new VerkaufsInfo(0L), keys, Optional.<Ordering> absent());
    }

    public static final Iterable<VerkaufsInfo> loadByParameter(final String parameter, final String value, final Ordering orderBy) {
	return loadFromParameter(parameter, value, TABLENAME, new VerkaufsInfo(0L), keys, Optional.of(orderBy));
    }

    public static final Iterable<VerkaufsInfo> loadByParameterStartsWith(final String parameter, final String startsWith, final Ordering orderBy) {
	return loadFromParameterStartsWith(parameter, startsWith, TABLENAME, new VerkaufsInfo(0L), keys, Optional.of(orderBy));
    }

    public static final Iterable<VerkaufsInfo> loadByParameterStartsWith(final String parameter, final String startsWith) {
	return loadFromParameterStartsWith(parameter, startsWith, TABLENAME, new VerkaufsInfo(0L), keys, Optional.<Ordering> absent());
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

    public String getVerkaufName() {
	return _verkaufName;
    }

    public void setVerkaufName(String verkaufName) {
	_verkaufName = verkaufName;
    }

    public Preis getPreis() {
	return _preis;
    }

    public void setPreis(Preis preis) {
	_preis = preis;
    }
}
