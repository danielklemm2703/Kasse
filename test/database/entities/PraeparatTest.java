package database.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import util.Try;
import util.Unit;
import database.Ordering;
import datameer.com.google.common.base.Optional;
import datameer.com.google.common.collect.FluentIterable;

public class PraeparatTest {

    @Test
    public void testSaveNewEntity() {
	Praeparat praeparat = new Praeparat("C:ehko");
	Try<Long> save = praeparat.save();
	assertEquals(true, save.isSuccess());
	assertEquals(praeparat.getEntityId().get(), save.get());
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
	Try<Unit> delete = praeparat.delete();
	assertEquals(true, delete.isSuccess());
	Optional<Praeparat> loadById = Praeparat.loadById(entityId);
	assertEquals(false, loadById.isPresent());
    }

    @Test
    public void testDeleteNotExistingEntity() {
	Praeparat praeparat = new Praeparat("C:ehko");
	Try<Unit> delete = praeparat.delete();
	assertEquals(false, delete.isSuccess());
    }

    @Test
    public void testloadByParameter() {
	FluentIterable<Praeparat> loadByParameter = FluentIterable.from(Praeparat.loadByParameter("ID", "1"));
	assertEquals(false, loadByParameter.isEmpty());
	loadByParameter = FluentIterable.from(Praeparat.loadByParameter("EIDIE", "4"));
	assertEquals(true, loadByParameter.isEmpty());
    }

    @Test
    public void testLoadByParameterWithOrdering() {
	new Praeparat("Divano").save();
	new Praeparat("ZDivano").save();
	FluentIterable<Praeparat> load = FluentIterable.from(Praeparat.loadByParameter("NAME", "ZDivano", new Ordering("NAME", "DESC")));
	assertEquals(false, load.isEmpty());
	assertEquals("ZDivano", load.first().get().getName());
    }

    @Test
    public void testLoadByParameterNotEveryEntry() {
	double random = Math.random();
	new Praeparat("" + random).save();

	FluentIterable<Praeparat> load = FluentIterable.from(Praeparat.loadByParameter("NAME", "" + random));
	assertEquals(false, load.isEmpty());
	assertEquals(1, load.size());
    }

    @Test
    public void testLoadByParameterEveryEntry() {
	new Praeparat("Divano").save();
	FluentIterable<Praeparat> load = FluentIterable.from(Praeparat.loadByParameter("'1'", "1"));
	assertEquals(false, load.isEmpty());
	int size = load.size();
	new Praeparat("Divano").save();
	load = FluentIterable.from(Praeparat.loadByParameter("'1'", "1"));
	assertEquals(size + 1, load.size());
    }

    @Test
    public void testLoadByParameterStartsWith() {
	new Praeparat("Divano").save();

	FluentIterable<Praeparat> load = FluentIterable.from(Praeparat.loadByParameterStartsWith("NAME", "D"));
	assertEquals(false, load.isEmpty());
	for (Praeparat praeparat : load) {
	    assertTrue(praeparat.getName().startsWith("D"));
	}
    }

    @Test
    public void testLoadByParameterStartsWith_Ordering() {
	new Praeparat("Daaaaivano").save();

	FluentIterable<Praeparat> load = FluentIterable.from(Praeparat.loadByParameterStartsWith("NAME", "D", new Ordering("NAME", "ASC")));
	assertEquals(false, load.isEmpty());
	assertEquals("Daaaaivano", load.first().get().getName());
	for (Praeparat praeparat : load) {
	    assertTrue(praeparat.getName().startsWith("D"));
	}
    }
}
