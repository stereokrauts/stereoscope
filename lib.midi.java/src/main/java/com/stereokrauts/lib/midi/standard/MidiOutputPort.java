package com.stereokrauts.lib.midi.standard;

import javax.sound.midi.MidiDevice.Info;

import com.stereokrauts.lib.midi.api.IMidiOutputPort;

public class MidiOutputPort extends MidiPort implements IMidiOutputPort {

	public MidiOutputPort(String myName, Info myInfo) {
		super(myName, myInfo);
	}

}
