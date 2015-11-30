package com.stereokrauts.stereoscope.plugin.ui;

public interface MidiPortSelectionResult {
	Object getMidiInputPort();
	Object getMidiOutputPort();
	boolean isClosedUsingOkay();
}
