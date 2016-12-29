package database.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTime;
import org.junit.Test;

import util.Try;
import util.Unit;
import database.Ordering;
import database.enums.WickelStaerke;
import database.enums.WickelTyp;
import datameer.com.google.common.base.Optional;
import datameer.com.google.common.collect.FluentIterable;

public class WickelTest {

    @Test
    public void testSaveNewEntity() {
	Wickel wickel = new Wickel(112L, WickelTyp.DAUERWELL, WickelStaerke.F, "rot", 112L, false);
	Try<Long> save = wickel.save();
	assertEquals(true, save.isSuccess());
	assertEquals(wickel.getEntityId().get(), save.get());
    }

    @Test
    public void testUpdateExistingEntity() {
	Wickel Wickel = new Wickel(112L, WickelTyp.DAUERWELL, WickelStaerke.F, "rot", 112L, false);
	Try<Long> save = Wickel.save();
	Long entityId = save.get();
	Wickel = new Wickel(entityId, 112L, WickelTyp.DAUERWELL, WickelStaerke.F, "rot", 112L, false);
	save = Wickel.save();
	assertEquals(save.get(), Wickel.getEntityId().get());
    }

    @Test
    public void testLoadExistingEntity() {
	Wickel wickel = new Wickel(112L, WickelTyp.DAUERWELL, WickelStaerke.F, "rot", 112L, false);
	Try<Long> save = wickel.save();
	Long entityId = save.get();
	Optional<Wickel> loadById = Wickel.loadById(entityId);
	assertEquals(true, loadById.isPresent());
	assertEquals(112L, loadById.get().getEinwirkZeit());
    }

    @Test
    public void testDeleteExistingEntity() {
	Wickel wickel = new Wickel(112L, WickelTyp.DAUERWELL, WickelStaerke.F, "rot", 112L, false);
	Try<Long> save = wickel.save();
	Long entityId = save.get();
	Try<Unit> delete = wickel.delete();
	assertEquals(true, delete.isSuccess());
	Optional<Wickel> loadById = Wickel.loadById(entityId);
	assertEquals(false, loadById.isPresent());
    }

    @Test
    public void testDeleteNotExistingEntity() {
	Wickel wickel = new Wickel(112L, WickelTyp.DAUERWELL, WickelStaerke.F, "rot", 112L, false);
	Try<Unit> delete = wickel.delete();
	assertEquals(false, delete.isSuccess());
    }

    @Test
    public void testloadByParameter() {
	String now = DateTime.now().toString();
	FluentIterable<Wickel> loadByParameter = FluentIterable.from(Wickel.loadByParameter("ID", "1"));
	assertEquals(false, loadByParameter.isEmpty());
	loadByParameter = FluentIterable.from(Wickel.loadByParameter("DAUTUUM", now));
	assertEquals(true, loadByParameter.isEmpty());
    }

    @Test
    public void testLoadByParameterWithOrdering() {
	new Wickel(112L, WickelTyp.DAUERWELL, WickelStaerke.F, "rot", 112L, false).save();
	new Wickel(112L, WickelTyp.DAUERWELL, WickelStaerke.F, "zrot", 112L, false).save();
	FluentIterable<Wickel> load = FluentIterable.from(Wickel.loadByParameter("WICKELTYP", WickelTyp.DAUERWELL.toString(), new Ordering("WICKELFARBE",
		"DESC")));
	assertEquals(false, load.isEmpty());
	assertEquals("zrot", load.first().get().getWickelFarbe());
    }

    @Test
    public void testLoadByParameterNotEveryEntry() {
	double random = Math.random();
	new Wickel(112L, WickelTyp.DAUERWELL, WickelStaerke.F, "" + random, 112L, false).save();

	FluentIterable<Wickel> load = FluentIterable.from(Wickel.loadByParameter("WICKELFARBE", "" + random));
	assertEquals(false, load.isEmpty());
	assertEquals(1, load.size());
    }

    @Test
    public void testLoadByParameterEveryEntry() {
	new Wickel(112L, WickelTyp.DAUERWELL, WickelStaerke.F, "rot", 112L, false).save();
	FluentIterable<Wickel> load = FluentIterable.from(Wickel.loadByParameter("'1'", "1"));
	assertEquals(false, load.isEmpty());
	int size = load.size();
	new Wickel(112L, WickelTyp.DAUERWELL, WickelStaerke.F, "rot", 112L, false).save();
	load = FluentIterable.from(Wickel.loadByParameter("'1'", "1"));
	assertEquals(size + 1, load.size());
    }

    @Test
    public void testLoadByParameterStartsWith() {
	new Wickel(112L, WickelTyp.DAUERWELL, WickelStaerke.F, "rot", 112L, false).save();

	FluentIterable<Wickel> load = FluentIterable.from(Wickel.loadByParameterStartsWith("WICKELTYP", "D"));
	assertEquals(false, load.isEmpty());
	for (Wickel wickel : load) {
	    assertTrue(wickel.getWickelTyp().toString().startsWith("D"));
	}
    }

    @Test
    public void testLoadByParameterStartsWith_Ordering() {
	new Wickel(112L, WickelTyp.DAUERWELL, WickelStaerke.F, "rot", 112L, false).save();

	FluentIterable<Wickel> load = FluentIterable.from(Wickel.loadByParameterStartsWith("WICKELTYP", "D", new Ordering("WICKELTYP", "ASC")));
	assertEquals(false, load.isEmpty());
	assertEquals(WickelTyp.DAUERWELL.toString(), load.first().get().getWickelTyp().toString());
	for (Wickel wickel : load) {
	    assertTrue(wickel.getWickelTyp().toString().startsWith("D"));
	}
    }
}
