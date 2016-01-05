package database.entities;

import java.util.LinkedList;

import org.joda.time.DateTime;

import util.Functions;
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

public class Transaktion extends Entity implements Buildable<Transaktion> {

    private long _kundeId;
    private FluentIterable<Long> _dienstleistungsIds;
    private FluentIterable<Long> _verkaufIds;
    private DateTime _datum;
    private long _friseurId;
    // abzüglich der eingelösten Gutschein Summe
    private Preis _preis;
    // wenn true -> kundeId = 0
    private boolean _laufkunde;
    private Optional<Preis> _gutscheinStartwert;
    private Optional<Long> _gutscheinIdEingeloest;
    private Optional<Long> _gutscheinIdGekauft;

    private static final ImmutableList<String> keys = ImmutableList.<String> builder()
    //
	    .add("TABLENAME")

	    .add("KUNDE_ID")

	    .add("DIENSTLEISTUNGS_IDS")

	    .add("VERKAUFS_IDS")

	    .add("DATUM")

	    .add("FRISEUR_ID")

	    .add("LAUFKUNDE")

	    .add("GUTSCHEIN_STARTWERT")

	    .add("GUTSCHEIN_EINGELOEST_ID")

	    .add("GUTSCHEIN_GEKAUFT_ID")

	    .build();

    public static final String TABLENAME = Transaktion.class.getSimpleName();

    @Override
    public Iterable<Pair<String, String>> persistenceContext() {
	LinkedList<Pair<String, String>> list = Lists.newLinkedList();
	// Table name must always be first!
	list.add(Pair.of(keys.get(0), this.getTableName()));
	list.add(Pair.of(keys.get(1), "" + this.getKundeId()));
	// TODO remove later
	String string = this.getDienstleistungsIds().toString();
	System.err.println(string);
	list.add(Pair.of(keys.get(2), string));
	list.add(Pair.of(keys.get(3), this.getVerkaufIds().toString()));
	list.add(Pair.of(keys.get(4), this.getDatum().toString()));
	list.add(Pair.of(keys.get(5), ""+this.getFriseurId()));
	list.add(Pair.of(keys.get(6), Boolean.toString(this.isLaufkunde())));
	list.add(Pair.of(keys.get(7), (this.getGutscheinStartwert().or(Preis.of(0D))).toString()));
	list.add(Pair.of(keys.get(8), "" + this.getGutscheinIdEingeloest().or(0L)));
	list.add(Pair.of(keys.get(9), "" + this.getGutscheinIdGekauft().or(0L)));
	return FluentIterable.from(list);
    }

    private Transaktion(final Long entityId) {
	super(entityId, TABLENAME);
    }

    public Transaktion(final long kundeId, final FluentIterable<Long> dienstleistungsIds, final FluentIterable<Long> verkaufIds, final DateTime datum,
	    final long friseurId, final boolean laufkunde, Optional<Preis> gutscheinStartwert, Optional<Long> gutscheinIdEingeloest,
	    Optional<Long> gutscheinIdGekauft) {
	super(TABLENAME);
	setKundeId(kundeId);
	setDienstleistungsIds(dienstleistungsIds);
	setVerkaufIds(verkaufIds);
	setDatum(datum);
	setFriseurId(friseurId);
	setLaufkunde(laufkunde);
	setGutscheinStartwert(gutscheinStartwert);
	setGutscheinIdEingeloest(gutscheinIdEingeloest);
	setGutscheinIdGekauft(gutscheinIdGekauft);
    }

    public Transaktion(Long entityId, final long kundeId, final FluentIterable<Long> dienstleistungsIds, final FluentIterable<Long> verkaufIds,
	    final DateTime datum,
	    final long friseurId, final boolean laufkunde, Optional<Preis> gutscheinStartwert, Optional<Long> gutscheinIdEingeloest,
	    Optional<Long> gutscheinIdGekauft) {
	super(entityId, TABLENAME);
	setKundeId(kundeId);
	setDienstleistungsIds(dienstleistungsIds);
	setVerkaufIds(verkaufIds);
	setDatum(datum);
	setFriseurId(friseurId);
	setLaufkunde(laufkunde);
	setGutscheinStartwert(gutscheinStartwert);
	setGutscheinIdEingeloest(gutscheinIdEingeloest);
	setGutscheinIdGekauft(gutscheinIdGekauft);
    }

    public static final Optional<Transaktion> loadById(final long entityId) {
	return loadFromTemplate(entityId, new Transaktion(entityId), TABLENAME, keys);
    }

    public static final Iterable<Transaktion> loadByParameter(final String parameter, final String value) {
	return loadFromParameter(parameter, value, TABLENAME, new Transaktion(0L), keys, Optional.<Ordering> absent());
    }

    public static final Iterable<Transaktion> loadByParameter(final String parameter, final String value, final Ordering orderBy) {
	return loadFromParameter(parameter, value, TABLENAME, new Transaktion(0L), keys, Optional.of(orderBy));
    }

    public static final Iterable<Transaktion> loadByParameterStartsWith(final String parameter, final String startsWith, final Ordering orderBy) {
	return loadFromParameterStartsWith(parameter, startsWith, TABLENAME, new Transaktion(0L), keys, Optional.of(orderBy));
    }

    public static final Iterable<Transaktion> loadByParameterStartsWith(final String parameter, final String startsWith) {
	return loadFromParameterStartsWith(parameter, startsWith, TABLENAME, new Transaktion(0L), keys, Optional.<Ordering> absent());
    }

    public final Try<Transaktion> build(final Pair<Long, Iterable<Pair<String, String>>> context) {
	return Try.of(new Supplier<Transaktion>() {
	    @Override
	    public Transaktion get() {
		Transaktion transaktion = new Transaktion(context._1);
		ImmutableList<Pair<String, String>> values = ImmutableList.copyOf(FluentIterable.from(context._2).filter(Predicates.withoutSecond(TABLENAME)));
		transaktion.setKundeId(Long.parseLong(values.get(0)._2));

		String dienstleistungsIds = values.get(1)._2.replace("[", "");
		dienstleistungsIds = dienstleistungsIds.replace("]", "");
		String[] dienstleistungen = dienstleistungsIds.split(",");
		transaktion.setDienstleistungsIds(FluentIterable.of(dienstleistungen).transform(Functions.toLong));

		String verkaufsIds = values.get(2)._2.replace("[", "");
		verkaufsIds = verkaufsIds.replace("]", "");
		String[] verkaeufe = verkaufsIds.split(",");
		transaktion.setVerkaufIds(FluentIterable.of(verkaeufe).transform(Functions.toLong));

		transaktion.setDatum(DateTime.parse(values.get(3)._2));
		transaktion.setFriseurId(Long.parseLong(values.get(4)._2));
		transaktion.setLaufkunde(Boolean.parseBoolean(values.get(5)._2));
		transaktion.setGutscheinStartwert(Optional.of(Preis.of(values.get(6)._2)));
		transaktion.setGutscheinIdEingeloest(Optional.of(Long.parseLong(values.get(7)._2)));
		transaktion.setGutscheinIdGekauft(Optional.of(Long.parseLong(values.get(8)._2)));
		return transaktion;
	    }
	});
    }

    public long getKundeId() {
	return _kundeId;
    }

    public void setKundeId(long kundeId) {
	_kundeId = kundeId;
    }

    public FluentIterable<Long> getDienstleistungsIds() {
	return _dienstleistungsIds;
    }

    public void setDienstleistungsIds(FluentIterable<Long> dienstleistungsIds) {
	_dienstleistungsIds = dienstleistungsIds;
    }

    public FluentIterable<Long> getVerkaufIds() {
	return _verkaufIds;
    }

    public void setVerkaufIds(FluentIterable<Long> verkaufIds) {
	_verkaufIds = verkaufIds;
    }

    public DateTime getDatum() {
	return _datum;
    }

    public void setDatum(DateTime datum) {
	_datum = datum;
    }

    public long getFriseurId() {
	return _friseurId;
    }

    public void setFriseurId(long friseurId) {
	_friseurId = friseurId;
    }

    public Preis getPreis() {
	return _preis;
    }

    public void setPreis(Preis preis) {
	_preis = preis;
    }

    public boolean isLaufkunde() {
	return _laufkunde;
    }

    public void setLaufkunde(boolean laufkunde) {
	_laufkunde = laufkunde;
    }

    public Optional<Preis> getGutscheinStartwert() {
	return _gutscheinStartwert;
    }

    public void setGutscheinStartwert(Optional<Preis> gutscheinStartwert) {
	_gutscheinStartwert = gutscheinStartwert;
    }

    public Optional<Long> getGutscheinIdEingeloest() {
	return _gutscheinIdEingeloest;
    }

    public void setGutscheinIdEingeloest(Optional<Long> gutscheinIdEingeloest) {
	_gutscheinIdEingeloest = gutscheinIdEingeloest;
    }

    public Optional<Long> getGutscheinIdGekauft() {
	return _gutscheinIdGekauft;
    }

    public void setGutscheinIdGekauft(Optional<Long> gutscheinIdGekauft) {
	_gutscheinIdGekauft = gutscheinIdGekauft;
    }

}
