package database.entities;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import util.Preis;
import util.Try;
import util.Unit;
import datameer.com.google.common.base.Optional;

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
}
