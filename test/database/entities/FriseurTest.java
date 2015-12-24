package database.entities;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import util.Try;
import util.Unit;
import datameer.com.google.common.base.Optional;

public class FriseurTest {

    @Test
    public void testSaveNewEntity() {
	Friseur friseur = new Friseur("Anne");
	Try<Long> save = friseur.save();
	assertEquals(true, save.isSuccess());
    }

    @Test
    public void testUpdateExistingEntity() {
	Friseur friseur = new Friseur("Anne");
	Try<Long> save = friseur.save();
	Long entityId = save.get();
	friseur = new Friseur(entityId, "Anne");
	save = friseur.save();
	assertEquals(save.get(), friseur.getEntityId().get());
    }

    @Test
    public void testLoadExistingEntity() {
	Friseur friseur = new Friseur("Anne");
	Try<Long> save = friseur.save();
	Long entityId = save.get();
	Optional<Friseur> loadById = Friseur.loadById(entityId);
	assertEquals(true, loadById.isPresent());
	assertEquals("Anne", loadById.get().getFriseurName());
    }

    @Test
    public void testDeleteExistingEntity() {
	Friseur friseur = new Friseur("Anne");
	Try<Long> save = friseur.save();
	Long entityId = save.get();
	Try<Unit> delete = Friseur.delete(entityId, Friseur.TABLENAME);
	assertEquals(true, delete.isSuccess());
	Optional<Friseur> loadById = Friseur.loadById(entityId);
	assertEquals(false, loadById.isPresent());
    }

    @Test
    public void testDeleteNotExistingEntity() {
	Try<Unit> delete = Friseur.delete(1132L, Friseur.TABLENAME);
	assertEquals(true, delete.isSuccess());
    }
}
