package com.stereokrauts.stereoscope.gui.document;

public interface BusLayoutAware {
	public enum LayoutPosition {
		TOP,
		BOTTOM
	}
	void setBusLayoutPosition(LayoutPosition position);
}
