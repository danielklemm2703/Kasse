package database.entities;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import util.Preis;
import util.Try;
import util.Unit;
import datameer.com.google.common.base.Optional;

public class VerkaufTest {

    @Test
    public void testSaveNewEntity() {
	Verkauf verkauf = new Verkauf("Haare schneiden", 2, Preis.of("22,22"));
	Try<Long> save = verkauf.save();
	assertEquals(true, save.isSuccess());
    }

    @Test
    public void testUpdateExistingEntity() {
	Verkauf verkauf = new Verkauf("Haare schneiden", 2, Preis.of("22,22"));
	Try<Long> save = verkauf.save();
	Long entityId = save.get();
	verkauf = new Verkauf(entityId, "Haare schneiden", 2, Preis.of("22,22"));
	save = verkauf.save();
	assertEquals(save.get(), verkauf.getEntityId().get());
    }

    @Test
    public void testLoadExistingEntity() {
	Verkauf verkauf = new Verkauf("Haare schneiden", 2, Preis.of("22,22"));
	Try<Long> save = verkauf.save();
	Long entityId = save.get();
	Optional<Verkauf> loadById = Verkauf.loadById(entityId);
	assertEquals(true, loadById.isPresent());
	assertEquals("Haare schneiden", loadById.get().getVerkaufsName());
    }

    @Test
    public void testDeleteExistingEntity() {
	Verkauf verkauf = new Verkauf("Haare schneiden", 2, Preis.of("22,22"));
	Try<Long> save = verkauf.save();
	Long entityId = save.get();
	Try<Unit> delete = Verkauf.delete(entityId, Verkauf.TABLENAME);
	assertEquals(true, delete.isSuccess());
	Optional<Verkauf> loadById = Verkauf.loadById(entityId);
	assertEquals(false, loadById.isPresent());
    }

    @Test
    public void testDeleteNotExistingEntity() {
	Try<Unit> delete = Verkauf.delete(1132L, Verkauf.TABLENAME);
	assertEquals(true, delete.isSuccess());
    }
}
