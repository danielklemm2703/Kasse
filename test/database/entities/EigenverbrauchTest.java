package database.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
	Eigenverbrauch eigenverbrauch = new Eigenverbrauch(1L, Preis.of(122D), DateTime.now());
	Try<Long> save = eigenverbrauch.save();
	assertEquals(true, save.isSuccess());
    }

    @Test
    public void testUpdateExistingEntity() {
	Eigenverbrauch eigenverbrauch = new Eigenverbrauch(1L, Preis.of(122D), DateTime.now());
	Try<Long> save = eigenverbrauch.save();
	Long entityId = save.get();
	eigenverbrauch = new Eigenverbrauch(entityId, 1L, Preis.of(122D), DateTime.now());
	save = eigenverbrauch.save();
	assertEquals(save.get(), eigenverbrauch.getEntityId().get());
    }

    @Test
    public void testLoadExistingEntity() {
	Eigenverbrauch eigenverbrauch = new Eigenverbrauch(1L, Preis.of(122D), DateTime.now());
	Try<Long> save = eigenverbrauch.save();
	Long entityId = save.get();
	Optional<Eigenverbrauch> loadById = Eigenverbrauch.loadById(entityId);
	assertEquals(true, loadById.isPresent());
	assertEquals(1L, loadById.get().getFriseurId());
    }

    @Test
    public void testDeleteExistingEntity() {
	Eigenverbrauch eigenverbrauch = new Eigenverbrauch(1L, Preis.of(122D), DateTime.now());
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
	String now = DateTime.now().toString();
	FluentIterable<Eigenverbrauch> loadByParameter = FluentIterable.from(Eigenverbrauch.loadByParameter("ID", "1"));
	assertEquals(false, loadByParameter.isEmpty());
	loadByParameter = FluentIterable.from(Eigenverbrauch.loadByParameter("DAUTUUM", now));
	assertEquals(true, loadByParameter.isEmpty());
    }

    @Test
    public void testLoadByParameterWithOrdering() {
	DateTime now = DateTime.now();
	new Eigenverbrauch(1, Preis.of(122D), now).save();
	new Eigenverbrauch(2, Preis.of(122D), now).save();
	FluentIterable<Eigenverbrauch> load = FluentIterable.from(Eigenverbrauch.loadByParameter("DATUM", now.toString(), new Ordering("FRISEUR_ID",
		"DESC")));
	assertEquals(false, load.isEmpty());
	assertEquals(2, load.first().get().getFriseurId());
    }

    @Test
    public void testLoadByParameterNotEveryEntry() {
	DateTime now = DateTime.now();
	new Eigenverbrauch(1L, Preis.of(122D), now).save();

	FluentIterable<Eigenverbrauch> load = FluentIterable.from(Eigenverbrauch.loadByParameter("DATUM", now.toString()));
	assertEquals(false, load.isEmpty());
	assertEquals(1, load.size());
    }

    @Test
    public void testLoadByParameterEveryEntry() {
	new Eigenverbrauch(1L, Preis.of(122D), DateTime.now()).save();
	FluentIterable<Eigenverbrauch> load = FluentIterable.from(Eigenverbrauch.loadByParameter("'1'", "1"));
	assertEquals(false, load.isEmpty());
	int size = load.size();
	new Eigenverbrauch(1L, Preis.of(122D), DateTime.now()).save();
	load = FluentIterable.from(Eigenverbrauch.loadByParameter("'1'", "1"));
	assertEquals(size + 1, load.size());
    }

    @Test
    public void testLoadByParameterStartsWith() {
	new Eigenverbrauch(1L, Preis.of(122D), DateTime.now()).save();

	FluentIterable<Eigenverbrauch> load = FluentIterable.from(Eigenverbrauch.loadByParameterStartsWith("PREIS", "1"));
	assertEquals(false, load.isEmpty());
	for (Eigenverbrauch eigenverbrauch : load) {
	    assertTrue(eigenverbrauch.getPreis().toString().startsWith("1"));
	}
    }

    @Test
    public void testLoadByParameterStartsWith_Ordering() {
	new Eigenverbrauch(1L, Preis.of(1.00D), DateTime.now()).save();

	FluentIterable<Eigenverbrauch> load = FluentIterable.from(Eigenverbrauch.loadByParameterStartsWith("PREIS", "1", new Ordering("PREIS", "ASC")));
	assertEquals(false, load.isEmpty());
	assertEquals(Preis.of(1.00D).toString(), load.first().get().getPreis().toString());
	for (Eigenverbrauch eigenverbrauch : load) {
	    assertTrue(eigenverbrauch.getPreis().toString().startsWith("1"));
	}
    }
}
