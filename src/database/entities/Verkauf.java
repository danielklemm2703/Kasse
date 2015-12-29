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

public class Verkauf extends Entity implements Buildable<Verkauf> {
    private static final ImmutableList<String> keys = ImmutableList.<String> builder().add("TABLENAME").add("NAME").add("KATEGORIE_ID").add("PREIS").build();

    public static final String TABLENAME = Verkauf.class.getSimpleName();
    private String _verkaufsName;
    private long _kategorieId;
    private Preis _preis;

    public Verkauf(final long entityId, final String verkaufsName, final long kategorieId, final Preis preis) {
	super(entityId, TABLENAME);
	setVerkaufsName(verkaufsName);
	setKategorieId(kategorieId);
	setPreis(preis);
    }

    Verkauf(final Long entityId) {
	super(entityId, TABLENAME);
    }

    public Verkauf(final String verkaufsName, final long kategorieId, final Preis preis) {
	super(TABLENAME);
	setVerkaufsName(verkaufsName);
	setKategorieId(kategorieId);
	setPreis(preis);
    }

    public static final Optional<Verkauf> loadById(final long entityId) {
	return loadFromTemplate(entityId, new Verkauf(entityId), TABLENAME, keys);
    }

    public static final Iterable<Verkauf> loadByParameter(final String parameter, final String value) {
	return loadFromParameter(parameter, value, TABLENAME, new Verkauf(0L), keys);
    }

    @Override
    public final Try<Verkauf> build(final Pair<Long, Iterable<Pair<String, String>>> context) {
	return Try.of(new Supplier<Verkauf>() {
	    @Override
	    public Verkauf get() {
		Verkauf verkauf = new Verkauf(context._1);
		ImmutableList<Pair<String, String>> values = ImmutableList.copyOf(FluentIterable.from(context._2).filter(Predicates.withoutSecond(TABLENAME)));
		verkauf.setVerkaufsName(values.get(0)._2);
		verkauf.setKategorieId(Long.parseLong(values.get(1)._2));
		verkauf.setPreis(Preis.of(values.get(2)._2));
		return verkauf;
	    }
	});
    }

    @Override
    public Iterable<Pair<String, String>> persistenceContext() {
	LinkedList<Pair<String, String>> list = Lists.newLinkedList();
	// Table name must always be first!
	list.add(Pair.of(keys.get(0), getTableName()));
	list.add(Pair.of(keys.get(1), getVerkaufsName()));
	list.add(Pair.of(keys.get(2), "" + getKategorieId()));
	list.add(Pair.of(keys.get(3), getPreis().toString()));
	return FluentIterable.from(list);
    }

    public long getKategorieId() {
	return _kategorieId;
    }

    public void setKategorieId(long kategorieId) {
	_kategorieId = kategorieId;
    }

    public String getVerkaufsName() {
	return _verkaufsName;
    }

    public void setVerkaufsName(String verkaufsName) {
	_verkaufsName = verkaufsName;
    }

    public Preis getPreis() {
	return _preis;
    }

    public void setPreis(Preis preis) {
	_preis = preis;
    }
}
