package com.stereokrauts.stereoscope.mixer.yamaha.y02r96;

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

/**
 * This class represents a Yamaha 02R96 mixer plugin.
 * @author th
 *
 */
public final class Y02r96Plugin implements IMixerPlugin {
	private final AbstractApplicationContext applContext;

	public AbstractApplicationContext getApplContext() {
		return this.applContext;
	}

	private final Y02r96Mixer mixer;
	private final IMidiInput input;
	private final IMidiOutput output;

	private static final SLogger LOG = StereoscopeLogManager.getLogger(Y02r96Plugin.class);
	private static final String ID = "stereoscope.mixer.yamaha.y02r96";

	public Y02r96Plugin(final AbstractApplicationContext ctx, final IMidiInputPort inputPort, final IMidiOutputPort outputPort) throws MidiException {
		this.applContext = ctx;
		this.input = Midi.getMidiSubsystem().getMidiInput(inputPort);
		this.output = Midi.getMidiSubsystem().getMidiOutput(outputPort);
		this.mixer = new Y02r96Mixer(this, new ISendMidi() {
			@Override
			public void sendSysexData(final byte[] sysexdata) {
				try {
					Y02r96Plugin.this.output.sendSysexMessage(sysexdata);
				} catch (final InvalidMidiDataException e) {
					LOG.log(Level.WARNING, "Could not send MIDI message", e);
				}
			}

			@Override
			public void activeSensing() {
				/* not needed here */
			}
		});

		final Y02r96MidiReceiver receiver = new Y02r96MidiReceiver(this.mixer);
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
				midiConnection.setInputPortName(Y02r96Plugin.this.input.getName());
				midiConnection.setOutputPortName(Y02r96Plugin.this.output.getName());
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
		return Y02r96Plugin.ID;
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
		return "Yamaha 02r96";
	}
}
