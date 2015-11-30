package com.stereokrauts.lib.midi.standard;

import javax.sound.midi.MidiDevice.Info;

import com.stereokrauts.lib.midi.api.IMidiInputPort;

public class MidiInputPort extends MidiPort implements IMidiInputPort {

	public MidiInputPort(String myName, Info myInfo) {
		super(myName, myInfo);
	}

}
