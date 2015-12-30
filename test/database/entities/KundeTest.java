package database.entities;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import util.Try;
import util.Unit;
import database.Ordering;
import datameer.com.google.common.base.Optional;
import datameer.com.google.common.collect.FluentIterable;

public class KundeTest {

    @Test
    public void testSaveNewEntity() {
	Kunde horst = new Kunde("Horst", "Klinker", 1L, "0190/666");
	Try<Long> save = horst.save();
	assertEquals(true, save.isSuccess());
    }

    @Test
    public void testUpdateExistingEntity() {
	Kunde horst = new Kunde("Horst", "Klinker", 1L, "0190/666");
	Try<Long> save = horst.save();
	Long entityId = save.get();
	horst = new Kunde(entityId, "Hort", "Kink", 1L, "0190/666");
	save = horst.save();
	assertEquals(save.get(), horst.getEntityId().get());
    }

    @Test
    public void testLoadExistingEntity() {
	Kunde horst = new Kunde("Horst", "Klinker", 1L, "0190/666");
	Try<Long> save = horst.save();
	Long entityId = save.get();
	Optional<Kunde> loadById = Kunde.loadById(entityId);
	assertEquals(true, loadById.isPresent());
	assertEquals("0190/666", loadById.get().getTelefon());
    }

    @Test
    public void testDeleteExistingEntity() {
	Kunde kunde = new Kunde("Horst", "Klinker", 1L, "0190/666");
	Try<Long> save = kunde.save();
	Long entityId = save.get();
	Try<Unit> delete = Kunde.delete(entityId, Kunde.TABLENAME);
	assertEquals(true, delete.isSuccess());
	Optional<Kunde> loadById = Kunde.loadById(entityId);
	assertEquals(false, loadById.isPresent());
    }

    @Test
    public void testDeleteNotExistingEntity() {
	Try<Unit> delete = Kunde.delete(1132L, Kunde.TABLENAME);
	assertEquals(true, delete.isSuccess());
    }

    @Test
    public void testLoadByParameter() {
	FluentIterable<Kunde> load = FluentIterable.from(Kunde.loadByParameter("VORNAME", "Horst"));
	assertEquals(false, load.isEmpty());
	load = FluentIterable.from(Kunde.loadByParameter("VORNAME", "Horswwwt"));
	assertEquals(true, load.isEmpty());
	load = FluentIterable.from(Kunde.loadByParameter("VORNAMER", "Horst"));
	assertEquals(true, load.isEmpty());
    }

    @Test
    public void testLoadByParameterWithOrdering() {
	new Kunde("Horst", "Zyxel", 1L, "0190/666").save();
	new Kunde("Horst", "Zyyel", 1L, "0190/666").save();
	FluentIterable<Kunde> load = FluentIterable.from(Kunde.loadByParameter("VORNAME", "Horst", new Ordering("NACHNAME", "DESC")));
	assertEquals(false, load.isEmpty());
	assertEquals("Zyyel", load.first().get().getNachname());
    }

    @Test
    public void testLoadByParameterNotEveryEntry() {
	double random = Math.random();
	new Kunde("Horst", "Klinker", 1L, "" + random).save();

	FluentIterable<Kunde> load = FluentIterable.from(Kunde.loadByParameter("TELEFON", "" + random));
	assertEquals(false, load.isEmpty());
	assertEquals(1,load.size());
    }

    @Test
    public void testLoadByParameterEveryEntry() {
	new Kunde("Horst", "Klinker", 1L, "0190/666").save();
	FluentIterable<Kunde> load = FluentIterable.from(Kunde.loadByParameter("'1'", "1"));
	assertEquals(false, load.isEmpty());
	int size = load.size();
	new Kunde("Horst", "Klinker", 1L, "0190/666").save();
	load = FluentIterable.from(Kunde.loadByParameter("'1'", "1"));
	assertEquals(size + 1, load.size());
    }
}