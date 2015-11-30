package com.stereokrauts.stereoscope.mixer.yamaha.dm1000;

import com.stereokrauts.lib.midi.api.ISendMidi;
import com.stereokrauts.stereoscope.mixer.yamaha.genericmidsize.GenericMidsizeMixer;
import com.stereokrauts.stereoscope.mixer.yamaha.genericmidsize.midi.GenericMidsizeSysexParameter;
import com.stereokrauts.stereoscope.plugin.interfaces.AbstractApplicationContext;

public final class DM1000Mixer extends GenericMidsizeMixer {
	DM1000MidiTransmitter sysex;

	// i/o-specs for this mixer
	private final static int inputChannelCount = 48;
	private final static int outputAuxCount = 8;
	private final static int outputBusCount = 8;
	private final static int matrixBusCount = 0;
	private final static int outputChannelCount = 2;
	private final static int outputGeqCount = 0;

	private final DM1000Plugin pluginInstance;
	private final AbstractApplicationContext context;

	public DM1000Mixer(final DM1000Plugin pluginInstance, final ISendMidi midi) {
		super(midi);
		this.pluginInstance = pluginInstance;
		this.context = pluginInstance.getApplContext();
		this.sysex = new DM1000MidiTransmitter(this, midi);
	}

	@Override
	protected void requestSamplerate() {
		this.sysex.requestSetupParameter(GenericMidsizeSysexParameter.ELMT_SETUP_DIO, GenericMidsizeSysexParameter.PARAM_DIO_CLOCKMASTER, (byte) 0);
	}

	@Override
	public int getChannelCount() {
		return inputChannelCount;
	}

	@Override
	public int getAuxCount() {
		return outputAuxCount;
	}

	@Override
	public int getBusCount() {
		return outputBusCount;
	}

	@Override
	public int getMatrixCount() {
		return matrixBusCount;
	}

	@Override
	public int getOutputCount() {
		return outputChannelCount;
	}

	@Override
	public int getGeqCount() {
		return outputGeqCount;
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

	public DM1000Plugin getPluginInstance() {
		return this.pluginInstance;
	}

	@Override
	public AbstractApplicationContext getContext() {
		return context;
	}
}
