package com.stereokrauts.stereoscope.plugin.gui;

import com.stereokrauts.stereoscope.plugin.ui.MidiPortSelectionResult;

public class EclipseMidiPortSelectionResult implements MidiPortSelectionResult {
	private final Object midiInputPort;
	private final Object midiOutputPort;
	private final boolean closedUsingOkay;

	public EclipseMidiPortSelectionResult(final Object midiInputPort, final Object midiOutputPort, final boolean closedUsingOkay) {
		this.midiInputPort = midiInputPort;
		this.midiOutputPort = midiOutputPort;
		this.closedUsingOkay = closedUsingOkay;
	}

	@Override
	public Object getMidiInputPort() {
		return this.midiInputPort;
	}
	@Override
	public Object getMidiOutputPort() {
		return this.midiOutputPort;
	}
	@Override
	public boolean isClosedUsingOkay() {
		return this.closedUsingOkay;
	}
}
