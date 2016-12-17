package database.enums;

public enum WickelTyp {
    DAUERWELL("Dauerwellwickel"), VOLUMENWELL("Volumenwellwickel"), NOT_FOUND("keine Angabe");

    private final String _typ;

    private WickelTyp(String typ) {
	_typ = typ;
    }

    public static WickelTyp of(final String inputTyp) {
	for (WickelTyp typ : WickelTyp.values()) {
	    if (inputTyp.equals(typ.toString())) {
		return typ;
	    }
	}
	return NOT_FOUND;
    }

    @Override
    public String toString() {
	return _typ;
    }
}
