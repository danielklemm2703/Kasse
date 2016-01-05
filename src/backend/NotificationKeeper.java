package backend;

import datameer.com.google.common.base.Optional;

public class NotificationKeeper {

    public Optional<TypedJFrame> _notification;

    public NotificationKeeper() {
	_notification = Optional.absent();
    }
}
