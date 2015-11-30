package com.stereokrauts.stereoscope.mixer.yamaha.dm1000;

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

public class DM1000Plugin implements IMixerPlugin {
	private final AbstractApplicationContext applContext;
	public AbstractApplicationContext getApplContext() {
		return this.applContext;
	}

	private final DM1000Mixer mixer;
	private final IMidiInput input;
	private final IMidiOutput output;

	private static final SLogger LOG = StereoscopeLogManager.getLogger(DM1000Plugin.class);
	private static final String ID = "stereoscope.mixer.yamaha.dm1000";

	public DM1000Plugin(final AbstractApplicationContext ctx, final IMidiInputPort inputPort, final IMidiOutputPort outputPort) throws MidiException {
		this.applContext = ctx;
		this.input = Midi.getMidiSubsystem().getMidiInput(inputPort);
		this.output = Midi.getMidiSubsystem().getMidiOutput(outputPort);
		this.mixer = new DM1000Mixer(this, new ISendMidi() {
			@Override
			public void sendSysexData(final byte[] sysexdata) {
				try {
					DM1000Plugin.this.output.sendSysexMessage(sysexdata);
				} catch (final InvalidMidiDataException e) {
					LOG.log(Level.WARNING, "Could not send MIDI message", e);
				}
			}

			@Override
			public void activeSensing() {
				/* not needed here */
			}
		});

		final DM1000MidiReceiver receiver = new DM1000MidiReceiver(this.mixer);
		this.input.setHandler(receiver);

		mixer.updateChannelNames();
		mixer.requestSamplerate();
	}

	@Override
	public IPersistentPluginConfiguration getPersistableConfiguration() {
		return new IPersistentPluginConfiguration() {
			@Override
			public MixerConnectionBean getConnectionBean() {
				final MixerConnectionBean bean = new MixerConnectionBean();
				final ConnectionMidiBean midiConnection = new ConnectionMidiBean();
				midiConnection.setInputPortName(DM1000Plugin.this.input.getName());
				midiConnection.setOutputPortName(DM1000Plugin.this.output.getName());
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
		return DM1000Plugin.ID;
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
		return "Yamaha DM1000";
	}
}
