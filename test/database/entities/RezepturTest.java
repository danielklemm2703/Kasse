package database.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import util.Try;
import util.Unit;
import database.Ordering;
import datameer.com.google.common.base.Optional;
import datameer.com.google.common.collect.FluentIterable;
import datameer.com.google.common.collect.ImmutableList;

public class RezepturTest {

    @Test
    public void testSaveNewEntity() {
	FluentIterable<Long> farbIds = FluentIterable.from(ImmutableList.of(1L, 2L, 3L));
	Rezeptur rezeptur = new Rezeptur(1L, Optional.of(farbIds), Optional.<Long> absent(), "war jut", false);
	Try<Long> save = rezeptur.save();
	assertEquals(true, save.isSuccess());
	assertEquals(rezeptur.getEntityId().get(), save.get());
    }

    @Test
    public void testUpdateExistingEntity() {
	Rezeptur rezeptur = new Rezeptur(1L, Optional.<FluentIterable<Long>> absent(), Optional.<Long> absent(), "war jut", false);
	Try<Long> save = rezeptur.save();
	Long entityId = save.get();
	rezeptur = new Rezeptur(entityId, 1L, Optional.<FluentIterable<Long>> absent(), Optional.<Long> absent(), "war jut", false);
	save = rezeptur.save();
	assertEquals(save.get(), rezeptur.getEntityId().get());
    }

    @Test
    public void testLoadExistingEntity() {
	FluentIterable<Long> farbIds = FluentIterable.from(ImmutableList.of(1L, 2L, 3L));
	Rezeptur rezeptur = new Rezeptur(1L, Optional.of(farbIds), Optional.<Long> absent(), "war jut", false);
	Try<Long> save = rezeptur.save();
	Long entityId = save.get();
	Optional<Rezeptur> loadById = Rezeptur.loadById(entityId);
	assertEquals(true, loadById.isPresent());
	assertEquals("war jut", loadById.get().getErgebnis());
    }

    @Test
    public void testDeleteExistingEntity() {
	Rezeptur rezeptur = new Rezeptur(1L, Optional.<FluentIterable<Long>> absent(), Optional.<Long> absent(), "war jut", false);
	Try<Long> save = rezeptur.save();
	Long entityId = save.get();
	Try<Unit> delete = rezeptur.delete();
	assertEquals(true, delete.isSuccess());
	Optional<Rezeptur> loadById = Rezeptur.loadById(entityId);
	assertEquals(false, loadById.isPresent());
    }

    @Test
    public void testDeleteNotExistingEntity() {
	Rezeptur rezeptur = new Rezeptur(1L, Optional.<FluentIterable<Long>> absent(), Optional.<Long> absent(), "war jut", false);
	Try<Unit> delete = rezeptur.delete();
	assertEquals(false, delete.isSuccess());
    }

    @Test
    public void testloadByParameter() {
	FluentIterable<Rezeptur> loadByParameter = FluentIterable.from(Rezeptur.loadByParameter("ID", "1"));
	assertEquals(false, loadByParameter.isEmpty());
	loadByParameter = FluentIterable.from(Rezeptur.loadByParameter("EIDIE", "4"));
	assertEquals(true, loadByParameter.isEmpty());
    }

    @Test
    public void testLoadByParameterWithOrdering() {
	FluentIterable<Long> farbIds = FluentIterable.from(ImmutableList.of(1L, 2L, 3L));
	new Rezeptur(1L, Optional.of(farbIds), Optional.<Long> absent(), "war jut", false).save();
	new Rezeptur(1L, Optional.of(farbIds), Optional.<Long> absent(), "zzzwar jut", false).save();
	FluentIterable<Rezeptur> load = FluentIterable.from(Rezeptur.loadByParameter("BEREITS_EINGETRAGEN", "false", new Ordering("ERGEBNIS", "DESC")));
	assertEquals(false, load.isEmpty());
	assertEquals("zzzwar jut", load.first().get().getErgebnis());
    }

    @Test
    public void testLoadByParameterNotEveryEntry() {
	String random = "" + Math.random();
	FluentIterable<Long> farbIds = FluentIterable.from(ImmutableList.of(1L, 2L, 3L));
	new Rezeptur(1L, Optional.of(farbIds), Optional.<Long> absent(), random, false).save();

	FluentIterable<Rezeptur> load = FluentIterable.from(Rezeptur.loadByParameter("ERGEBNIS", "" + random));
	assertEquals(false, load.isEmpty());
	assertEquals(1, load.size());
	FluentIterable<Long> ids = load.get(0).getFarbIds().get();
	assertEquals(3, ids.size());
	assertEquals(1L, ids.first().get(), 0);
    }

    @Test
    public void testLoadByParameterEveryEntry() {
	new Rezeptur(1L, Optional.<FluentIterable<Long>> absent(), Optional.<Long> absent(), "vorhanden", false).save();
	FluentIterable<Rezeptur> load = FluentIterable.from(Rezeptur.loadByParameter("'1'", "1"));
	assertEquals(false, load.isEmpty());
	int size = load.size();
	new Rezeptur(1L, Optional.<FluentIterable<Long>> absent(), Optional.<Long> absent(), "vorhanden", false).save();
	load = FluentIterable.from(Rezeptur.loadByParameter("'1'", "1"));
	assertEquals(size + 1, load.size());
    }

    @Test
    public void testLoadByParameterStartsWith() {
	new Rezeptur(1L, Optional.<FluentIterable<Long>> absent(), Optional.<Long> absent(), "vorhanden", false).save();

	FluentIterable<Rezeptur> load = FluentIterable.from(Rezeptur.loadByParameterStartsWith("ERGEBNIS", "v"));
	assertEquals(false, load.isEmpty());
	for (Rezeptur rezeptur : load) {
	    assertTrue(rezeptur.getErgebnis().startsWith("v"));
	}
    }

    @Test
    public void testLoadByParameterStartsWith_Ordering() {
	new Rezeptur(1L, Optional.<FluentIterable<Long>> absent(), Optional.<Long> absent(), "vaaaaorhanden", false).save();

	FluentIterable<Rezeptur> load = FluentIterable.from(Rezeptur.loadByParameterStartsWith("ERGEBNIS", "v", new Ordering("ERGEBNIS", "ASC")));
	assertEquals(false, load.isEmpty());
	assertEquals("vaaaaorhanden", load.first().get().getErgebnis());
	for (Rezeptur rezeptur : load) {
	    assertTrue(rezeptur.getErgebnis().startsWith("v"));
	}
    }
}
