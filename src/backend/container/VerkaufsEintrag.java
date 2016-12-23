package backend.container;

import database.entities.Friseur;
import database.entities.Gutschein;
import database.entities.Kunde;
import database.entities.Verkauf;
import datameer.com.google.common.base.Optional;

public class VerkaufsEintrag {

    private Optional<Verkauf> _verkauf;
    private Optional<Gutschein> _gutschein;
    private Friseur _friseur;
    private Optional<Kunde> _kunde;

    public VerkaufsEintrag(Verkauf verkauf, Friseur friseur, Optional<Kunde> kunde) {
	_verkauf = Optional.of(verkauf);
	_friseur = friseur;
	_kunde = kunde;
	_gutschein = Optional.absent();
    }

    public VerkaufsEintrag(Gutschein gutschein, Friseur friseur, Optional<Kunde> kunde) {
	_verkauf = Optional.absent();
	_friseur = friseur;
	_kunde = kunde;
	_gutschein = Optional.of(gutschein);
    }

    public boolean isGutschein() {
	return _gutschein.isPresent();
    }

    public boolean isVerkauf() {
	return _verkauf.isPresent();
    }

    public Gutschein getGutscheon() {
	return _gutschein.get();
    }

    public Verkauf getVerkauf() {
	return _verkauf.get();
    }

    public Friseur getFriseur() {
	return _friseur;
    }

    public Optional<Kunde> getKunde() {
	return _kunde;
    }
}
