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

public class Gutschein extends Entity {

    private long _transaktionId;
    private long _kundeId;
    private Preis _restWert;
    private static final ImmutableList<String> keys = ImmutableList.<String> builder().add("TABLENAME").add("TRANSAKTION_ID").add("KUNDE_ID").add("RESTWERT")
	    .build();
    private static final String TABLENAME = Gutschein.class.getSimpleName();

    @Override
    public Iterable<Pair<String, String>> persistenceContext() {
	LinkedList<Pair<String, String>> list = Lists.newLinkedList();
	// Table name must always be first!
	list.add(Pair.of(keys.get(0), this.getTableName()));
	list.add(Pair.of(keys.get(1), "" + this.getTransaktionId()));
	list.add(Pair.of(keys.get(2), "" + this.getKundeId()));
	list.add(Pair.of(keys.get(3), this.getRestWert().toString()));
	return FluentIterable.from(list);
    }

    public Gutschein(final long transaktionId, final long kundeId, final Preis restWert) {
	super(TABLENAME);
	_transaktionId = transaktionId;
	_kundeId = kundeId;
	_restWert = restWert;
    }

    public Gutschein(final long entityId, final long transaktionId, final long kundeId, final Preis restWert) {
	super(entityId, TABLENAME);
	_transaktionId = transaktionId;
	_kundeId = kundeId;
	_restWert = restWert;
    }

    Gutschein(Long entityId) {
	super(entityId, TABLENAME);
    }

    public long getTransaktionId() {
	return _transaktionId;
    }

    public void setTransaktionId(long transaktionId) {
	_transaktionId = transaktionId;
    }

    public long getKundeId() {
	return _kundeId;
    }

    public void setKundeId(long kundeId) {
	_kundeId = kundeId;
    }

    public Preis getRestWert() {
	return _restWert;
    }

    public void setRestWert(Preis restWert) {
	_restWert = restWert;
    }

    public static final Optional<Gutschein> loadById(long entityId) {
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
	Try<Gutschein> gutschein = build(context);
	if (gutschein.isFailure()) {
	    System.err.println(String.format("could not parse Entity with id '%s', from table '%s', reason:", "" + entityId, TABLENAME));
	    System.err.println(gutschein.failure().getLocalizedMessage());
	    return Optional.absent();
	}
	return Optional.of(gutschein.get());
    }

    private static final Try<Gutschein> build(final Pair<Long, Iterable<Pair<String, String>>> context) {
	return Try.of(new Supplier<Gutschein>() {
	    @Override
	    public Gutschein get() {
		Gutschein gutschein = new Gutschein(context._1);
		ImmutableList<Pair<String, String>> values = ImmutableList.copyOf(FluentIterable.from(context._2).filter(Predicates.withoutSecond(TABLENAME)));
		gutschein.setTransaktionId(Long.parseLong(values.get(0)._2));
		gutschein.setKundeId(Long.parseLong(values.get(1)._2));
		gutschein.setRestWert(Preis.of(values.get(2)._2));
		return gutschein;
	    }
	});
    }
}
