package database.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTime;
import org.junit.Test;

import util.Preis;
import util.Try;
import util.Unit;
import database.Ordering;
import datameer.com.google.common.base.Optional;
import datameer.com.google.common.collect.FluentIterable;

public class VerkaufTest {

    @Test
    public void testSaveNewEntity() {
	Verkauf verkauf = new Verkauf("Haare schneiden", 2, Preis.of("22,22"));
	Try<Long> save = verkauf.save();
	assertEquals(true, save.isSuccess());
	assertEquals(verkauf.getEntityId().get(), save.get());
    }

    @Test
    public void testUpdateExistingEntity() {
	Verkauf verkauf = new Verkauf("Haare schneiden", 2, Preis.of("22,22"));
	Try<Long> save = verkauf.save();
	Long entityId = save.get();
	verkauf = new Verkauf(entityId, "Haare schneiden", 2, Preis.of("22,22"));
	save = verkauf.save();
	assertEquals(save.get(), verkauf.getEntityId().get());
    }

    @Test
    public void testLoadExistingEntity() {
	Verkauf verkauf = new Verkauf("Haare schneiden", 2, Preis.of("22,22"));
	Try<Long> save = verkauf.save();
	Long entityId = save.get();
	Optional<Verkauf> loadById = Verkauf.loadById(entityId);
	assertEquals(true, loadById.isPresent());
	assertEquals("Haare schneiden", loadById.get().getVerkaufsName());
    }

    @Test
    public void testDeleteExistingEntity() {
	Verkauf verkauf = new Verkauf("Haare schneiden", 2, Preis.of("22,22"));
	Try<Long> save = verkauf.save();
	Long entityId = save.get();
	Try<Unit> delete = verkauf.delete();
	assertEquals(true, delete.isSuccess());
	Optional<Verkauf> loadById = Verkauf.loadById(entityId);
	assertEquals(false, loadById.isPresent());
    }

    @Test
    public void testDeleteNotExistingEntity() {
	Verkauf verkauf = new Verkauf("Haare schneiden", 2, Preis.of("22,22"));
	Try<Unit> delete = verkauf.delete();
	assertEquals(false, delete.isSuccess());
    }

    @Test
    public void testloadByParameter() {
	String now = DateTime.now().toString();
	FluentIterable<Verkauf> loadByParameter = FluentIterable.from(Verkauf.loadByParameter("ID", "1"));
	assertEquals(false, loadByParameter.isEmpty());
	loadByParameter = FluentIterable.from(Verkauf.loadByParameter("DAUTUUM", now));
	assertEquals(true, loadByParameter.isEmpty());
    }

    @Test
    public void testLoadByParameterWithOrdering() {
	new Verkauf("Haare schneiden", 2, Preis.of("22,22")).save();
	new Verkauf("ZhHaare schneiden", 2, Preis.of("22,22")).save();
	FluentIterable<Verkauf> load = FluentIterable.from(Verkauf.loadByParameter("NAME", "ZhHaare schneiden", new Ordering("NAME", "DESC")));
	assertEquals(false, load.isEmpty());
	assertEquals("ZhHaare schneiden", load.first().get().getVerkaufsName());
    }

    @Test
    public void testLoadByParameterNotEveryEntry() {
	Preis preis = Preis.of(Math.random());
	new Verkauf("Haare schneiden", 2, preis).save();

	FluentIterable<Verkauf> load = FluentIterable.from(Verkauf.loadByParameter("PREIS", preis.toString()));
	assertEquals(false, load.isEmpty());
	assertEquals(1, load.size());
    }

    @Test
    public void testLoadByParameterEveryEntry() {
	new Verkauf("Haare schneiden", 2, Preis.of("22,22")).save();
	FluentIterable<Verkauf> load = FluentIterable.from(Verkauf.loadByParameter("'1'", "1"));
	assertEquals(false, load.isEmpty());
	int size = load.size();
	new Verkauf("Haare schneiden", 2, Preis.of("22,22")).save();
	load = FluentIterable.from(Verkauf.loadByParameter("'1'", "1"));
	assertEquals(size + 1, load.size());
    }

    @Test
    public void testLoadByParameterStartsWith() {
	new Verkauf("Haare schneiden", 2, Preis.of("22,22")).save();

	FluentIterable<Verkauf> load = FluentIterable.from(Verkauf.loadByParameterStartsWith("NAME", "H"));
	assertEquals(false, load.isEmpty());
	for (Verkauf verkauf : load) {
	    assertTrue(verkauf.getVerkaufsName().startsWith("H"));
	}
    }

    @Test
    public void testLoadByParameterStartsWith_Ordering() {
	new Verkauf("Haaare schneiden", 2, Preis.of("22,22")).save();

	FluentIterable<Verkauf> load = FluentIterable.from(Verkauf.loadByParameterStartsWith("NAME", "H", new Ordering("NAME", "ASC")));
	assertEquals(false, load.isEmpty());
	assertEquals("Haaare schneiden", load.first().get().getVerkaufsName());
	for (Verkauf verkauf : load) {
	    assertTrue(verkauf.getVerkaufsName().startsWith("H"));
	}
    }
}
