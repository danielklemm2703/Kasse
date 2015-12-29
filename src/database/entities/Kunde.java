package database.entities;

import java.util.LinkedList;

import org.joda.time.DateTime;

import util.Pair;
import util.Predicates;
import util.Try;
import datameer.com.google.common.base.Optional;
import datameer.com.google.common.base.Supplier;
import datameer.com.google.common.collect.FluentIterable;
import datameer.com.google.common.collect.ImmutableList;
import datameer.com.google.common.collect.Lists;

public class Kunde extends Entity implements Buildable<Kunde> {

    // Kunde: Id,
    // Vorname,nachname,strasse,Ort,Plz,Geburtsdatum,Telmobil,TelPrivat,aktiv
    private String _vorname;
    private String _nachname;
    private String _strasse;
    private String _ort;
    private String _postleitzahl;
    private DateTime _geburtstag;
    private String _telefonPrivat;
    private String _telefonMobil;
    private static final ImmutableList<String> keys = ImmutableList.<String> builder()
    //
	    .add("TABLENAME")

	    .add("VORNAME")

	    .add("NACHNAME")

	    .add("STRASSE")

	    .add("ORT")

	    .add("POSTLEITZAHL")

	    .add("GEBURTSTAG")

	    .add("TELEFON_PRIVAT")

	    .add("TELEFON_MOBIL")

	    .build();
    public static final String TABLENAME = Kunde.class.getSimpleName();
   

    @Override
    public Iterable<Pair<String, String>> persistenceContext() {
	LinkedList<Pair<String, String>> list = Lists.newLinkedList();
	// Table name must always be first!
	list.add(Pair.of(keys.get(0), this.getTableName()));
	list.add(Pair.of(keys.get(1), this.getVorname()));
	list.add(Pair.of(keys.get(2), this.getNachname()));
	list.add(Pair.of(keys.get(3), this.getStrasse()));
	list.add(Pair.of(keys.get(4), this.getOrt()));
	list.add(Pair.of(keys.get(5), this.getPostleitzahl()));
	list.add(Pair.of(keys.get(6), this.getGeburtstag().toString()));
	list.add(Pair.of(keys.get(7), this.getTelefonPrivat()));
	list.add(Pair.of(keys.get(8), this.getTelefonMobil()));
	return FluentIterable.from(list);
    }

    private Kunde(final Long entityId) {
	super(entityId, TABLENAME);
    }

    public Kunde(final String vorname, final String nachname, final String strasse, final String ort, final String postleitzahl, final DateTime geburtstag,
	    final String telefonPrivat, final String telefonMobil) {
	super(TABLENAME);
	setVorname(vorname);
	setNachname(nachname);
	setStrasse(strasse);
	setOrt(ort);
	setPostleitzahl(postleitzahl);
	setGeburtstag(geburtstag);
	setTelefonPrivat(telefonPrivat);
	setTelefonMobil(telefonMobil);
    }

    public Kunde(Long entityId, final String vorname, final String nachname, final String strasse, final String ort, final String postleitzahl,
	    final DateTime geburtstag, final String telefonPrivat, final String telefonMobil) {
	super(entityId, TABLENAME);
	setVorname(vorname);
	setNachname(nachname);
	setStrasse(strasse);
	setOrt(ort);
	setPostleitzahl(postleitzahl);
	setGeburtstag(geburtstag);
	setTelefonPrivat(telefonPrivat);
	setTelefonMobil(telefonMobil);
    }

    public static final Optional<Kunde> loadById(final long entityId) {
	return loadFromTemplate(entityId, new Kunde(entityId), TABLENAME, keys);
    }

    public static final Iterable<Kunde> loadByParameter(final String parameter, final String value) {
	return loadFromParameter(parameter, value, TABLENAME, new Kunde(0L), keys);
    }

    public final Try<Kunde> build(final Pair<Long, Iterable<Pair<String, String>>> context) {
	return Try.of(new Supplier<Kunde>() {
	    @Override
	    public Kunde get() {
		Kunde kunde = new Kunde(context._1);
		ImmutableList<Pair<String, String>> values = ImmutableList.copyOf(FluentIterable.from(context._2).filter(Predicates.withoutSecond(TABLENAME)));
		kunde.setVorname(values.get(0)._2);
		kunde.setNachname(values.get(1)._2);
		kunde.setStrasse(values.get(2)._2);
		kunde.setOrt(values.get(3)._2);
		kunde.setPostleitzahl(values.get(4)._2);
		kunde.setGeburtstag(DateTime.parse(values.get(5)._2));
		kunde.setTelefonPrivat(values.get(6)._2);
		kunde.setTelefonMobil(values.get(7)._2);
		return kunde;
	    }
	});
    }

    public String getVorname() {
	return _vorname;
    }

    public void setVorname(String vorname) {
	_vorname = vorname;
    }

    public String getNachname() {
	return _nachname;
    }

    public void setNachname(String nachname) {
	_nachname = nachname;
    }

    public String getStrasse() {
	return _strasse;
    }

    public void setStrasse(String strasse) {
	_strasse = strasse;
    }

    public String getOrt() {
	return _ort;
    }

    public void setOrt(String ort) {
	_ort = ort;
    }

    public String getPostleitzahl() {
	return _postleitzahl;
    }

    public void setPostleitzahl(String postleitzahl) {
	_postleitzahl = postleitzahl;
    }

    public DateTime getGeburtstag() {
	return _geburtstag;
    }

    public void setGeburtstag(DateTime geburtstag) {
	_geburtstag = geburtstag;
    }

    public String getTelefonPrivat() {
	return _telefonPrivat;
    }

    public void setTelefonPrivat(String telefonPrivat) {
	_telefonPrivat = telefonPrivat;
    }

    public String getTelefonMobil() {
	return _telefonMobil;
    }

    public void setTelefonMobil(String telefonMobil) {
	_telefonMobil = telefonMobil;
    }
}
