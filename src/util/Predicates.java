package util;

import database.entities.Dienstleistung;
import database.entities.Rezeptur;
import datameer.com.google.common.base.Optional;
import datameer.com.google.common.base.Predicate;

public class Predicates {
    public static final Predicate<String> without(final String forbidden) {
	return new Predicate<String>() {
	    @Override
	    public boolean apply(String input) {
		return !input.equals(forbidden);
	    }
	};
    }

    public static final Predicate<Pair<String, String>> withoutSecond(final String forbidden) {
	return new Predicate<Pair<String, String>>() {
	    @Override
	    public boolean apply(Pair<String, String> input) {
		return !input._2.equals(forbidden);
	    }
	};
    }

    // TODO make it work again
    // public static final Predicate<Transaktion> withRezept = new
    // Predicate<Transaktion>() {
    // @Override
    // public boolean apply(Transaktion transaktion) {
    // FluentIterable<Long> dienstleistungsIds =
    // transaktion.getDienstleistungsIds();
    // FluentIterable<Optional<Dienstleistung>> filter =
    // dienstleistungsIds.transform(toDienstleistung).filter(rezepturpflicht);
    // return !filter.isEmpty();
    // }
    // };

    public static final Predicate<Rezeptur> eingetragen = new Predicate<Rezeptur>() {
	@Override
	public boolean apply(Rezeptur input) {
	    return input.isBereitsEingetragen();
	}
    };

    public static final Predicate<Optional<Dienstleistung>> rezepturpflicht = new Predicate<Optional<Dienstleistung>>() {
	@Override
	public boolean apply(Optional<Dienstleistung> input) {
	    return input.isPresent() && input.get().isRezepturplichtig();
	}
    };

    public static final <T> Predicate<Try<T>> isSuccess() {
	return new Predicate<Try<T>>() {
	    @Override
	    public boolean apply(Try<T> input) {
		return input.isSuccess();
	    }
	};
    }

    public static final <T> Predicate<Optional<T>> isPresent() {
	return new Predicate<Optional<T>>() {
	    @Override
	    public boolean apply(Optional<T> input) {
		return input.isPresent();
	    }
	};
    }
}
