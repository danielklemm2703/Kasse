package database.entities;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTime;
import org.junit.Test;

import util.Try;
import util.Unit;
import datameer.com.google.common.base.Optional;

public class EigenverbrauchTest {

    @Test
    public void testSaveNewEntity() {
	Eigenverbrauch eigenverbrauch = new Eigenverbrauch(1L, DateTime.now());
	Try<Long> save = eigenverbrauch.save();
	assertEquals(true, save.isSuccess());
    }

    @Test
    public void testUpdateExistingEntity() {
	Eigenverbrauch eigenverbrauch = new Eigenverbrauch(1L, DateTime.now());
	Try<Long> save = eigenverbrauch.save();
	Long entityId = save.get();
	eigenverbrauch = new Eigenverbrauch(entityId, 1L, DateTime.now());
	save = eigenverbrauch.save();
	assertEquals(save.get(), eigenverbrauch.getEntityId().get());
    }

    @Test
    public void testLoadExistingEntity() {
	Eigenverbrauch eigenverbrauch = new Eigenverbrauch(1L, DateTime.now());
	Try<Long> save = eigenverbrauch.save();
	Long entityId = save.get();
	Optional<Eigenverbrauch> loadById = Eigenverbrauch.loadById(entityId);
	assertEquals(true, loadById.isPresent());
	assertEquals(1L, loadById.get().getFriseurId());
    }

    @Test
    public void testDeleteExistingEntity() {
	Eigenverbrauch eigenverbrauch = new Eigenverbrauch(1L, DateTime.now());
	Try<Long> save = eigenverbrauch.save();
	Long entityId = save.get();
	Try<Unit> delete = Eigenverbrauch.delete(entityId, Eigenverbrauch.TABLENAME);
	assertEquals(true, delete.isSuccess());
	Optional<Eigenverbrauch> loadById = Eigenverbrauch.loadById(entityId);
	assertEquals(false, loadById.isPresent());
    }

    @Test
    public void testDeleteNotExistingEntity() {
	Try<Unit> delete = Eigenverbrauch.delete(1132L, Eigenverbrauch.TABLENAME);
	assertEquals(true, delete.isSuccess());
    }
}
