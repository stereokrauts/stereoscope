package com.stereokrauts.lib.midi.api;

import javax.sound.midi.MidiDevice;

public interface IMidiPort {
	String getName();
	MidiDevice.Info getInfo();
}