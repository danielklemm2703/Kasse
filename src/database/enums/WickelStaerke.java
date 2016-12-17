package database.enums;

public enum WickelStaerke {
    F("F"), G("G"), N("N"), NOT_FOUND("n.V.");

    private final String _staerke;

    private WickelStaerke(String typ) {
	_staerke = typ;
    }

    public static WickelStaerke of(final String inputTyp) {
	for (WickelStaerke typ : WickelStaerke.values()) {
	    if (inputTyp.equals(typ.toString())) {
		return typ;
	    }
	}
	return NOT_FOUND;
    }

    @Override
    public String toString() {
	return _staerke;
    }
}
