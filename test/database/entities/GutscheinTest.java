package database.entities;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import util.Preis;
import util.Try;
import util.Unit;
import database.Ordering;
import datameer.com.google.common.base.Optional;
import datameer.com.google.common.collect.FluentIterable;

public class GutscheinTest {

    @Test
    public void testSaveNewEntity() {
	Gutschein gutschein = new Gutschein(4L, 4L, Preis.of("12,33"));
	Try<Long> save = gutschein.save();
	assertEquals(true, save.isSuccess());
    }

    @Test
    public void testUpdateExistingEntity() {
	Gutschein gutschein = new Gutschein(4L, 4L, Preis.of("12,33"));
	Try<Long> save = gutschein.save();
	Long entityId = save.get();
	gutschein = new Gutschein(entityId, 4L, 4L, Preis.of("12,33"));
	save = gutschein.save();
	assertEquals(save.get(), gutschein.getEntityId().get());
    }

    @Test
    public void testLoadExistingEntity() {
	Gutschein gutschein = new Gutschein(4L, 4L, Preis.of("12,33"));
	Try<Long> save = gutschein.save();
	Long entityId = save.get();
	Optional<Gutschein> loadById = Gutschein.loadById(entityId);
	assertEquals(true, loadById.isPresent());
	assertEquals("12,33 EUR", loadById.get().getRestWert().toString());
    }

    @Test
    public void testDeleteExistingEntity() {
	Gutschein gutschein = new Gutschein(4L, 4L, Preis.of("12,33"));
	Try<Long> save = gutschein.save();
	Long entityId = save.get();
	Try<Unit> delete = Gutschein.delete(entityId, Gutschein.TABLENAME);
	assertEquals(true, delete.isSuccess());
	Optional<Gutschein> loadById = Gutschein.loadById(entityId);
	assertEquals(false, loadById.isPresent());
    }

    @Test
    public void testDeleteNotExistingEntity() {
	Try<Unit> delete = Gutschein.delete(1132L, Gutschein.TABLENAME);
	assertEquals(true, delete.isSuccess());
    }

    @Test
    public void testloadByParameter() {
	FluentIterable<Gutschein> loadByParameter = FluentIterable.from(Gutschein.loadByParameter("ID", "1"));
	assertEquals(false, loadByParameter.isEmpty());
	loadByParameter = FluentIterable.from(Gutschein.loadByParameter("EIDIE", "4"));
	assertEquals(true, loadByParameter.isEmpty());
    }

    @Test
    public void testLoadByParameterWithOrdering() {
	new Gutschein(1L, 1L, Preis.of(22D)).save();
	new Gutschein(1L, 1L, Preis.of(9900000D)).save();
	FluentIterable<Gutschein> load = FluentIterable.from(Gutschein.loadByParameter("RESTWERT", Preis.of(9900000D).toString(), new Ordering("RESTWERT",
		"DESC")));
	assertEquals(false, load.isEmpty());
	assertEquals(Preis.of(9900000D).toString(), load.first().get().getRestWert().toString());
    }

    @Test
    public void testLoadByParameterNotEveryEntry() {
	Preis preis = Preis.of(Math.random());
	new Gutschein(1L, 1L, preis).save();

	FluentIterable<Gutschein> load = FluentIterable.from(Gutschein.loadByParameter("RESTWERT", preis.toString()));
	assertEquals(false, load.isEmpty());
	assertEquals(1, load.size());
    }

    @Test
    public void testLoadByParameterEveryEntry() {
	new Gutschein(1L, 1L, Preis.of(22D)).save();
	FluentIterable<Gutschein> load = FluentIterable.from(Gutschein.loadByParameter("'1'", "1"));
	assertEquals(false, load.isEmpty());
	int size = load.size();
	new Gutschein(1L, 1L, Preis.of(22D)).save();
	load = FluentIterable.from(Gutschein.loadByParameter("'1'", "1"));
	assertEquals(size + 1, load.size());
    }
}
