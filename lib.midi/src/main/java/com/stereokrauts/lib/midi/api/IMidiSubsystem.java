package com.stereokrauts.lib.midi.api;


public interface IMidiSubsystem {
	IMidiOutputPort[] getOutputPorts();
	IMidiInputPort[] getInputPorts();
	IMidiOutput getMidiOutput(final IMidiOutputPort p) throws MidiException;
	IMidiInput getMidiInput(final IMidiInputPort p) throws MidiException;
}