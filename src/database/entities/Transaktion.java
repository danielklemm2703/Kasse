package database.entities;

import java.util.LinkedList;

import org.joda.time.DateTime;

import util.Functions;
import util.Pair;
import util.Predicates;
import util.Preis;
import util.Try;
import database.Ordering;
import datameer.com.google.common.base.Function;
import datameer.com.google.common.base.Optional;
import datameer.com.google.common.base.Supplier;
import datameer.com.google.common.collect.FluentIterable;
import datameer.com.google.common.collect.ImmutableList;
import datameer.com.google.common.collect.Lists;

public class Transaktion extends Entity implements Buildable<Transaktion> {

    private FluentIterable<DienstleistungsInfo> _dienstleistungInfos;
    private FluentIterable<VerkaufsInfo> _verkaufInfos;
    private DateTime _datum;
    // abzüglich der eingelösten Gutschein Summe
    private Optional<Preis> _gutscheinStartwert;
    private Optional<Gutschein> _gutscheinEingeloest;
    // = abzüglich gutscheinwert
    private Preis _gesamtUmsatz;

    private static final ImmutableList<String> keys = ImmutableList.<String> builder()
    //
	    .add("TABLENAME")

	    .add("DIENSTLEISTUNG_INFO_IDS")

	    .add("VERKAUF_INFO_IDS")

	    .add("DATUM")

	    .add("GUTSCHEIN_STARTWERT")

	    .add("GUTSCHEIN_EINGELOEST_ID")

	    .add("GESAMT_UMSATZ")

	    .build();

    public static final String TABLENAME = Transaktion.class.getSimpleName();

    @Override
    public Iterable<Pair<String, String>> persistenceContext() {
	LinkedList<Pair<String, String>> list = Lists.newLinkedList();
	// Table name must always be first!
	list.add(Pair.of(keys.get(0), this.getTableName()));
	list.add(Pair.of(keys.get(1), _dienstleistungInfos.transform(Functions.toEntityId)
	//
		.filter(Predicates.<Long> isPresent())

		.transform(Functions.<Long> get())

		.toString()));
	list.add(Pair.of(keys.get(2), _verkaufInfos.transform(Functions.toEntityId)
	//
		.filter(Predicates.<Long> isPresent())

		.transform(Functions.<Long> get())

		.toString()));
	list.add(Pair.of(keys.get(3), _datum.toString()));
	list.add(Pair.of(keys.get(4), _gutscheinStartwert.or(Preis.of(0L)).toString()));
	list.add(Pair.of(keys.get(5), _gutscheinEingeloest.transform(new Function<Gutschein, String>() {
	    @Override
	    public String apply(Gutschein input) {
		return input.getEntityId().or(-1L).toString();
	    }
	}).or("-1").toString()));
	list.add(Pair.of(keys.get(6), _gesamtUmsatz.toString()));
	return FluentIterable.from(list);
    }

    private Transaktion(final Long entityId) {
	super(entityId, TABLENAME);
    }

    public Transaktion(FluentIterable<DienstleistungsInfo> dienstleistungInfo, FluentIterable<VerkaufsInfo> verkaufInfo, DateTime datum,
	    Optional<Preis> gutscheinStartwert, Optional<Gutschein> gutscheinEingeloest, Preis gesamtUmsatz) {
	super(TABLENAME);
	_dienstleistungInfos = dienstleistungInfo;
	_verkaufInfos = verkaufInfo;
	_datum = datum;
	_gutscheinStartwert = gutscheinStartwert;
	_gutscheinEingeloest = gutscheinEingeloest;
	_gesamtUmsatz = gesamtUmsatz;
    }

    public Transaktion(Long entityId, FluentIterable<DienstleistungsInfo> dienstleistungInfo, FluentIterable<VerkaufsInfo> verkaufInfos,
	    DateTime datum, Optional<Preis> gutscheinStartwert, Optional<Gutschein> gutscheinEingeloest, Preis gesamtUmsatz) {
	super(entityId, TABLENAME);
	_dienstleistungInfos = dienstleistungInfo;
	_verkaufInfos = verkaufInfos;
	_datum = datum;
	_gutscheinStartwert = gutscheinStartwert;
	_gutscheinEingeloest = gutscheinEingeloest;
	_gesamtUmsatz = gesamtUmsatz;
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

    private static final Function<Long, Optional<DienstleistungsInfo>> loadDienstleistungInfo = new Function<Long, Optional<DienstleistungsInfo>>() {
	@Override
	public Optional<DienstleistungsInfo> apply(Long input) {
	    Optional<DienstleistungsInfo> output = DienstleistungsInfo.loadById(input);
	    if (!output.isPresent()) {
		System.err.println("could not find DienstleistungsInfo " + input);
	    }
	    return output;
	}
    };

    private static final Function<Long, Optional<VerkaufsInfo>> loadVerkaufInfo = new Function<Long, Optional<VerkaufsInfo>>() {
	@Override
	public Optional<VerkaufsInfo> apply(Long input) {
	    Optional<VerkaufsInfo> output = VerkaufsInfo.loadById(input);
	    if (!output.isPresent()) {
		System.err.println("could not find VerkaufsInfo " + input);
	    }
	    return output;
	}
    };

    public final Try<Transaktion> build(final Pair<Long, Iterable<Pair<String, String>>> context) {
	return Try.of(new Supplier<Transaktion>() {
	    @Override
	    public Transaktion get() {
		Transaktion transaktion = new Transaktion(context._1);
		ImmutableList<Pair<String, String>> values = ImmutableList.copyOf(FluentIterable.from(context._2).filter(Predicates.withoutSecond(TABLENAME)));

		String dienstleistungInfoIds = values.get(0)._2.replace("[", "");
		dienstleistungInfoIds = dienstleistungInfoIds.replace("]", "");
		String[] dienstleistungen = dienstleistungInfoIds.split(",");
		if (dienstleistungen.length == 1 && dienstleistungen[0].equals("")) {
		    System.err.println("error case");
		}

		FluentIterable<DienstleistungsInfo> deinstleistungInfos = FluentIterable.of(dienstleistungen)
		//
			.transform(Functions.toLong)

			.transform(loadDienstleistungInfo)

			.filter(Predicates.<DienstleistungsInfo> isPresent())

			.transform(Functions.<DienstleistungsInfo> get());
		transaktion.setDienstleistungInfos(deinstleistungInfos);

		String verkaufInfoIds = values.get(1)._2.replace("[", "");
		verkaufInfoIds = verkaufInfoIds.replace("]", "");
		String[] verkaeufe = verkaufInfoIds.split(",");
		FluentIterable<VerkaufsInfo> verkaufInfos = FluentIterable.of(verkaeufe)
		//
			.transform(Functions.toLong)

			.transform(loadVerkaufInfo)

			.filter(Predicates.<VerkaufsInfo> isPresent())

			.transform(Functions.<VerkaufsInfo> get());
		transaktion.setVerkaufInfos(verkaufInfos);

		transaktion.setDatum(DateTime.parse(values.get(2)._2));
		transaktion.setGutscheinStartwert(Optional.of(Preis.of(values.get(3)._2)));

		long entityId = Long.parseLong(values.get(4)._2);
		Optional<Gutschein> gutscheinEingeloest = Gutschein.loadById(entityId);
		transaktion.setGutscheinEingeloest(gutscheinEingeloest);
		transaktion.setGesamtUmsatz(Preis.of(values.get(5)._2));
		return transaktion;
	    }
	});
    }

    public DateTime getDatum() {
	return _datum;
    }

    public void setDatum(DateTime datum) {
	_datum = datum;
    }

    public Optional<Preis> getGutscheinStartwert() {
	return _gutscheinStartwert;
    }

    public void setGutscheinStartwert(Optional<Preis> gutscheinStartwert) {
	_gutscheinStartwert = gutscheinStartwert;
    }

    public Optional<Gutschein> getGutscheinEingeloest() {
	return _gutscheinEingeloest;
    }

    public void setGutscheinEingeloest(Optional<Gutschein> gutscheinEingeloest) {
	_gutscheinEingeloest = gutscheinEingeloest;
    }

    public FluentIterable<DienstleistungsInfo> getDienstleistungInfos() {
	return _dienstleistungInfos;
    }

    public void setDienstleistungInfos(FluentIterable<DienstleistungsInfo> dienstleistungInfos) {
	_dienstleistungInfos = dienstleistungInfos;
    }

    public FluentIterable<VerkaufsInfo> getVerkaufInfos() {
	return _verkaufInfos;
    }

    public void setVerkaufInfos(FluentIterable<VerkaufsInfo> verkaufInfos) {
	_verkaufInfos = verkaufInfos;
    }

    public Preis getGesamtUmsatz() {
	return _gesamtUmsatz;
    }

    public void setGesamtUmsatz(Preis gesamtUmsatz) {
	_gesamtUmsatz = gesamtUmsatz;
    }
}
