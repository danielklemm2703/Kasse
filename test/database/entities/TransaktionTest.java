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
	FluentIterable<DienstleistungsInfo> dlInfo = FluentIterable.from(ImmutableList.<DienstleistungsInfo> of());
	FluentIterable<VerkaufsInfo> vkInfo = FluentIterable.from(ImmutableList.<VerkaufsInfo> of());
	Try<Long> transaktion = new Transaktion(dlInfo, vkInfo, DateTime.now(), Optional.<Preis> absent(), Optional.<Gutschein> absent(), Preis.of(0L)).save();
	assertEquals(true, transaktion.isSuccess());
	// TODO insert info test cases
    }

    @Test
    public void testUpdateExistingEntity() {
	FluentIterable<DienstleistungsInfo> dlInfo = FluentIterable.from(ImmutableList.<DienstleistungsInfo> of());
	FluentIterable<VerkaufsInfo> vkInfo = FluentIterable.from(ImmutableList.<VerkaufsInfo> of());
	Try<Long> save = new Transaktion(dlInfo, vkInfo, DateTime.now(), Optional.<Preis> absent(), Optional.<Gutschein> absent(), Preis.of(0L)).save();
	Long entityId = save.get();
	Transaktion transaktion = new Transaktion(entityId, dlInfo, vkInfo, DateTime.now(), Optional.<Preis> absent(), Optional.<Gutschein> absent(),
		Preis.of(0L));
	save = transaktion.save();
	assertEquals(save.get(), transaktion.getEntityId().get());
    }

    @Test
    public void testLoadExistingEntity() {
	FluentIterable<DienstleistungsInfo> dlInfo = FluentIterable.from(ImmutableList.<DienstleistungsInfo> of());
	FluentIterable<VerkaufsInfo> vkInfo = FluentIterable.from(ImmutableList.<VerkaufsInfo> of());
	Transaktion transaktion = new Transaktion(dlInfo, vkInfo, DateTime.now(), Optional.<Preis> absent(), Optional.<Gutschein> absent(), Preis.of(0L));
	Try<Long> save = transaktion.save();
	Long entityId = save.get();
	Optional<Transaktion> loadById = Transaktion.loadById(entityId);
	assertEquals(true, loadById.isPresent());
	assertEquals(transaktion.getDienstleistungInfos().isEmpty(), loadById.get().getDienstleistungInfos().isEmpty());
	assertEquals(transaktion.getVerkaufInfos().isEmpty(), loadById.get().getVerkaufInfos().isEmpty());
	assertEquals(transaktion.getDatum().getMillis(), loadById.get().getDatum().getMillis());
    }

    @Test
    public void testDeleteExistingEntity() {
	FluentIterable<DienstleistungsInfo> dlInfo = FluentIterable.from(ImmutableList.<DienstleistungsInfo> of());
	FluentIterable<VerkaufsInfo> vkInfo = FluentIterable.from(ImmutableList.<VerkaufsInfo> of());
	Transaktion transaktion = new Transaktion(dlInfo, vkInfo, DateTime.now(), Optional.<Preis> absent(), Optional.<Gutschein> absent(), Preis.of(0L));
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
	FluentIterable<DienstleistungsInfo> dlInfo = FluentIterable.from(ImmutableList.<DienstleistungsInfo> of());
	FluentIterable<VerkaufsInfo> vkInfo = FluentIterable.from(ImmutableList.<VerkaufsInfo> of());
	new Transaktion(dlInfo, vkInfo, DateTime.now(), Optional.<Preis> absent(), Optional.<Gutschein> absent(), Preis.of(0L)).save();
	new Transaktion(dlInfo, vkInfo, DateTime.now(), Optional.<Preis> absent(), Optional.<Gutschein> absent(), Preis.of(1L)).save();
	FluentIterable<Transaktion> load = FluentIterable.from(Transaktion.loadByParameter("GESAMT_UMSATZ", "1,00 EUR", new Ordering("GESAMT_UMSATZ", "DESC")));
	assertEquals(false, load.isEmpty());
	assertEquals(Preis.of(1L).toString(), load.first().get().getGesamtUmsatz().toString());
    }

    @Test
    public void testLoadByParameterNotEveryEntry() {
	DateTime now = DateTime.now();
	FluentIterable<DienstleistungsInfo> dlInfo = FluentIterable.from(ImmutableList.<DienstleistungsInfo> of());
	FluentIterable<VerkaufsInfo> vkInfo = FluentIterable.from(ImmutableList.<VerkaufsInfo> of());
	new Transaktion(dlInfo, vkInfo, now, Optional.<Preis> absent(), Optional.<Gutschein> absent(), Preis.of(0L)).save();

	FluentIterable<Transaktion> load = FluentIterable.from(Transaktion.loadByParameter("DATUM", now.toString()));
	assertEquals(false, load.isEmpty());
	assertEquals(1, load.size());
    }

    @Test
    public void testLoadByParameterEveryEntry() {
	DateTime now = DateTime.now();
	FluentIterable<DienstleistungsInfo> dlInfo = FluentIterable.from(ImmutableList.<DienstleistungsInfo> of());
	FluentIterable<VerkaufsInfo> vkInfo = FluentIterable.from(ImmutableList.<VerkaufsInfo> of());
	new Transaktion(dlInfo, vkInfo, now, Optional.<Preis> absent(), Optional.<Gutschein> absent(), Preis.of(0L)).save();
	FluentIterable<Transaktion> load = FluentIterable.from(Transaktion.loadByParameter("'1'", "1"));
	assertEquals(false, load.isEmpty());
	int size = load.size();
	new Transaktion(dlInfo, vkInfo, now, Optional.<Preis> absent(), Optional.<Gutschein> absent(), Preis.of(0L)).save();
	load = FluentIterable.from(Transaktion.loadByParameter("'1'", "1"));
	assertEquals(size + 1, load.size());
    }

    @Test
    public void testLoadByParameterStartsWith() {
	DateTime now = DateTime.now();
	FluentIterable<DienstleistungsInfo> dlInfo = FluentIterable.from(ImmutableList.<DienstleistungsInfo> of());
	FluentIterable<VerkaufsInfo> vkInfo = FluentIterable.from(ImmutableList.<VerkaufsInfo> of());
	new Transaktion(dlInfo, vkInfo, now, Optional.<Preis> absent(), Optional.<Gutschein> absent(), Preis.of(0L)).save();

	FluentIterable<Transaktion> load = FluentIterable.from(Transaktion.loadByParameterStartsWith("GESAMT_UMSATZ", "0"));
	assertEquals(false, load.isEmpty());
	for (Transaktion transaktion : load) {
	    assertTrue(transaktion.getGesamtUmsatz().toString().startsWith("0"));
	}
    }

    @Test
    public void testLoadByParameterStartsWith_Ordering() {
	DateTime now = DateTime.now();
	FluentIterable<DienstleistungsInfo> dlInfo = FluentIterable.from(ImmutableList.<DienstleistungsInfo> of());
	FluentIterable<VerkaufsInfo> vkInfo = FluentIterable.from(ImmutableList.<VerkaufsInfo> of());
	new Transaktion(dlInfo, vkInfo, now, Optional.<Preis> absent(), Optional.<Gutschein> absent(), Preis.of(0L)).save();

	FluentIterable<Transaktion> load = FluentIterable.from(Transaktion.loadByParameterStartsWith("GESAMT_UMSATZ", "0", new Ordering("DATUM", "DESC")));
	assertEquals(false, load.isEmpty());
	assertEquals(now.getMillis(), load.first().get().getDatum().getMillis());
	for (Transaktion transaktion : load) {
	    assertTrue(transaktion.getGesamtUmsatz().toString().startsWith("0"));
	}
    }
}
