package com.stereokrauts.lib.midi;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

import org.junit.Before;
import org.junit.Test;

import com.stereokrauts.lib.midi.api.IMidiInput;
import com.stereokrauts.lib.midi.api.IReactToMidi;
import com.stereokrauts.lib.midi.support.MidiInput;


public class MidiInputTest {
	private static class MidiDeviceImpl implements MidiDevice {
		public MidiDeviceImpl() {
			this.trs = new Transmitter() {
				private Receiver rcv;
				@Override
				public void setReceiver(final Receiver receiver) {
					this.rcv = receiver;
				}
				
				@Override
				public Receiver getReceiver() {
					return this.rcv;
				}
				
				@Override
				public void close() {}
			};
			
			this.rcv = new Receiver() {
				@Override
				public void send(final MidiMessage message, final long timeStamp) {
				}
				
				@Override
				public void close() {}
			};
		}
		private Receiver rcv;
		private Transmitter trs;
		@Override
		public Info getDeviceInfo() {
			return new MidiDevice.Info("Internal", "Internal", "Internal", "Internal") { };
		}

		@Override
		public int getMaxReceivers() {
			return 1;
		}

		@Override
		public int getMaxTransmitters() {
			return 1;
		}

		@Override
		public long getMicrosecondPosition() {
			return 0;
		}

		@Override
		public Receiver getReceiver() throws MidiUnavailableException {
			return this.rcv;
		}

		@Override
		public List<Receiver> getReceivers() {
			return Arrays.asList(this.rcv);
		}

		@Override
		public Transmitter getTransmitter() throws MidiUnavailableException {
			return this.trs;
		}

		@Override
		public List<Transmitter> getTransmitters() {
			return Arrays.asList(this.trs);
		}

		@Override
		public boolean isOpen() {
			return true;
		}
		
		@Override
		public void open() throws MidiUnavailableException {		}
		
		@Override
		public void close() {}
	}
	
	private int lastReceived = 0;
	private MidiDevice dev;
	private IMidiInput dut;
	private IReactToMidi dummy;
	
	@Before
	public void setup() throws Exception
	{
		this.dev = new MidiDeviceImpl();
		this.dut = new MidiInput(this.dev);
		this.dummy = new IReactToMidi() {
			@Override
			public void handleSysex(final byte[] message) {
				MidiInputTest.this.lastReceived = 1;
			}
			
			@Override
			public void handleProgramChange(final int newProgram) {
				MidiInputTest.this.lastReceived = 2;
			}
		};
		this.dut.setHandler(this.dummy);
	}

	@Test
	public void testSysex() throws Exception
	{
		this.dev.getTransmitter().getReceiver().send(new MidiMessage(new byte[] { (byte) 0xF0, (byte) 0x36 }) {
			@Override
			public Object clone() {
				return null;
			}
		}, 0L);
		
		assertEquals(1, this.lastReceived);
	}
	
	@Test
	public void testProgramChange() throws Exception
	{
		this.dev.getTransmitter().getReceiver().send(new MidiMessage(new byte[] { (byte) 0xC2, (byte) 0x36 }) {
			@Override
			public Object clone() {
				return null;
			}
		}, 0L);
		
		assertEquals(2, this.lastReceived);
	}
}
