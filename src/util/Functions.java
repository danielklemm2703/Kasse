package util;

import database.entities.Dienstleistung;
import database.entities.Entity;
import database.entities.Rezeptur;
import database.entities.Transaktion;
import datameer.com.google.common.base.Function;
import datameer.com.google.common.base.Optional;
import datameer.com.google.common.collect.FluentIterable;

public class Functions {

    public static final Function<String, Long> toLong = new Function<String, Long>() {
	@Override
	public Long apply(String input) {
	    if (input.equals("")) {
		return -1L;
	    }
	    return Long.parseLong(input.trim());
	}
    };

    public static final Function<Long, Optional<Dienstleistung>> toDienstleistung = new Function<Long, Optional<Dienstleistung>>() {
	@Override
	public Optional<Dienstleistung> apply(Long input) {
	    return Dienstleistung.loadById(input);
	}
    };

    public static final Function<Transaktion, Optional<Rezeptur>> toRezeptur = new Function<Transaktion, Optional<Rezeptur>>() {
	@Override
	public Optional<Rezeptur> apply(Transaktion input) {
	    System.err.println(input.getEntityId());
	    return FluentIterable.from(Rezeptur.loadByParameter("TRANSAKTION_ID", "" + input.getEntityId().get())).first();
	}
    };

    public static final Function<Optional<Rezeptur>, Rezeptur> toPresent = new Function<Optional<Rezeptur>, Rezeptur>() {
	@Override
	public Rezeptur apply(Optional<Rezeptur> input) {
	    return input.get();
	}
    };

    public static final Function<Entity, Optional<Long>> toEntityId = new Function<Entity, Optional<Long>>() {
	@Override
	public Optional<Long> apply(Entity input) {
	    return input.getEntityId();
	}
    };

    public static final <T> Function<Optional<T>, T> get(){
	return new Function<Optional<T>, T>() {
	    @Override
	    public T apply(Optional<T> input) {
		return input.get();
	    }
	};
    }
}
