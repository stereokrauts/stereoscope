package com.stereokrauts.stereoscope.mixer.yamaha.y02r96;

import com.stereokrauts.lib.midi.api.ISendMidi;
import com.stereokrauts.stereoscope.mixer.yamaha.genericmidsize.GenericMidsizeMixer;
import com.stereokrauts.stereoscope.mixer.yamaha.genericmidsize.midi.GenericMidsizeSysexParameter;
import com.stereokrauts.stereoscope.plugin.interfaces.AbstractApplicationContext;

/**
 * This class represents a Yamaha 02R96 mixer.
 * @author th
 *
 */
public final class Y02r96Mixer extends GenericMidsizeMixer {
	private final Y02r96MidiTransmitter sysex;

	// i/o-specs for this mixer
	private static final int INPUT_CHANNEL_COUNT = 56;
	private static final int OUTPUT_AUX_COUNT = 8;
	private static final int OUTPUT_BUS_COUNT = 8;
	private static final int MATRIX_COUNT = 0;
	private static final int OUTPUT_CHANNEL_COUNT = 2;
	private static final int OUTPUT_GEQ_COUNT = 0;

	private final Y02r96Plugin pluginInstance;

	private AbstractApplicationContext context;

	public Y02r96Plugin getPluginInstance() {
		return this.pluginInstance;
	}

	public Y02r96Mixer(final Y02r96Plugin myPluginInstance, final ISendMidi midi) {
		super(midi);
		this.pluginInstance = myPluginInstance;
		if (pluginInstance != null) {
			this.context = pluginInstance.getApplContext();
		}
		this.sysex = new Y02r96MidiTransmitter(this, midi);
	}

	@Override
	protected void requestSamplerate() {
		this.sysex.requestSetupParameter(GenericMidsizeSysexParameter.ELMT_SETUP_DIO, GenericMidsizeSysexParameter.PARAM_DIO_CLOCKMASTER, (byte) 0);
	}

	@Override
	public int getChannelCount() {
		return INPUT_CHANNEL_COUNT;
	}

	@Override
	public int getAuxCount() {
		return OUTPUT_AUX_COUNT;
	}

	@Override
	public int getBusCount() {
		return OUTPUT_BUS_COUNT;
	}

	@Override
	public int getMatrixCount() {
		return MATRIX_COUNT;
	}

	@Override
	public int getOutputCount() {
		return OUTPUT_CHANNEL_COUNT;
	}

	@Override
	public int getGeqCount() {
		return OUTPUT_GEQ_COUNT;
	}
	
	@Override
	public String getPluginName() {
		return this.getPluginInstance().getPluginName();
	}

	/* The old mixers need some caching, as they only return the
	 * channel names one character at a time. This makes the whole
	 * thing a bit ugly, the following functions are for "internal use"
	 * only, which is why they are package-private.
	 */
	@Override
	protected void updateChannelNames() {
		for (int i = 0; i < this.getChannelCount(); i++) {
			this.sysex.requestChannelName((byte) i);
		}
	}

	@Override
	public AbstractApplicationContext getContext() {
		return context;
	}
}
