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

public class DienstleistungsInfoTest {

    @Test
    public void testSaveNewEntity() {
	DienstleistungsInfo dienstleistungInfo = new DienstleistungsInfo("Horst Hammer", "Helga", "Haare schneiden", Preis.of(22.33d));
	Try<Long> save = dienstleistungInfo.save();
	assertEquals(true, save.isSuccess());
    }

    @Test
    public void testUpdateExistingEntity() {
	DienstleistungsInfo dienstleistungInfo = new DienstleistungsInfo("Horst Hammer", "Helga", "Haare schneiden", Preis.of(22.33d));
	Try<Long> save = dienstleistungInfo.save();
	Long entityId = save.get();
	dienstleistungInfo = new DienstleistungsInfo(entityId, "Hans Hammer", "Olga", "Haare schneiden", Preis.of(22.33d));
	save = dienstleistungInfo.save();
	assertEquals(save.get(), dienstleistungInfo.getEntityId().get());
    }

    @Test
    public void testLoadExistingEntity() {
	DienstleistungsInfo dienstleistungInfo = new DienstleistungsInfo("Horst Hammer", "Helga", "Haare schneiden", Preis.of(22.33d));
	Try<Long> save = dienstleistungInfo.save();
	Long entityId = save.get();
	Optional<DienstleistungsInfo> loadById = DienstleistungsInfo.loadById(entityId);
	assertEquals(true, loadById.isPresent());
	assertEquals("Helga", loadById.get().getFriseurName());
    }

    @Test
    public void testDeleteExistingEntity() {
	DienstleistungsInfo dienstleistungInfo = new DienstleistungsInfo("Horst Hammer", "Helga", "Haare schneiden", Preis.of(22.33d));
	Try<Long> save = dienstleistungInfo.save();
	Long entityId = save.get();
	Try<Unit> delete = DienstleistungsInfo.delete(entityId, DienstleistungsInfo.TABLENAME);
	assertEquals(true, delete.isSuccess());
	Optional<DienstleistungsInfo> loadById = DienstleistungsInfo.loadById(entityId);
	assertEquals(false, loadById.isPresent());
    }

    @Test
    public void testDeleteNotExistingEntity() {
	Try<Unit> delete = DienstleistungsInfo.delete(1132L, DienstleistungsInfo.TABLENAME);
	assertEquals(true, delete.isSuccess());
    }

    @Test
    public void testloadByParameter() {
	FluentIterable<DienstleistungsInfo> loadByParameter = FluentIterable.from(DienstleistungsInfo.loadByParameter("KUNDE_NAME", "Horst Hammer"));
	assertEquals(false, loadByParameter.isEmpty());
	loadByParameter = FluentIterable.from(DienstleistungsInfo.loadByParameter("KUNDE_NAMER", "Haare schneiden"));
	assertEquals(true, loadByParameter.isEmpty());
    }

    @Test
    public void testLoadByParameterWithOrdering() {
	String name = "Ziemlich viele Haare abschneiden";
	new DienstleistungsInfo("Horst Hammer", "Helga", name, Preis.of(122D)).save();
	new DienstleistungsInfo("Horst Hammer", "Helga", "Haare abschneiden", Preis.of(122D)).save();
	FluentIterable<DienstleistungsInfo> load = FluentIterable.from(DienstleistungsInfo.loadByParameter("PREIS", Preis.of(122D).toString(), new Ordering(
		"DIENSTLEISTUNG_NAME",
		"DESC")));
	assertEquals(false, load.isEmpty());
	assertEquals(name, load.first().get().getDienstleistungName());
    }

    @Test
    public void testLoadByParameterNotEveryEntry() {
	double random = Math.random();
	new DienstleistungsInfo("" + random, "Helga", "HaareSchneiden", Preis.of(122D)).save();

	FluentIterable<DienstleistungsInfo> load = FluentIterable.from(DienstleistungsInfo.loadByParameter("KUNDE_NAME", "" + random));
	assertEquals(false, load.isEmpty());
	assertEquals(1, load.size());
    }

    @Test
    public void testLoadByParameterEveryEntry() {
	String name = "Ziemlich viele Haare abschneiden";
	new DienstleistungsInfo("Horst Hammer", "Helga", name, Preis.of(122D)).save();
	FluentIterable<DienstleistungsInfo> load = FluentIterable.from(DienstleistungsInfo.loadByParameter("'1'", "1"));
	assertEquals(false, load.isEmpty());
	int size = load.size();
	new DienstleistungsInfo("Horst Hammer", "Helga", name, Preis.of(122D)).save();
	load = FluentIterable.from(DienstleistungsInfo.loadByParameter("'1'", "1"));
	assertEquals(size + 1, load.size());
    }

    @Test
    public void testLoadByParameterStartsWith() {
	new DienstleistungsInfo("Horst Hammer", "Helga", "Haare schneiden", Preis.of(122D)).save();

	FluentIterable<DienstleistungsInfo> load = FluentIterable.from(DienstleistungsInfo.loadByParameterStartsWith("KUNDE_NAME", "H"));
	assertEquals(false, load.isEmpty());
	for (DienstleistungsInfo dienstleistung : load) {
	    assertTrue(dienstleistung.getKundeName().startsWith("H"));
	}
    }

    @Test
    public void testLoadByParameterStartsWith_Ordering() {
	new DienstleistungsInfo("Horst Hammer", "Helga", "Haare schneiden", Preis.of(122D)).save();

	FluentIterable<DienstleistungsInfo> load = FluentIterable.from(DienstleistungsInfo.loadByParameterStartsWith("KUNDE_NAME", "H", new Ordering(
		"KUNDE_NAME", "ASC")));
	assertEquals(false, load.isEmpty());
	assertEquals("Haare schneiden", load.first().get().getDienstleistungName());
	for (DienstleistungsInfo dienstleistung : load) {
	    assertTrue(dienstleistung.getKundeName().startsWith("H"));
	}
    }
}
