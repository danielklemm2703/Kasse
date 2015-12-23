package database.entities;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import util.Preis;
import util.Try;
import datameer.com.google.common.base.Optional;

public class DienstleistungTest {

    @Test
    public void testSaveNewEntity() {
	Dienstleistung dienstleistung = new Dienstleistung("Haare schneiden", 2, Preis.of("22,22"), false);
	Try<Long> save = dienstleistung.save();
	assertEquals(true, save.isSuccess());
    }

    @Test
    public void testUpdateExistingEntity() {
	Dienstleistung dienstleistung = new Dienstleistung("Haare schneiden", 2, Preis.of("22,22"), false);
	Try<Long> save = dienstleistung.save();
	Long entityId = save.get();
	dienstleistung = new Dienstleistung(entityId, "Haare schneiden", 2, Preis.of("22,22"), false);
	save = dienstleistung.save();
	assertEquals(save.get(), dienstleistung.getEntityId().get());
    }

    @Test
    public void testLoadExistingEntity() {
	Dienstleistung dienstleistung = new Dienstleistung("Haare schneiden", 2, Preis.of("22,22"), false);
	Try<Long> save = dienstleistung.save();
	Long entityId = save.get();
	Optional<Dienstleistung> loadById = Dienstleistung.loadById(entityId);
	assertEquals(true, loadById.isPresent());
	assertEquals("Haare schneiden", loadById.get().getDienstleistungsName());
    }
}
