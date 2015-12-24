package database.entities;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import util.Try;
import util.Unit;
import datameer.com.google.common.base.Optional;

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
}
