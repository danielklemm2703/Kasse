package database.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import util.Preis;
import util.Try;
import util.Unit;
import database.Ordering;
import datameer.com.google.common.base.Optional;
import datameer.com.google.common.collect.FluentIterable;

public class DienstleistungTest {

    @Test
    public void testSaveNewEntity() {
	Dienstleistung dienstleistung = new Dienstleistung("Haare schneiden", 2, Preis.of("22,22"), false);
	Try<Long> save = dienstleistung.save();
	assertEquals(true, save.isSuccess());
    }

    @Test
    public void testUpdateExistingEntity() {
	Dienstleistung dienstleistung = new Dienstleistung("Haare schneiden", 2, Preis.of("22,22"), false);
	Try<Long> save = dienstleistung.save();
	Long entityId = save.get();
	dienstleistung = new Dienstleistung(entityId, "Haare schneiden", 2, Preis.of("22,22"), false);
	save = dienstleistung.save();
	assertEquals(save.get(), dienstleistung.getEntityId().get());
    }

    @Test
    public void testLoadExistingEntity() {
	Dienstleistung dienstleistung = new Dienstleistung("Haare schneiden", 2, Preis.of("22,22"), false);
	Try<Long> save = dienstleistung.save();
	Long entityId = save.get();
	Optional<Dienstleistung> loadById = Dienstleistung.loadById(entityId);
	assertEquals(true, loadById.isPresent());
	assertEquals("Haare schneiden", loadById.get().getDienstleistungsName());
    }

    @Test
    public void testDeleteExistingEntity() {
	Dienstleistung dienstleistung = new Dienstleistung("Haare schneiden", 2, Preis.of("22,22"), false);
	Try<Long> save = dienstleistung.save();
	Long entityId = save.get();
	Try<Unit> delete = Dienstleistung.delete(entityId, Dienstleistung.TABLENAME);
	assertEquals(true, delete.isSuccess());
	Optional<Dienstleistung> loadById = Dienstleistung.loadById(entityId);
	assertEquals(false, loadById.isPresent());
    }

    @Test
    public void testDeleteNotExistingEntity() {
	Try<Unit> delete = Dienstleistung.delete(1132L, Dienstleistung.TABLENAME);
	assertEquals(true, delete.isSuccess());
    }

    @Test
    public void testloadByParameter() {
	FluentIterable<Dienstleistung> loadByParameter = FluentIterable.from(Dienstleistung.loadByParameter("NAME", "Haare schneiden"));
	assertEquals(false, loadByParameter.isEmpty());
	loadByParameter = FluentIterable.from(Dienstleistung.loadByParameter("NAMER", "Haare schneiden"));
	assertEquals(true, loadByParameter.isEmpty());
    }

    @Test
    public void testLoadByParameterWithOrdering() {
	String name = "Ziemlich viele Haare abschneiden";
	new Dienstleistung(name, 1, Preis.of(122D), false).save();
	new Dienstleistung("Haare abschneiden", 1, Preis.of(122D), false).save();
	FluentIterable<Dienstleistung> load = FluentIterable.from(Dienstleistung.loadByParameter("PREIS", Preis.of(122D).toString(), new Ordering(
"NAME", "DESC")));
	assertEquals(false, load.isEmpty());
	assertEquals(name, load.first().get().getDienstleistungsName());
    }

    @Test
    public void testLoadByParameterNotEveryEntry() {
	double random = Math.random();
	new Dienstleistung("" + random, 1, Preis.of(122D), false).save();

	FluentIterable<Dienstleistung> load = FluentIterable.from(Dienstleistung.loadByParameter("NAME", "" + random));
	assertEquals(false, load.isEmpty());
	assertEquals(1, load.size());
    }

    @Test
    public void testLoadByParameterEveryEntry() {
	String name = "Ziemlich viele Haare abschneiden";
	new Dienstleistung(name, 1, Preis.of(122D), false).save();
	FluentIterable<Dienstleistung> load = FluentIterable.from(Dienstleistung.loadByParameter("'1'", "1"));
	assertEquals(false, load.isEmpty());
	int size = load.size();
	new Dienstleistung(name, 1, Preis.of(122D), false).save();
	load = FluentIterable.from(Dienstleistung.loadByParameter("'1'", "1"));
	assertEquals(size + 1, load.size());
    }

    @Test
    public void testLoadByParameterStartsWith() {
	new Dienstleistung("Haare schneiden", 2, Preis.of("22,22"), false).save();

	FluentIterable<Dienstleistung> load = FluentIterable.from(Dienstleistung.loadByParameterStartsWith("NAME", "H"));
	assertEquals(false, load.isEmpty());
	for (Dienstleistung dienstleistung : load) {
	    assertTrue(dienstleistung.getDienstleistungsName().startsWith("H"));
	}
    }

    @Test
    public void testLoadByParameterStartsWith_Ordering() {
	new Dienstleistung("Haaare schneiden", 2, Preis.of("22,22"), false).save();

	FluentIterable<Dienstleistung> load = FluentIterable.from(Dienstleistung.loadByParameterStartsWith("NAME", "H", new Ordering("NAME", "ASC")));
	assertEquals(false, load.isEmpty());
	assertEquals("Haaare schneiden", load.first().get().getDienstleistungsName());
	for (Dienstleistung dienstleistung : load) {
	    assertTrue(dienstleistung.getDienstleistungsName().startsWith("H"));
	}
    }
}
