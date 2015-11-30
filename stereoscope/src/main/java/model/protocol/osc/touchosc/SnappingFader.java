package model.protocol.osc.touchosc;

public class SnappingFader {
	static final float SNAP_DELTA = 0.1f;
	private double currentRealValue;

	public SnappingFader(final double currentRealValue) {
		this.currentRealValue = currentRealValue;
	}

	public void tryUpdate(final double value, final ISnappingFaderEventHandler handler) {
		if (isInBoundary(value)) {
			currentRealValue = value;
			handler.snapSucceeded();
		} else {
			handler.snapFailed();
		}
	}

	public void forceUpdate(final double value) {
		currentRealValue = value;
	}

	public boolean isInBoundary(final double receivedValue) {
		return Math.abs(receivedValue - currentRealValue) <= SNAP_DELTA;
	}
}
