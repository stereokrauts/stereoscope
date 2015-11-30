package com.stereokrauts.stereoscope.mixer.yamaha.pm5d;

import java.util.logging.Level;

import javax.sound.midi.InvalidMidiDataException;

import model.beans.ConnectionMidiBean;
import model.beans.MixerConnectionBean;
import model.mixer.interfaces.IAmMixer;

import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.lib.midi.api.IMidiInput;
import com.stereokrauts.lib.midi.api.IMidiInputPort;
import com.stereokrauts.lib.midi.api.IMidiOutput;
import com.stereokrauts.lib.midi.api.IMidiOutputPort;
import com.stereokrauts.lib.midi.api.ISendMidi;
import com.stereokrauts.lib.midi.api.MidiException;
import com.stereokrauts.plugin.midibridge.Midi;
import com.stereokrauts.stereoscope.gui.metrics.model.ICommunicationAware;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageReceiver;
import com.stereokrauts.stereoscope.plugin.interfaces.AbstractApplicationContext;
import com.stereokrauts.stereoscope.plugin.interfaces.IMixerPlugin;
import com.stereokrauts.stereoscope.plugin.interfaces.IPersistentPluginConfiguration;

public class Pm5dPlugin implements IMixerPlugin {
	private final AbstractApplicationContext applContext;
	public AbstractApplicationContext getApplContext() {
		return this.applContext;
	}

	private final Pm5dMixer mixer;
	private final IMidiInput input;
	private final IMidiOutput output;

	private static final SLogger LOG = StereoscopeLogManager.getLogger(Pm5dPlugin.class);
	private static final String ID = "stereoscope.mixer.yamaha.pm5d";

	public Pm5dPlugin(final AbstractApplicationContext ctx, final IMidiInputPort inputPort, final IMidiOutputPort outputPort) throws MidiException {
		this.applContext = ctx;
		this.input = Midi.getMidiSubsystem().getMidiInput(inputPort);
		this.output = Midi.getMidiSubsystem().getMidiOutput(outputPort);
		this.mixer = new Pm5dMixer(this, new ISendMidi() {
			@Override
			public void sendSysexData(final byte[] sysexdata) {
				try {
					Pm5dPlugin.this.output.sendSysexMessage(sysexdata);
				} catch (final InvalidMidiDataException e) {
					LOG.log(Level.WARNING, "Could not send MIDI message", e);
				}
			}

			@Override
			public void activeSensing() {
				/* not needed here */
			}
		});

		final Pm5dMidiReceiver receiver = new Pm5dMidiReceiver(this.mixer);
		this.input.setHandler(receiver);

		mixer.requestSamplerate();
		mixer.updateChannelNames();
	}

	@Override
	public IPersistentPluginConfiguration getPersistableConfiguration() {
		return new IPersistentPluginConfiguration() {
			@Override
			public MixerConnectionBean getConnectionBean() {
				final MixerConnectionBean bean = new MixerConnectionBean();
				final ConnectionMidiBean midiConnection = new ConnectionMidiBean();
				midiConnection.setInputPortName(Pm5dPlugin.this.input.getName());
				midiConnection.setOutputPortName(Pm5dPlugin.this.output.getName());
				bean.setMidiConnection(midiConnection);
				return bean;
			}
		};
	}

	@Override
	public IMessageReceiver getMessageEndpoint() {
		return this.mixer;
	}

	@Override
	public String getPluginName() {
		return Pm5dPlugin.ID;
	}

	@Override
	public IAmMixer getMixer() {
		return this.mixer;
	}

	@Override
	public void shutdown() {
		this.input.close();
		this.output.close();
	}

	@Override
	public void registerCommunicationAware(final ICommunicationAware communicationAware) {
		this.mixer.registerCommunicationAware(communicationAware);
	}

	@Override
	public String getDisplayName() {
		return "Yamaha PM5D";
	}
}
