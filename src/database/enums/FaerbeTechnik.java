package database.enums;

public enum FaerbeTechnik {
    ANSATZ("Ansatz"), STRAEHNE("Strähne"), NOT_FOUND("keine Angabe");

    private final String _name;

    private FaerbeTechnik(String name) {
	_name = name;
    }

    public static FaerbeTechnik of(final String name) {
	for (FaerbeTechnik technik : FaerbeTechnik.values()) {
	    if (name.equals(technik.toString())) {
		return technik;
	    }
	}
	return NOT_FOUND;
    }

    @Override
    public String toString() {
	return _name;
    }
}
