package database.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import util.Try;
import util.Unit;
import database.Ordering;
import datameer.com.google.common.base.Optional;
import datameer.com.google.common.collect.FluentIterable;

public class KategorieTest {

    @Test
    public void testSaveNewEntity() {
	Kategorie kategorie = new Kategorie("Haare", true, false);
	Try<Long> save = kategorie.save();
	assertEquals(true, save.isSuccess());
    }

    @Test
    public void testUpdateExistingEntity() {
	Kategorie kategorie = new Kategorie("Haare", true, false);
	Try<Long> save = kategorie.save();
	Long entityId = save.get();
	kategorie = new Kategorie(entityId, "Haaren", true, false);
	save = kategorie.save();
	assertEquals(save.get(), kategorie.getEntityId().get());
    }

    @Test
    public void testLoadExistingEntity() {
	Kategorie kategorie = new Kategorie("Haare", true, false);
	Try<Long> save = kategorie.save();
	Long entityId = save.get();
	Optional<Kategorie> loadById = Kategorie.loadById(entityId);
	assertEquals(true, loadById.isPresent());
	assertEquals("Haare", loadById.get().getKategorieName());
    }

    @Test
    public void testDeleteExistingEntity() {
	Kategorie kategorie = new Kategorie("Haare", true, false);
	Try<Long> save = kategorie.save();
	Long entityId = save.get();
	Try<Unit> delete = Kategorie.delete(entityId, Kategorie.TABLENAME);
	assertEquals(true, delete.isSuccess());
	Optional<Kategorie> loadById = Kategorie.loadById(entityId);
	assertEquals(false, loadById.isPresent());
    }

    @Test
    public void testDeleteNotExistingEntity() {
	Try<Unit> delete = Kategorie.delete(1132L, Kategorie.TABLENAME);
	assertEquals(true, delete.isSuccess());
    }

    @Test
    public void testloadByParameter() {
	FluentIterable<Kategorie> loadByParameter = FluentIterable.from(Kategorie.loadByParameter("ID", "1"));
	assertEquals(false, loadByParameter.isEmpty());
	loadByParameter = FluentIterable.from(Kategorie.loadByParameter("EIDIE", "4"));
	assertEquals(true, loadByParameter.isEmpty());
    }

    @Test
    public void testLoadByParameterWithOrdering() {
	new Kategorie("Herren", false, false).save();
	new Kategorie("ZHerren", false, false).save();
	FluentIterable<Kategorie> load = FluentIterable.from(Kategorie.loadByParameter("DIENSTLEISTUNGS_KATEGORIE", "false", new Ordering(
"KATEGORIE_NAME",
		"DESC")));
	assertEquals(false, load.isEmpty());
	assertEquals("ZHerren", load.first().get().getKategorieName());
    }

    @Test
    public void testLoadByParameterNotEveryEntry() {
	double random = Math.random();
	new Kategorie("" + random, false, false).save();

	FluentIterable<Kategorie> load = FluentIterable.from(Kategorie.loadByParameter("KATEGORIE_NAME", "" + random));
	assertEquals(false, load.isEmpty());
	assertEquals(1, load.size());
    }

    @Test
    public void testLoadByParameterEveryEntry() {
	new Kategorie("Herren", false, false).save();
	FluentIterable<Kategorie> load = FluentIterable.from(Kategorie.loadByParameter("'1'", "1"));
	assertEquals(false, load.isEmpty());
	int size = load.size();
	new Kategorie("Herren", false, false).save();
	load = FluentIterable.from(Kategorie.loadByParameter("'1'", "1"));
	assertEquals(size + 1, load.size());
    }

    @Test
    public void testLoadByParameterStartsWith() {
	new Kategorie("Herren", false, false).save();

	FluentIterable<Kategorie> load = FluentIterable.from(Kategorie.loadByParameterStartsWith("KATEGORIE_NAME", "H"));
	assertEquals(false, load.isEmpty());
	for (Kategorie kategorie : load) {
	    assertTrue(kategorie.getKategorieName().startsWith("H"));
	}
    }

    @Test
    public void testLoadByParameterStartsWith_Ordering() {
	new Kategorie("Haaaaerren", false, false).save();

	FluentIterable<Kategorie> load = FluentIterable.from(Kategorie.loadByParameterStartsWith("KATEGORIE_NAME", "H", new Ordering("KATEGORIE_NAME", "ASC")));
	assertEquals(false, load.isEmpty());
	assertEquals("Haaaaerren", load.first().get().getKategorieName());
	for (Kategorie kategorie : load) {
	    assertTrue(kategorie.getKategorieName().startsWith("H"));
	}
    }
}
