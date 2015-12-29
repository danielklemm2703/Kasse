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

public class Friseur extends Entity implements Buildable<Friseur> {
    private String _friseurName;
    private static final ImmutableList<String> keys = ImmutableList.<String> builder().add("TABLENAME").add("FRISEUR_NAME").build();
    public static final String TABLENAME = Friseur.class.getSimpleName();

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
	return loadFromTemplate(entityId, new Friseur(entityId), TABLENAME, keys);
    }

    public static final Iterable<Friseur> loadByParameter(final String parameter, final String value) {
	return loadFromParameter(parameter, value, TABLENAME, new Friseur(0L), keys, Optional.<Ordering> absent());
    }

    public static final Iterable<Friseur> loadByParameter(final String parameter, final String value, final Ordering orderBy) {
	return loadFromParameter(parameter, value, TABLENAME, new Friseur(0L), keys, Optional.of(orderBy));
    }

    public final Try<Friseur> build(final Pair<Long, Iterable<Pair<String, String>>> context) {
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
