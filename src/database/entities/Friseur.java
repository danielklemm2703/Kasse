package database.entities;

import java.util.LinkedList;

import util.Pair;
import util.Predicates;
import util.Try;
import datameer.com.google.common.base.Optional;
import datameer.com.google.common.base.Supplier;
import datameer.com.google.common.collect.FluentIterable;
import datameer.com.google.common.collect.ImmutableList;
import datameer.com.google.common.collect.Lists;

public class Friseur extends Entity {
    private String _friseurName;
    private static final ImmutableList<String> keys = ImmutableList.<String> builder().add("TABLENAME").add("FRISEUR_NAME").build();
    private static final String TABLENAME = Friseur.class.getSimpleName();

    @Override
    public Iterable<Pair<String, String>> persistenceContext() {
	LinkedList<Pair<String, String>> list = Lists.newLinkedList();
	// Table name must always be first!
	list.add(Pair.of(keys.get(0), this.getTableName()));
	list.add(Pair.of(keys.get(1), "" + this.getFriseurName()));
	return FluentIterable.from(list);
    }

    private Friseur(final Long entityId) {
	super(entityId, TABLENAME);
    }

    public Friseur(final String friseurName) {
	super(TABLENAME);
	_friseurName = friseurName;
    }

    public Friseur(Long entityId, final String friseurName) {
	super(entityId, TABLENAME);
	_friseurName = friseurName;
    }

    public String getFriseurName() {
	return _friseurName;
    }

    public void setFriseurName(String friseurName) {
	_friseurName = friseurName;
    }

    public static final Optional<Friseur> loadById(long entityId) {
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
	Try<Friseur> friseur = build(context);
	if (friseur.isFailure()) {
	    System.err.println(String.format("could not parse Entity with id '%s', from table '%s', reason:", "" + entityId, TABLENAME));
	    System.err.println(friseur.failure().getLocalizedMessage());
	    return Optional.absent();
	}
	return Optional.of(friseur.get());
    }

    private static final Try<Friseur> build(final Pair<Long, Iterable<Pair<String, String>>> context) {
	return Try.of(new Supplier<Friseur>() {
	    @Override
	    public Friseur get() {
		Friseur friseur = new Friseur(context._1);
		ImmutableList<Pair<String, String>> values = ImmutableList.copyOf(FluentIterable.from(context._2).filter(Predicates.withoutSecond(TABLENAME)));
		friseur.setFriseurName(values.get(0)._2);
		return friseur;
	    }
	});
    }
}
