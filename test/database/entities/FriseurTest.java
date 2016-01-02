package database.entities;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import util.Try;
import util.Unit;
import database.Ordering;
import datameer.com.google.common.base.Optional;
import datameer.com.google.common.collect.FluentIterable;

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

    @Test
    public void testloadByParameter() {
	FluentIterable<Friseur> loadByParameter = FluentIterable.from(Friseur.loadByParameter("ID", "1"));
	assertEquals(false, loadByParameter.isEmpty());
	loadByParameter = FluentIterable.from(Friseur.loadByParameter("EIDIE", "4"));
	assertEquals(true, loadByParameter.isEmpty());
    }

    @Test
    public void testLoadByParameterWithOrdering() {
	new Friseur("Aanne").save();
	new Friseur("ZAanne").save();
	FluentIterable<Friseur> load = FluentIterable.from(Friseur.loadByParameter("FRISEUR_NAME", "ZAanne", new Ordering("FRISEUR_NAME",
		"DESC")));
	assertEquals(false, load.isEmpty());
	assertEquals("ZAanne", load.first().get().getFriseurName());
    }

    @Test
    public void testLoadByParameterNotEveryEntry() {
	double random = Math.random();
	new Friseur("" + random).save();

	FluentIterable<Friseur> load = FluentIterable.from(Friseur.loadByParameter("FRISEUR_NAME", "" + random));
	assertEquals(false, load.isEmpty());
	assertEquals(1, load.size());
    }

    @Test
    public void testLoadByParameterEveryEntry() {
	new Friseur("Aanne").save();
	FluentIterable<Friseur> load = FluentIterable.from(Friseur.loadByParameter("'1'", "1"));
	assertEquals(false, load.isEmpty());
	int size = load.size();
	new Friseur("Aanne").save();
	load = FluentIterable.from(Friseur.loadByParameter("'1'", "1"));
	assertEquals(size + 1, load.size());
    }
}
