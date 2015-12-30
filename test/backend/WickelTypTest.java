package backend;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class WickelTypTest {

    @Test
    public void test() {
	WickelTyp staerke = WickelTyp.of("HUGO");
	assertEquals("keine Angabe", staerke.toString());
	assertEquals(WickelTyp.NOT_FOUND, staerke);

	staerke = WickelTyp.of("Volumenwellwickel");
	assertEquals("Volumenwellwickel", staerke.toString());
	assertEquals(WickelTyp.VOLUMENWELL, staerke);

	staerke = WickelTyp.of("Dauerwellwickel");
	assertEquals("Dauerwellwickel", staerke.toString());
	assertEquals(WickelTyp.DAUERWELL, staerke);
    }

}
