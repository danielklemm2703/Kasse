package database;

import java.util.LinkedList;

import org.joda.time.DateTime;

import util.Pair;
import util.Predicates;
import util.Try;
import datameer.com.google.common.base.Optional;
import datameer.com.google.common.base.Supplier;
import datameer.com.google.common.collect.FluentIterable;
import datameer.com.google.common.collect.ImmutableList;
import datameer.com.google.common.collect.Lists;

public class Kunde extends Entity {

    private String _name;
    private int _age;
    private float _size;
    private DateTime _birthday;
    private static final ImmutableList<String> keys = ImmutableList.<String> builder().add("TABLENAME").add("NAME").add("AGE").add("SIZE").add("BIRTHDAY")
	    .build();
    private static final String TABLENAME = Kunde.class.getSimpleName();
   

    @Override
    public Iterable<Pair<String, String>> persistenceContext() {
	LinkedList<Pair<String, String>> list = Lists.newLinkedList();
	// Table name must always be first!
	list.add(Pair.of(keys.get(0), this.getTableName()));
	list.add(Pair.of(keys.get(1), this.getName()));
	list.add(Pair.of(keys.get(2), "" + this.getAge()));
	list.add(Pair.of(keys.get(3), Float.toString(this.getSize())));
	list.add(Pair.of(keys.get(4), this.getBirthday().toString()));
	return FluentIterable.from(list);
    }

    private Kunde(final Long entityId) {
	super(entityId, TABLENAME);
    }

    public Kunde(String name, int age, float size, DateTime birthday) {
	super(TABLENAME);
	_name = name;
	_age = age;
	_size = size;
	_birthday = birthday;
    }

    public Kunde(Long entityId, String name, int age, float size, DateTime birthday) {
	super(entityId, TABLENAME);
	_name = name;
	_age = age;
	_size = size;
	_birthday = birthday;
    }

    public String getName() {
	return _name;
    }

    public void setName(String name) {
	_name = name;
    }

    public int getAge() {
	return _age;
    }

    public void setAge(int age) {
	_age = age;
    }

    public float getSize() {
	return _size;
    }

    public void setSize(float size) {
	_size = size;
    }

    public DateTime getBirthday() {
	return _birthday;
    }

    public void setBirthday(DateTime birthday) {
	_birthday = birthday;
    }

    public static final Optional<Kunde> loadById(long entityId) {
	Try<Pair<Long, Iterable<Pair<String, String>>>> loadContext = loadContext(entityId, TABLENAME, keys);
	if (loadContext.isFailure()) {
	    System.err.println(String.format("could not load Entity with id '%s', from table '%s', reason:", "" + entityId, TABLENAME));
	    System.err.println(loadContext.failure().getLocalizedMessage());
	    return Optional.absent();
	}
	Pair<Long, Iterable<Pair<String, String>>> context = loadContext.get();
	if (FluentIterable.from(context._2).isEmpty()) {
	    System.err.println(String.format("could not find Entity with id '%s', in table '%s', is it even persisted?", "" + entityId, TABLENAME));
	    return Optional.absent();
	}
	Try<Kunde> kunde = build(context);
	if (kunde.isFailure()) {
	    System.err.println(String.format("could not parse Entity with id '%s', from table '%s', reason:", "" + entityId, TABLENAME));
	    System.err.println(kunde.failure().getLocalizedMessage());
	    return Optional.absent();
	}
	return Optional.of(kunde.get());
    }

    private static final Try<Kunde> build(final Pair<Long, Iterable<Pair<String, String>>> context) {
	return Try.of(new Supplier<Kunde>() {
	    @Override
	    public Kunde get() {
		Kunde kunde = new Kunde(context._1);
		ImmutableList<Pair<String, String>> values = ImmutableList.copyOf(FluentIterable.from(context._2).filter(Predicates.withoutSecond(TABLENAME)));
		kunde.setName(values.get(0)._2);
		kunde.setAge(Integer.parseInt(values.get(1)._2));
		kunde.setSize(Float.parseFloat(values.get(2)._2));
		kunde.setBirthday(DateTime.parse(values.get(3)._2));
		return kunde;
	    }
	});
    }
}
