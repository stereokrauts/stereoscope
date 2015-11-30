package com.stereokrauts.lib.midi.macos;

import javax.sound.midi.MidiDevice.Info;

import com.stereokrauts.lib.midi.api.IMidiPort;

public abstract class MacMidiPort implements IMidiPort {
	private final MacMidiEntity entity;

	public MacMidiPort(final MacMidiEntity entity) {
		this.entity = entity;
	}

	@Override
	public String getName() {
		return getEntity().getDisplayName();
	}

	@Override
	public Info getInfo() {
		return null;
	}

	@Override
	public String toString() {
		return String.format("%s", getName());
	}

	MacMidiEntity getEntity() {
		return entity;
	}

}
