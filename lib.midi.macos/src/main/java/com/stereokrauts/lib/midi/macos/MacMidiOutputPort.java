package com.stereokrauts.lib.midi.macos;

import com.stereokrauts.lib.midi.api.IMidiOutputPort;

public class MacMidiOutputPort extends MacMidiPort implements IMidiOutputPort {

	public MacMidiOutputPort(MacMidiEntity entity) {
		super(entity);
	}

}
