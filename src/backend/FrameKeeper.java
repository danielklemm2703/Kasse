package backend;

import datameer.com.google.common.base.Optional;

public class FrameKeeper {
    public Optional<TypedJFrame> _openFrame;

    public FrameKeeper() {
	_openFrame = Optional.absent();
    }
}
