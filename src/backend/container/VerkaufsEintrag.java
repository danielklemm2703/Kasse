package backend.container;

import database.entities.Friseur;
import database.entities.Kunde;
import database.entities.Verkauf;
import datameer.com.google.common.base.Optional;

public class VerkaufsEintrag {

    private Verkauf _verkauf;
    private Friseur _friseur;
    private Optional<Kunde> _kunde;

    public VerkaufsEintrag(Verkauf verkauf, Friseur friseur, Optional<Kunde> kunde) {
	_verkauf = verkauf;
	_friseur = friseur;
	_kunde = kunde;
    }

    public Verkauf getVerkauf() {
	return _verkauf;
    }

    public Friseur getFriseur() {
	return _friseur;
    }

    public Optional<Kunde> getKunde() {
	return _kunde;
    }
}
