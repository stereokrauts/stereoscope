package com.stereokrauts.lib.midi.macos;

import com.stereokrauts.lib.midi.api.IMidiInputPort;

public class MacMidiInputPort extends MacMidiPort implements IMidiInputPort {

	public MacMidiInputPort(MacMidiEntity entity) {
		super(entity);
	}

}
