package database.entities;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import util.Try;
import util.Unit;
import datameer.com.google.common.base.Optional;

public class PraeparatTest {

    @Test
    public void testSaveNewEntity() {
	Praeparat praeparat = new Praeparat("C:ehko");
	Try<Long> save = praeparat.save();
	assertEquals(true, save.isSuccess());
    }

    @Test
    public void testUpdateExistingEntity() {
	Praeparat praeparat = new Praeparat("C:ehko");
	Try<Long> save = praeparat.save();
	Long entityId = save.get();
	praeparat = new Praeparat(entityId, "C:ehko");
	save = praeparat.save();
	assertEquals(save.get(), praeparat.getEntityId().get());
    }

    @Test
    public void testLoadExistingEntity() {
	Praeparat praeparat = new Praeparat("C:ehko");
	Try<Long> save = praeparat.save();
	Long entityId = save.get();
	Optional<Praeparat> loadById = Praeparat.loadById(entityId);
	assertEquals(true, loadById.isPresent());
	assertEquals("C:ehko", loadById.get().getName());
    }

    @Test
    public void testDeleteExistingEntity() {
	Praeparat praeparat = new Praeparat("C:ehko");
	Try<Long> save = praeparat.save();
	Long entityId = save.get();
	Try<Unit> delete = Praeparat.delete(entityId, Praeparat.TABLENAME);
	assertEquals(true, delete.isSuccess());
	Optional<Praeparat> loadById = Praeparat.loadById(entityId);
	assertEquals(false, loadById.isPresent());
    }

    @Test
    public void testDeleteNotExistingEntity() {
	Try<Unit> delete = Praeparat.delete(1132L, Praeparat.TABLENAME);
	assertEquals(true, delete.isSuccess());
    }
}
