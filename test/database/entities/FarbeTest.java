package database.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import util.Try;
import util.Unit;
import backend.enums.FaerbeTechnik;
import database.Ordering;
import datameer.com.google.common.base.Optional;
import datameer.com.google.common.collect.FluentIterable;

public class FarbeTest {
    @Test
    public void testSaveNewEntity() {
	Farbe Farbe = new Farbe(1L, FaerbeTechnik.ANSATZ, "rot", "3%");
	Try<Long> save = Farbe.save();
	assertEquals(true, save.isSuccess());
    }

    @Test
    public void testUpdateExistingEntity() {
	Farbe Farbe = new Farbe(1L, FaerbeTechnik.ANSATZ, "rot", "3%");
	Try<Long> save = Farbe.save();
	Long entityId = save.get();
	Farbe = new Farbe(entityId, 1L, FaerbeTechnik.ANSATZ, "rot", "3%");
	save = Farbe.save();
	assertEquals(save.get(), Farbe.getEntityId().get());
    }

    @Test
    public void testLoadExistingEntity() {
	Farbe farbe = new Farbe(1L, FaerbeTechnik.ANSATZ, "rot", "3%");
	Try<Long> save = farbe.save();
	Long entityId = save.get();
	Optional<Farbe> loadById = Farbe.loadById(entityId);
	assertEquals(true, loadById.isPresent());
	assertEquals(1L, loadById.get().getPraeparatId());
    }

    @Test
    public void testDeleteExistingEntity() {
	Farbe farbe = new Farbe(1L, FaerbeTechnik.ANSATZ, "rot", "3%");
	Try<Long> save = farbe.save();
	Long entityId = save.get();
	Try<Unit> delete = Farbe.delete(entityId, Farbe.TABLENAME);
	assertEquals(true, delete.isSuccess());
	Optional<Farbe> loadById = Farbe.loadById(entityId);
	assertEquals(false, loadById.isPresent());
    }

    @Test
    public void testDeleteNotExistingEntity() {
	Try<Unit> delete = Farbe.delete(1132L, Farbe.TABLENAME);
	assertEquals(true, delete.isSuccess());
    }

    @Test
    public void testloadByParameter() {
	FluentIterable<Farbe> loadByParameter = FluentIterable.from(Farbe.loadByParameter("ID", "1"));
	assertEquals(false, loadByParameter.isEmpty());
	loadByParameter = FluentIterable.from(Farbe.loadByParameter("EIDIE", "4"));
	assertEquals(true, loadByParameter.isEmpty());
    }

    @Test
    public void testLoadByParameterWithOrdering() {
	new Farbe(1L, FaerbeTechnik.ANSATZ, "rot", "3%").save();
	new Farbe(1L, FaerbeTechnik.ANSATZ, "zrot", "3%").save();
	FluentIterable<Farbe> load = FluentIterable
		.from(Farbe.loadByParameter("FAERBETECHNIK", FaerbeTechnik.ANSATZ.toString(), new Ordering("FARBE", "DESC")));
	assertEquals(false, load.isEmpty());
	assertEquals("zrot", load.first().get().getFarbe());
    }

    @Test
    public void testLoadByParameterNotEveryEntry() {
	double random = Math.random();
	new Farbe(1L, FaerbeTechnik.ANSATZ, "rot", "" + random).save();

	FluentIterable<Farbe> load = FluentIterable.from(Farbe.loadByParameter("OXYD", "" + random));
	assertEquals(false, load.isEmpty());
	assertEquals(1, load.size());
    }

    @Test
    public void testLoadByParameterEveryEntry() {
	new Farbe(1L, FaerbeTechnik.ANSATZ, "rot", "3%").save();
	FluentIterable<Farbe> load = FluentIterable.from(Farbe.loadByParameter("'1'", "1"));
	assertEquals(false, load.isEmpty());
	int size = load.size();
	new Farbe(1L, FaerbeTechnik.ANSATZ, "rot", "3%").save();
	load = FluentIterable.from(Farbe.loadByParameter("'1'", "1"));
	assertEquals(size + 1, load.size());
    }

    @Test
    public void testLoadByParameterStartsWith() {
	new Farbe(1L, FaerbeTechnik.ANSATZ, "rot", "3%").save();

	FluentIterable<Farbe> load = FluentIterable.from(Farbe.loadByParameterStartsWith("FAERBETECHNIK", "A"));
	assertEquals(false, load.isEmpty());
	for (Farbe Farbe : load) {
	    assertTrue(Farbe.getFaerbeTechnik().toString().startsWith("A"));
	}
    }

    @Test
    public void testLoadByParameterStartsWith_Ordering() {
	new Farbe(1L, FaerbeTechnik.ANSATZ, "rot", "0%").save();

	FluentIterable<Farbe> load = FluentIterable.from(Farbe.loadByParameterStartsWith("FAERBETECHNIK", "A", new Ordering("OXYD", "ASC")));
	assertEquals(false, load.isEmpty());
	assertEquals("0%", load.first().get().getOxyd());
	for (Farbe farbe : load) {
	    assertTrue(farbe.getFaerbeTechnik().toString().startsWith("A"));
	}
    }
}
