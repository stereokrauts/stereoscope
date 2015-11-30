package model.protocol.osc.touchosc;

import java.util.HashMap;
import java.util.List;

public class SnappingFaderManager {
	private final HashMap<Object, SnappingFader> faders = new HashMap<>();
	private final HashMap<Object, Boolean> updateStrategyIsForced = new HashMap<>();
	private boolean active;

	public SnappingFaderManager(final boolean isActive, final List<? extends Object> identifiers) {
		active = isActive;
		for (int i = 0; i < identifiers.size(); i++) {
			faders.put(identifiers.get(i), new SnappingFader(0));
			updateStrategyIsForced.put(identifiers.get(i), Boolean.FALSE);
		}
	}

	public void setSnapFaderActive(final boolean active) {
		this.active = active;
	}

	public void tryUpdate(final Object index, final double value, final ISnappingFaderEventHandler handler) {
		if (active) {
			if (isForced(index)) {
				forceUpdate(index, value);
				handler.snapSucceeded();
			} else {
				faders.get(index).tryUpdate(value, handler);
			}
		} else {
			handler.snapSucceeded();
		}
	}

	public void forceUpdate(final Object index, final double value) {
		faders.get(index).forceUpdate(value);
	}

	public void updateStrategy_ForceWhenHit(final Object index, final float receivedValue) {
		if (faders.get(index).isInBoundary(receivedValue)) {
			updateStrategyIsForced.put(index, Boolean.TRUE);
		}
	}

	public void updateStrategy_Try(final Object index) {
		updateStrategyIsForced.put(index, Boolean.FALSE);
	}

	private boolean isForced(final Object index) {
		return updateStrategyIsForced.get(index);
	}
}
