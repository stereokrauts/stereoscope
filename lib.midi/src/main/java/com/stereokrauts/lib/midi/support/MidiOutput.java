package com.stereokrauts.lib.midi.support;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;

import com.stereokrauts.lib.binary.ByteStringConversion;
import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.lib.midi.api.IMidiOutput;

/**
 * This class is responsible for translating between the stereoscope MIDI
 * subsystem and the java sound API.
 * @author Tobias Heide &lt;tobi@s-hei.de&gt;
 */
public final class MidiOutput implements IMidiOutput {
	/**
	 * The logger for this subsystem.
	 */
	private static final SLogger LOGGER = StereoscopeLogManager.getLogger("model");

	/**
	 * The sound api class receiving messages from the stereoscope software.
	 */
	private final Receiver recv;
	/**
	 * The associated device.
	 */
	private final MidiDevice myDevice;
	/**
	 * Do we currently have an open session?
	 */
	private boolean isOpen = false;
	
	/**
	 * Private Queue for communication between the asynchronus reception of
	 * MIDI events and the handling of these messages in the stereoscope
	 * software.
	 */
	private final BlockingQueue<SysexMessage> messages;
	
	/**
	 * This class runs in an extra thread and sends all messages to the
	 * java sound API.
	 * @author Tobias Heide &lt;tobi@s-hei.de&gt;
	 *
	 */
	protected final class AsyncMidiSender implements Runnable {
		/**
		 * Only true when shutdown requested.
		 */
		private boolean done = false;

		@Override
		public void run() {
			try {
				while (!this.done) {
					final SysexMessage m = MidiOutput.this.messages.take();
					
					MidiOutput.this.recv.send(m, -1);
				}
			} catch (final InterruptedException e) {
				LOGGER.severe("AsyncMidiSender interrupted cruely: "
						+ e.getMessage());
			}
		}
		
		/**
		 * Called from an external function when a shutdown of the
		 * application has been requested by the user. Does not need to
		 * be synchronized as it is the only function that writes to
		 * the done variable.
		 */
		public void shutdown() {
			this.done = true;
		}
	}
	
	/**
	 * instance of the above internal class.
	 */
	private final AsyncMidiSender asm;
	
	/**
	 * Creates a new MIDI sending object.
	 * @param d The device to sent messages to
	 * @throws MidiUnavailableException Any error from the java sound api.
	 */
	public MidiOutput(final MidiDevice d) throws MidiUnavailableException {
		this.messages = new LinkedBlockingQueue<SysexMessage>();
		this.asm = new AsyncMidiSender();
		final Thread t = new Thread(this.asm);
		t.setName("Asynchronous MIDI Sender");
		t.start();
		
		this.recv = d.getReceiver();
		this.myDevice = d;
		this.isOpen = true;
	}
	
	/* (non-Javadoc)
	 * @see com.stereokrauts.lib.midi.standard.IMidiOutput#sendSysexMessage(byte[])
	 */
	@Override
	public void sendSysexMessage(final byte[] message)
	throws InvalidMidiDataException {
		LOGGER.finest("MIDI sent: " + ByteStringConversion.toHex(message));
		final SysexMessage sm = new SysexMessage();
		sm.setMessage(message, message.length);
		try {
			this.messages.put(sm);
		} catch (final InterruptedException e) {
			LOGGER.severe("Midi Output Buffer overrun:" + e.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.stereokrauts.lib.midi.standard.IMidiOutput#sendActiveSensing()
	 */
	@Override
	public void sendActiveSensing() {
		final ShortMessage sm = new ShortMessage();
		try {
			sm.setMessage(ShortMessage.ACTIVE_SENSING);
			this.recv.send(sm, -1);
		} catch (final Exception e) {
			/* ignore */
			LOGGER.info("Active sensing message dropped: " + e.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.stereokrauts.lib.midi.standard.IMidiOutput#close()
	 */
	@Override
	public void close() {
		if (this.isOpen) {
			//recv.close();
			if (this.myDevice.isOpen()) {
				this.myDevice.close();
			}
			this.isOpen = false;
			this.asm.shutdown();
		}
	}

	@Override
	public String getName() {
		return this.myDevice.getDeviceInfo().getName();
	}
}
