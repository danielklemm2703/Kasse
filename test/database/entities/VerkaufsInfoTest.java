package database.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import util.Preis;
import util.Try;
import util.Unit;
import database.Ordering;
import datameer.com.google.common.base.Optional;
import datameer.com.google.common.collect.FluentIterable;

public class VerkaufsInfoTest {

    @Test
    public void testSaveNewEntity() {
	VerkaufsInfo verkaufsInfo = new VerkaufsInfo("Horst Hammer", "Helga", "Shampoo", Preis.of(22.33d));
	Try<Long> save = verkaufsInfo.save();
	assertEquals(true, save.isSuccess());
	assertEquals(verkaufsInfo.getEntityId().get(), save.get());
    }

    @Test
    public void testUpdateExistingEntity() {
	VerkaufsInfo verkaufsInfo = new VerkaufsInfo("Horst Hammer", "Helga", "Shampoo", Preis.of(22.33d));
	Try<Long> save = verkaufsInfo.save();
	Long entityId = save.get();
	verkaufsInfo = new VerkaufsInfo(entityId, "Hans Hammer", "Olga", "Shampoo", Preis.of(22.33d));
	save = verkaufsInfo.save();
	assertEquals(save.get(), verkaufsInfo.getEntityId().get());
    }

    @Test
    public void testLoadExistingEntity() {
	VerkaufsInfo verkaufsInfo = new VerkaufsInfo("Horst Hammer", "Helga", "Shampoo", Preis.of(22.33d));
	Try<Long> save = verkaufsInfo.save();
	Long entityId = save.get();
	Optional<VerkaufsInfo> loadById = VerkaufsInfo.loadById(entityId);
	assertEquals(true, loadById.isPresent());
	assertEquals("Helga", loadById.get().getFriseurName());
    }

    @Test
    public void testDeleteExistingEntity() {
	VerkaufsInfo verkaufsInfo = new VerkaufsInfo("Horst Hammer", "Helga", "Shampoo", Preis.of(22.33d));
	Try<Long> save = verkaufsInfo.save();
	Long entityId = save.get();
	Try<Unit> delete = verkaufsInfo.delete();
	assertEquals(true, delete.isSuccess());
	Optional<VerkaufsInfo> loadById = VerkaufsInfo.loadById(entityId);
	assertEquals(false, loadById.isPresent());
    }

    @Test
    public void testDeleteNotExistingEntity() {
	VerkaufsInfo verkaufsInfo = new VerkaufsInfo("Horst Hammer", "Helga", "Shampoo", Preis.of(22.33d));
	Try<Unit> delete = verkaufsInfo.delete();
	assertEquals(false, delete.isSuccess());
    }

    @Test
    public void testloadByParameter() {
	FluentIterable<VerkaufsInfo> loadByParameter = FluentIterable.from(VerkaufsInfo.loadByParameter("KUNDE_NAME", "Horst Hammer"));
	assertEquals(false, loadByParameter.isEmpty());
	loadByParameter = FluentIterable.from(VerkaufsInfo.loadByParameter("KUNDE_NAMER", "Shampoo"));
	assertEquals(true, loadByParameter.isEmpty());
    }

    @Test
    public void testLoadByParameterWithOrdering() {
	String name = "Ziemlich viel Shampoo";
	new VerkaufsInfo("Horst Hammer", "Helga", name, Preis.of(122D)).save();
	new VerkaufsInfo("Horst Hammer", "Helga", "Haare abschneiden", Preis.of(122D)).save();
	FluentIterable<VerkaufsInfo> load = FluentIterable.from(VerkaufsInfo.loadByParameter("PREIS", Preis.of(122D).toString(), new Ordering("VERKAUF_NAME",
		"DESC")));
	assertEquals(false, load.isEmpty());
	assertEquals(name, load.first().get().getVerkaufName());
    }

    @Test
    public void testLoadByParameterNotEveryEntry() {
	double random = Math.random();
	new VerkaufsInfo("" + random, "Helga", "Shampoo", Preis.of(122D)).save();

	FluentIterable<VerkaufsInfo> load = FluentIterable.from(VerkaufsInfo.loadByParameter("KUNDE_NAME", "" + random));
	assertEquals(false, load.isEmpty());
	assertEquals(1, load.size());
    }

    @Test
    public void testLoadByParameterEveryEntry() {
	String name = "Ziemlich viel Shampoo";
	new VerkaufsInfo("Horst Hammer", "Helga", name, Preis.of(122D)).save();
	FluentIterable<VerkaufsInfo> load = FluentIterable.from(VerkaufsInfo.loadByParameter("'1'", "1"));
	assertEquals(false, load.isEmpty());
	int size = load.size();
	new VerkaufsInfo("Horst Hammer", "Helga", name, Preis.of(122D)).save();
	load = FluentIterable.from(VerkaufsInfo.loadByParameter("'1'", "1"));
	assertEquals(size + 1, load.size());
    }

    @Test
    public void testLoadByParameterStartsWith() {
	new VerkaufsInfo("Horst Hammer", "Helga", "Shampoo", Preis.of(122D)).save();

	FluentIterable<VerkaufsInfo> load = FluentIterable.from(VerkaufsInfo.loadByParameterStartsWith("KUNDE_NAME", "H"));
	assertEquals(false, load.isEmpty());
	for (VerkaufsInfo dienstleistung : load) {
	    assertTrue(dienstleistung.getKundeName().startsWith("H"));
	}
    }

    @Test
    public void testLoadByParameterStartsWith_Ordering() {
	new VerkaufsInfo("Horst Hammer", "Helga", "Shampoo", Preis.of(122D)).save();

	FluentIterable<VerkaufsInfo> load = FluentIterable.from(VerkaufsInfo.loadByParameterStartsWith("KUNDE_NAME", "H", new Ordering("KUNDE_NAME", "ASC")));
	assertEquals(false, load.isEmpty());
	assertEquals("Shampoo", load.first().get().getVerkaufName());
	for (VerkaufsInfo dienstleistung : load) {
	    assertTrue(dienstleistung.getKundeName().startsWith("H"));
	}
    }

}
