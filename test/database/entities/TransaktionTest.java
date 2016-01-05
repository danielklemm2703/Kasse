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
import datameer.com.google.common.collect.ImmutableList;

public class TransaktionTest {

    @Test
    public void testSaveNewEntity() {
	FluentIterable<Long> ids = FluentIterable.from(ImmutableList.<Long> of());
	Transaktion transaktion = new Transaktion(1L, ids, ids, DateTime.now(), 1L, false, Optional.<Preis> absent(), Optional.<Long> absent(),
		Optional.<Long> absent());
	Try<Long> save = transaktion.save();
	assertEquals(true, save.isSuccess());
    }

    @Test
    public void testUpdateExistingEntity() {
	FluentIterable<Long> ids = FluentIterable.from(ImmutableList.<Long> of());
	Transaktion Transaktion = new Transaktion(1L, ids, ids, DateTime.now(), 1L, false, Optional.<Preis> absent(), Optional.<Long> absent(),
		Optional.<Long> absent());
	Try<Long> save = Transaktion.save();
	Long entityId = save.get();
	Transaktion = new Transaktion(entityId, 1L, ids, ids, DateTime.now(), 1L, false, Optional.<Preis> absent(), Optional.<Long> absent(),
		Optional.<Long> absent());
	save = Transaktion.save();
	assertEquals(save.get(), Transaktion.getEntityId().get());
    }

    @Test
    public void testLoadExistingEntity() {
	FluentIterable<Long> ids = FluentIterable.from(ImmutableList.of(1L, 2L, 3L));
	Transaktion transaktion = new Transaktion(1L, ids, ids, DateTime.now(), 1L, false, Optional.<Preis> absent(), Optional.<Long> absent(),
		Optional.<Long> absent());
	Try<Long> save = transaktion.save();
	Long entityId = save.get();
	Optional<Transaktion> loadById = Transaktion.loadById(entityId);
	assertEquals(true, loadById.isPresent());
	assertEquals(false, loadById.get().isLaufkunde());
    }

    @Test
    public void testDeleteExistingEntity() {
	FluentIterable<Long> ids = FluentIterable.from(ImmutableList.of(1L, 2L, 3L));
	Transaktion transaktion = new Transaktion(1L, ids, ids, DateTime.now(), 1L, false, Optional.<Preis> absent(), Optional.<Long> absent(),
		Optional.<Long> absent());
	Try<Long> save = transaktion.save();
	Long entityId = save.get();
	Try<Unit> delete = Transaktion.delete(entityId, Transaktion.TABLENAME);
	assertEquals(true, delete.isSuccess());
	Optional<Transaktion> loadById = Transaktion.loadById(entityId);
	assertEquals(false, loadById.isPresent());
    }

    @Test
    public void testDeleteNotExistingEntity() {
	Try<Unit> delete = Transaktion.delete(1132L, Transaktion.TABLENAME);
	assertEquals(true, delete.isSuccess());
    }

    @Test
    public void testloadByParameter() {
	FluentIterable<Transaktion> loadByParameter = FluentIterable.from(Transaktion.loadByParameter("ID", "1"));
	assertEquals(false, loadByParameter.isEmpty());
	loadByParameter = FluentIterable.from(Transaktion.loadByParameter("EIDIE", "4"));
	assertEquals(true, loadByParameter.isEmpty());
    }

    @Test
    public void testLoadByParameterWithOrdering() {
	FluentIterable<Long> ids = FluentIterable.from(ImmutableList.of(1L, 2L, 3L));
	new Transaktion(1L, ids, ids, DateTime.now(), 1L, true, Optional.<Preis> absent(), Optional.<Long> absent(), Optional.<Long> absent()).save();
	new Transaktion(1L, ids, ids, DateTime.now(), 1L, false, Optional.<Preis> absent(), Optional.<Long> absent(), Optional.<Long> absent()).save();
	FluentIterable<Transaktion> load = FluentIterable.from(Transaktion.loadByParameter("FRISEUR_ID", "1", new Ordering("LAUFKUNDE", "DESC")));
	assertEquals(false, load.isEmpty());
	assertEquals(true, load.first().get().isLaufkunde());
    }

    @Test
    public void testLoadByParameterNotEveryEntry() {
	FluentIterable<Long> ids = FluentIterable.from(ImmutableList.of(1L, 2L, 3L));
	DateTime now = DateTime.now();
	new Transaktion(1L, ids, ids, now, 1L, true, Optional.<Preis> absent(), Optional.<Long> absent(), Optional.<Long> absent()).save();

	FluentIterable<Transaktion> load = FluentIterable.from(Transaktion.loadByParameter("DATUM", now.toString()));
	assertEquals(false, load.isEmpty());
	assertEquals(1, load.size());
	FluentIterable<Long> dlIds = load.get(0).getDienstleistungsIds();
	assertEquals(3, dlIds.size());
	assertEquals(1L, dlIds.first().get(), 0);
	FluentIterable<Long> vkIds = load.get(0).getVerkaufIds();
	assertEquals(3, vkIds.size());
	assertEquals(1L, vkIds.first().get(), 0);
    }

    @Test
    public void testLoadByParameterEveryEntry() {
	FluentIterable<Long> ids = FluentIterable.from(ImmutableList.of(1L, 2L, 3L));
	DateTime now = DateTime.now();
	new Transaktion(1L, ids, ids, now, 1L, true, Optional.<Preis> absent(), Optional.<Long> absent(), Optional.<Long> absent()).save();
	FluentIterable<Transaktion> load = FluentIterable.from(Transaktion.loadByParameter("'1'", "1"));
	assertEquals(false, load.isEmpty());
	int size = load.size();
	new Transaktion(1L, ids, ids, now, 1L, true, Optional.<Preis> absent(), Optional.<Long> absent(), Optional.<Long> absent()).save();
	load = FluentIterable.from(Transaktion.loadByParameter("'1'", "1"));
	assertEquals(size + 1, load.size());
    }

    @Test
    public void testLoadByParameterStartsWith() {
	FluentIterable<Long> ids = FluentIterable.from(ImmutableList.of(1L, 2L, 3L));
	DateTime now = DateTime.now();
	new Transaktion(1L, ids, ids, now, 1L, true, Optional.<Preis> absent(), Optional.<Long> absent(), Optional.<Long> absent()).save();

	FluentIterable<Transaktion> load = FluentIterable.from(Transaktion.loadByParameterStartsWith("LAUFKUNDE", "tr"));
	assertEquals(false, load.isEmpty());
	for (Transaktion transaktion : load) {
	    assertTrue(transaktion.isLaufkunde());
	}
    }

    @Test
    public void testLoadByParameterStartsWith_Ordering() {
	FluentIterable<Long> ids = FluentIterable.from(ImmutableList.of(1L, 2L, 3L));
	DateTime now = DateTime.now();
	new Transaktion(1L, ids, ids, now, 1L, true, Optional.<Preis> absent(), Optional.<Long> absent(), Optional.<Long> absent()).save();

	FluentIterable<Transaktion> load = FluentIterable.from(Transaktion.loadByParameterStartsWith("LAUFKUNDE", "tr", new Ordering("LAUFKUNDE", "ASC")));
	assertEquals(false, load.isEmpty());
	assertEquals(true, load.first().get().isLaufkunde());
	for (Transaktion transaktion : load) {
	    assertTrue(transaktion.isLaufkunde());
	}
    }
}
