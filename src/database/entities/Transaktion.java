package database.entities;

public class Transaktion extends Entity implements Buildable<Transaktion> {
    // Transaktion: Id, KundeId,DiensleistungenIds[],VerkaufIds[],Datum,
    // FriseurId,Preis(AbzüglichEingelösterGutschein,inklusivegekaufterGutschein),
    // aktiv,laufkunde(bool true->kundeId
    // ==0),GutscheinStartwert,GutscheinIdEingelöst,GutscheinIdGekauft
}
