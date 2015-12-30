package backend;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class WickelStaerkeTest {

    @Test
    public void test() {
	WickelStaerke staerke = WickelStaerke.of("HUGO");
	assertEquals("n.V.", staerke.toString());
	assertEquals(WickelStaerke.NOT_FOUND, staerke);

	staerke = WickelStaerke.of("F");
	assertEquals("F", staerke.toString());
	assertEquals(WickelStaerke.F, staerke);

	staerke = WickelStaerke.of("G");
	assertEquals("G", staerke.toString());
	assertEquals(WickelStaerke.G, staerke);

	staerke = WickelStaerke.of("N");
	assertEquals("N", staerke.toString());
	assertEquals(WickelStaerke.N, staerke);
    }

}
