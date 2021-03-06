package database.enums;

public enum FrameType {

    REZEPTUREN("Rezepturen"), KUNDENDATEN("Kundendaten"), CHECK("Check"), NOTIFICATION("Notification"), KASSE("Kasse"), EIGENVERBRAUCH("Eigenverbrauch"), KUNDE(
	    "Kunde"), ADMIN("Admin"), NOT_FOUND("Not found"), DIENSTLEISTUNG_CHOSER("Dienstleistung choser"), VERKAUF_CHOSER("Verkauf choser"), KASSE_FINISH(
	    "Kasse finish");

    private final String _name;

    private FrameType(String name) {
	_name = name;
    }

    public static FrameType of(final String name) {
	for (FrameType type : FrameType.values()) {
	    if (name.equals(type.toString())) {
		return type;
	    }
	}
	return NOT_FOUND;
    }

    @Override
    public String toString() {
	return _name;
    }
}
