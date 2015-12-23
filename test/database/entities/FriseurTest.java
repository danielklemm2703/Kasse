package database.entities;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import util.Try;
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
}
