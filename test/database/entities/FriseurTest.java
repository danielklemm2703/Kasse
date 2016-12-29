package database.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
	assertEquals(friseur.getEntityId().get(), save.get());
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
	Try<Unit> delete = friseur.delete();
	assertEquals(true, delete.isSuccess());
	Optional<Friseur> loadById = Friseur.loadById(entityId);
	assertEquals(false, loadById.isPresent());
    }

    @Test
    public void testDeleteNotExistingEntity() {
	Friseur friseur = new Friseur("Anne");
	Try<Unit> delete = friseur.delete();
	assertEquals(false, delete.isSuccess());
    }

    @Test
    public void testloadByParameter() {
	Friseur friseur = new Friseur("Anne");
	Try<Long> save = friseur.save();
	Long entityId = save.get();
	FluentIterable<Friseur> loadByParameter = FluentIterable.from(Friseur.loadByParameter("ID", "" + entityId));
	assertEquals(false, loadByParameter.isEmpty());
	assertEquals("Anne", loadByParameter.first().get().getFriseurName());
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

    @Test
    public void testLoadByParameterStartsWith() {
	new Friseur("Aanne").save();

	FluentIterable<Friseur> load = FluentIterable.from(Friseur.loadByParameterStartsWith("FRISEUR_NAME", "A"));
	assertEquals(false, load.isEmpty());
	for (Friseur friseur : load) {
	    assertTrue(friseur.getFriseurName().startsWith("A"));
	}
    }

    @Test
    public void testLoadByParameterStartsWith_Ordering() {
	new Friseur("Aaannee").save();

	FluentIterable<Friseur> load = FluentIterable.from(Friseur.loadByParameterStartsWith("FRISEUR_NAME", "A", new Ordering("FRISEUR_NAME", "ASC")));
	assertEquals(false, load.isEmpty());
	assertEquals("Aaannee", load.first().get().getFriseurName());
	for (Friseur friseur : load) {
	    assertTrue(friseur.getFriseurName().startsWith("A"));
	}
    }
}
