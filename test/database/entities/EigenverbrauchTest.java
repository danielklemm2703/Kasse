package database.entities;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTime;
import org.junit.Test;

import util.Preis;
import util.Try;
import util.Unit;
import database.Ordering;
import datameer.com.google.common.base.Optional;
import datameer.com.google.common.collect.FluentIterable;

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

    @Test
    public void testloadByParameter() {
	FluentIterable<Eigenverbrauch> loadByParameter = FluentIterable.from(Eigenverbrauch.loadByParameter("NAME", "Haare schneiden"));
	assertEquals(false, loadByParameter.isEmpty());
	loadByParameter = FluentIterable.from(Eigenverbrauch.loadByParameter("NAMER", "Haare schneiden"));
	assertEquals(true, loadByParameter.isEmpty());
    }

    @Test
    public void testLoadByParameterWithOrdering() {
	DateTime now = DateTime.now();
	new Eigenverbrauch(1, now).save();
	new Eigenverbrauch(2, now).save();
	FluentIterable<Eigenverbrauch> load = FluentIterable.from(Eigenverbrauch.loadByParameter("DATUM", now.toString(), new Ordering("FRISEUR_ID",
		"DESC")));
	assertEquals(false, load.isEmpty());
	assertEquals(2, load.first().get().getFriseurId());
    }

    @Test
    public void testLoadByParameterNotEveryEntry() {
	double random = Math.random();
	new Eigenverbrauch("" + random, 1, Preis.of(122D), false).save();

	FluentIterable<Eigenverbrauch> load = FluentIterable.from(Eigenverbrauch.loadByParameter("NAME", "" + random));
	assertEquals(false, load.isEmpty());
	assertEquals(1, load.size());
    }

    @Test
    public void testLoadByParameterEveryEntry() {
	String name = "Ziemlich viele Haare abschneiden";
	new Eigenverbrauch(name, 1, Preis.of(122D), false).save();
	FluentIterable<Eigenverbrauch> load = FluentIterable.from(Eigenverbrauch.loadByParameter("'1'", "1"));
	assertEquals(false, load.isEmpty());
	int size = load.size();
	new Eigenverbrauch(name, 1, Preis.of(122D), false).save();
	load = FluentIterable.from(Eigenverbrauch.loadByParameter("'1'", "1"));
	assertEquals(size + 1, load.size());
    }
}
