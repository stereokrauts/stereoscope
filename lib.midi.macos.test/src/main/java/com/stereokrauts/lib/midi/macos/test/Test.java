package com.stereokrauts.lib.midi.macos.test;

import java.util.Arrays;

import com.stereokrauts.lib.binary.ByteStringConversion;
import com.stereokrauts.lib.midi.api.IMidiInput;
import com.stereokrauts.lib.midi.api.IMidiInputPort;
import com.stereokrauts.lib.midi.api.IMidiSubsystem;
import com.stereokrauts.lib.midi.api.IReactToMidi;
import com.stereokrauts.lib.midi.api.MidiException;

public final class Test {
	private long packetCounter = 0;
	private long byteCounter = 0;
	
	public Test() {
		
	}
	
	public void setMidiClient(MidiClient midiClient) throws MidiException, InterruptedException {
		Thread.sleep(10000);
		System.out.println("In Activator!");
		IMidiSubsystem midiSubsystem = MidiClient.getMidiSubsystem();
		IMidiInputPort[] inputPorts = midiSubsystem.getInputPorts();
		
		System.err.println(Arrays.toString(inputPorts));
		
		IMidiInput input = midiSubsystem.getMidiInput(inputPorts[3]);
		System.err.println("Using " + input.getName());
		
		IReactToMidi myHandler = new IReactToMidi() {
			@Override
			public void handleSysex(byte[] message) {
				packetCounter++;
				byteCounter += message.length;
				
				System.err.println(String.format("Packet %-4d    Length %-8d: %s", packetCounter, byteCounter, ByteStringConversion.toHex(message)));
			}
			
			@Override
			public void handleProgramChange(int newProgram) {
				// TODO Auto-generated method stub
				
			}
		};
		input.setHandler(myHandler);
		Thread.sleep(10000000);
	}

}
