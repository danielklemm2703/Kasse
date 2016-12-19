package database.util;

import java.awt.Color;

import util.Pair;
import util.table.NonEditColorableTableModel;
import util.table.NonEditableColumnTableModel;
import database.Ordering;
import database.entities.Dienstleistung;
import database.entities.Kategorie;
import database.entities.Kunde;
import database.entities.Ort;
import datameer.com.google.common.base.Optional;
import datameer.com.google.common.collect.ImmutableMap;
import datameer.com.google.common.collect.ImmutableMap.Builder;

public class TableDatas {

    private static final NonEditColorableTableModel createEmptyDienstleistungModel() {
	NonEditColorableTableModel model = new NonEditColorableTableModel();
	model.addColumn("Dienstleistung");
	model.addColumn("Preis");
	return model;
    }

    private static final NonEditableColumnTableModel createEmptyKundenModel() {
	NonEditableColumnTableModel model = new NonEditableColumnTableModel();
	model.addColumn("Nachname");
	model.addColumn("Vorname");
	model.addColumn("Ort");
	model.addColumn("Telefon");
	return model;
    }

    public static final Pair<NonEditColorableTableModel, ImmutableMap<Integer, Optional<Dienstleistung>>> loadDienstleistungChoserData() {
	NonEditColorableTableModel model = createEmptyDienstleistungModel();
	Iterable<Kategorie> dienstleistungsKategorien = Kategorie.loadByParameter("DIENSTLEISTUNGS_KATEGORIE", "true");
	ImmutableMap.Builder<Integer, Optional<Dienstleistung>> aggregate = ImmutableMap.<Integer, Optional<Dienstleistung>> builder();
	int index = 0;
	for (Kategorie kategorie : dienstleistungsKategorien) {
	    model.addRow(new Object[] { kategorie.getKategorieName(), "" }, Color.lightGray);
	    aggregate.put(index, Optional.<Dienstleistung> absent());
	    index++;
	    Iterable<Dienstleistung> dienstleistungenZuKategorie = Dienstleistung.loadByParameter("KATEGORIE_ID", "" + kategorie.getEntityId().get(),
		    new Ordering("NAME", "ASC"));
	    for (Dienstleistung dienstleistung : dienstleistungenZuKategorie) {
		model.addRow(new Object[] { dienstleistung.getDienstleistungsName(), dienstleistung.getPreis().toString() });
		aggregate.put(index, Optional.of(dienstleistung));
		index++;
	    }
	}
	return Pair.of(model, aggregate.build());
    }

    public static final Pair<NonEditableColumnTableModel, ImmutableMap<Integer, Kunde>> loadKundeData(final String letter) {
	Iterable<Kunde> kunden = Kunde.loadByParameterStartsWith("NACHNAME", letter, new Ordering("NACHNAME", "ASC"));
	NonEditableColumnTableModel model = createEmptyKundenModel();
	Builder<Integer, Kunde> aggregate = ImmutableMap.<Integer, Kunde> builder();
	int index = 0;
	for (Kunde kunde : kunden) {
	    model.addRow(new Object[] { kunde.getNachname(), kunde.getVorname(), Ort.loadById(kunde.getOrtId()).get().getOrtName(), kunde.getTelefon() });
	    aggregate.put(index++, kunde);
	}
	return Pair.of(model, aggregate.build());
    }
}
