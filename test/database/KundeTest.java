package database;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTime;
import org.junit.Test;

import util.Try;
import datameer.com.google.common.base.Optional;

public class KundeTest {

    @Test
    public void testSaveNewEntity() {
	DateTime now = DateTime.now();
	Kunde horst = new Kunde("horst", 12, 12.4F, now);
	Try<Long> save = horst.save();
	assertEquals(true, save.isSuccess());
    }

    @Test
    public void testUpdateExistingEntity() {
	DateTime now = DateTime.now();
	Kunde horst = new Kunde("horst", 12, 12.4F, now);
	Try<Long> save = horst.save();
	Long entityId = save.get();
	horst = new Kunde(entityId, "horsty", 13, 12.4F, now);
	save = horst.save();
	assertEquals(save.get(), horst.getEntityId().get());
    }

    @Test
    public void testLoadExistingEntity() {
	DateTime now = DateTime.now();
	Kunde horst = new Kunde("horst", 12, 12.4F, now);
	Try<Long> save = horst.save();
	Long entityId = save.get();
	Optional<Kunde> loadById = Kunde.loadById(entityId);
	assertEquals(true, loadById.isPresent());
	assertEquals(12, loadById.get().getAge());
    }
}
