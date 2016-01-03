package backend;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import backend.enums.FaerbeTechnik;

public class FaerbeTechnikTest {

    @Test
    public void test() {
	FaerbeTechnik technik = FaerbeTechnik.of("HUGO");
	assertEquals("keine Angabe", technik.toString());
	assertEquals(FaerbeTechnik.NOT_FOUND, technik);

	technik = FaerbeTechnik.of("Strähne");
	assertEquals("Strähne", technik.toString());
	assertEquals(FaerbeTechnik.STRAEHNE, technik);

	technik = FaerbeTechnik.of("Ansatz");
	assertEquals("Ansatz", technik.toString());
	assertEquals(FaerbeTechnik.ANSATZ, technik);
    }

}
