package util;

import java.text.DecimalFormat;

public class Preis {
    private double _preis;
    private static final DecimalFormat FORMAT = new DecimalFormat("#0.00");

    private Preis(final double preis) {
	_preis = preis;
    }

    public static final Preis of(final double preis) {
	double toFormat = ((double) Math.round(preis * 100)) / 100;
	return new Preis(toFormat);
    }

    public static final Preis of(final String input) {
	String parsed = input.replace(",", ".");
	parsed = parsed.replace("EUR", "");
	double preis = Double.parseDouble(parsed);
	double toFormat = ((double) Math.round(preis * 100)) / 100;
	return new Preis(toFormat);
    }

    public String toString(){
	return FORMAT.format(_preis) + " EUR";
    }

    public double value() {
	return _preis;
    }
}
