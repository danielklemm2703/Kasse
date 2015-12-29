package database;

public final class Ordering {
    public String _orderBy;
    public String _direction;

    public Ordering(final String orderBy, final String direction) {
	_orderBy = orderBy;
	_direction = direction;
    }
}
