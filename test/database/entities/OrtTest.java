package database.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import util.Try;
import util.Unit;
import database.Ordering;
import datameer.com.google.common.base.Optional;
import datameer.com.google.common.collect.FluentIterable;

public class OrtTest {
    @Test
    public void testSaveNewEntity() {
	Ort ort = new Ort("Brand-Erbisdorf");
	Try<Long> save = ort.save();
	assertEquals(true, save.isSuccess());
	assertEquals(ort.getEntityId().get(), save.get());
    }

    @Test
    public void testUpdateExistingEntity() {
	Ort ort = new Ort("Brand-Erbisdorf");
	Try<Long> save = ort.save();
	Long entityId = save.get();
	ort = new Ort(entityId, "Brand-Erbisdorf");
	save = ort.save();
	assertEquals(save.get(), ort.getEntityId().get());
    }

    @Test
    public void testLoadExistingEntity() {
	Ort ort = new Ort("Brand-Erbisdorf");
	Try<Long> save = ort.save();
	Long entityId = save.get();
	Optional<Ort> loadById = Ort.loadById(entityId);
	assertEquals(true, loadById.isPresent());
	assertEquals("Brand-Erbisdorf", loadById.get().getOrtName());
    }

    @Test
    public void testDeleteExistingEntity() {
	Ort ort = new Ort("Brand-Erbisdorf");
	Try<Long> save = ort.save();
	Long entityId = save.get();
	Try<Unit> delete = ort.delete();
	assertEquals(true, delete.isSuccess());
	Optional<Ort> loadById = Ort.loadById(entityId);
	assertEquals(false, loadById.isPresent());
    }

    @Test
    public void testDeleteNotExistingEntity() {
	Ort ort = new Ort("Brand-Erbisdorf");
	Try<Unit> delete = ort.delete();
	assertEquals(false, delete.isSuccess());
    }

    @Test
    public void testloadByParameter() {
	Ort ort = new Ort("Brand-Erbisdorf");
	Try<Long> save = ort.save();
	Long entityId = save.get();
	FluentIterable<Ort> loadByParameter = FluentIterable.from(Ort.loadByParameter("ID", "" + entityId));
	assertEquals(false, loadByParameter.isEmpty());
	assertEquals("Brand-Erbisdorf", loadByParameter.first().get().getOrtName());
	loadByParameter = FluentIterable.from(Ort.loadByParameter("EIDIE", "4"));
	assertEquals(true, loadByParameter.isEmpty());
    }

    @Test
    public void testLoadByParameterWithOrdering() {
	new Ort("Brand-Erbisdorf").save();
	new Ort("ZBrand-Erbisdorf").save();
	FluentIterable<Ort> load = FluentIterable.from(Ort.loadByParameter("ORT_NAME", "ZBrand-Erbisdorf", new Ordering("ORT_NAME", "DESC")));
	assertEquals(false, load.isEmpty());
	assertEquals("ZBrand-Erbisdorf", load.first().get().getOrtName());
    }

    @Test
    public void testLoadByParameterNotEveryEntry() {
	double random = Math.random();
	new Ort("" + random).save();

	FluentIterable<Ort> load = FluentIterable.from(Ort.loadByParameter("ORT_NAME", "" + random));
	assertEquals(false, load.isEmpty());
	assertEquals(1, load.size());
    }

    @Test
    public void testLoadByParameterEveryEntry() {
	new Ort("Brand-Erbisdorf").save();
	FluentIterable<Ort> load = FluentIterable.from(Ort.loadByParameter("'1'", "1"));
	assertEquals(false, load.isEmpty());
	int size = load.size();
	new Ort("Brand-Erbisdorf").save();
	load = FluentIterable.from(Ort.loadByParameter("'1'", "1"));
	assertEquals(size + 1, load.size());
    }

    @Test
    public void testLoadByParameterStartsWith() {
	new Ort("Brand-Erbisdorf").save();

	FluentIterable<Ort> load = FluentIterable.from(Ort.loadByParameterStartsWith("ORT_NAME", "B"));
	assertEquals(false, load.isEmpty());
	for (Ort Ort : load) {
	    assertTrue(Ort.getOrtName().startsWith("B"));
	}
    }

    @Test
    public void testLoadByParameterStartsWith_Ordering() {
	new Ort("Baaarand-Erbisdorf").save();

	FluentIterable<Ort> load = FluentIterable.from(Ort.loadByParameterStartsWith("ORT_NAME", "B", new Ordering("ORT_NAME", "ASC")));
	assertEquals(false, load.isEmpty());
	assertEquals("Baaarand-Erbisdorf", load.first().get().getOrtName());
	for (Ort Ort : load) {
	    assertTrue(Ort.getOrtName().startsWith("B"));
	}
    }
}
