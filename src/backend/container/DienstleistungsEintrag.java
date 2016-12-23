package backend.container;

import database.entities.Dienstleistung;
import database.entities.Friseur;
import database.entities.Kunde;
import datameer.com.google.common.base.Optional;

public class DienstleistungsEintrag {

    private Dienstleistung _dienstleistung;
    private Friseur _friseur;
    private Optional<Kunde> _kunde;

    public DienstleistungsEintrag(Dienstleistung dienstleistung, Friseur friseur, Optional<Kunde> kunde) {
	_dienstleistung = dienstleistung;
	_friseur = friseur;
	_kunde = kunde;
    }

    public Dienstleistung getDienstleistung() {
	return _dienstleistung;
    }

    public Friseur getFriseur() {
	return _friseur;
    }

    public Optional<Kunde> getKunde() {
	return _kunde;
    }
}
