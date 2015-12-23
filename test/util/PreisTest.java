package util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PreisTest {

    @Test
    public void testOf() {
	Preis preis = Preis.of(12.555F);
	assertEquals("12,56 EUR", preis.toString());
	assertEquals(12.56D, preis.value(), 0);
	preis = Preis.of(12F);
	assertEquals("12,00 EUR", preis.toString());
	assertEquals(12D, preis.value(), 0);
	preis = Preis.of("12");
	assertEquals("12,00 EUR", preis.toString());
	assertEquals(12D, preis.value(), 0);
	preis = Preis.of("12.5555");
	assertEquals("12,56 EUR", preis.toString());
	assertEquals(12.56D, preis.value(), 0);
	preis = Preis.of("12,5555");
	assertEquals("12,56 EUR", preis.toString());
	assertEquals(12.56D, preis.value(), 0);
	preis = Preis.of("12,5555 EUR");
	assertEquals("12,56 EUR", preis.toString());
	assertEquals(12.56D, preis.value(), 0);
	preis = Preis.of("12,5555EUR");
	assertEquals("12,56 EUR", preis.toString());
	assertEquals(12.56D, preis.value(), 0);
    }

}
