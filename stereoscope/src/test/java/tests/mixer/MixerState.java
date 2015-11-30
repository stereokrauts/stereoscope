package tests.mixer;

/**
 * This class represents the state of a whole mixer and is
 * a Java Bean.
 * 
 * @author Tobias Heide &lt;tobi@s-hei.de&gt;
 */
public class MixerState {
	public static final int GEQ_NUMBER_OF_BANDS = 31;

	private final float[] channelLevels;
	private final boolean[] channelOnButtons;
	private final float[][] channelAuxSends;

	private final String[] channelNames;
	private final float[][][] geqBandLevels;	/* [geqNumber][leftRightFlag][bandNumber] */

	private float masterLevel;
	private final float auxMasters[];
	private final float busMaster[];

	private final float[] busDelays;
	private final float[] auxDelays;
	private final float[] outputDelays;

	private int numberOfChannels = 0;
	private int numberOfAux = 0;
	private int numberOfBus = 0;
	private int numberOfOutputs = 0;
	private int numberOfGeqs = 0;



	public MixerState(final int channelCount, final int auxCount, final int busCount, final int outputsCount, final int geqCount) {
		this.numberOfAux = auxCount;
		this.numberOfChannels = channelCount;
		this.numberOfGeqs = geqCount;
		this.numberOfBus = busCount;
		this.numberOfOutputs = outputsCount;

		this.channelLevels = new float[channelCount];
		this.channelOnButtons = new boolean[channelCount];
		this.channelAuxSends = new float[auxCount][channelCount];
		this.channelNames = new String[channelCount];
		this.geqBandLevels = new float[geqCount][2][GEQ_NUMBER_OF_BANDS];

		this.busDelays = new float[busCount];
		this.auxDelays = new float[auxCount];
		this.outputDelays = new float[outputsCount];

		this.auxMasters = new float[auxCount];
		this.busMaster = new float[busCount];
		this.resetValues();
	}

	private void resetValues() {
		/* reset channel levels and names and on buttons */
		for (int i = 0; i < this.numberOfChannels; i++) {
			this.channelLevels[i] = 0.0f;
			this.channelNames[i] = "CH" + i;
			this.channelOnButtons[i] = false;
		}

		/* reset aux sends */
		for (int i = 0; i < this.numberOfAux; i++) {
			for (int j = 0; j < this.numberOfChannels; j++) {
				this.channelAuxSends[i][j] = 0.0f;
			}
		}

		/* reset geqs */
		for (int i = 0; i < this.numberOfGeqs; i++) {
			for (int j = 0; j < GEQ_NUMBER_OF_BANDS; j++) {
				this.geqBandLevels[i][0][j] = 0.0f;
				this.geqBandLevels[i][1][j] = 0.0f;
			}
		}

		/* reset masters */
		this.masterLevel = 0.0f;

		/* reset aux masters and delays */
		for (int i = 0; i < this.numberOfAux; i++) {
			this.auxMasters[i] = 0.0f;
			this.auxDelays[i] = 0.0f;
		}

		/* reset bus delays */
		for (int i = 0; i < this.numberOfBus; i++) {
			this.busDelays[i] = 0.0f;
		}

		/* reset output delays */
		for (int i = 0; i < this.numberOfOutputs; i++) {
			this.outputDelays[i] = 0.0f;
		}

	}

	public float getChannelLevel(final int channel) {
		return this.channelLevels[channel];
	}

	public void setChannelLevel(final int channel, final float level) {
		this.channelLevels[channel] = level;
	}

	public boolean getChannelOnButtons(final int channel) {
		return this.channelOnButtons[channel];
	}

	public void setChannelOnButtons(final int channel, final boolean status) {
		this.channelOnButtons[channel] = status;
	}

	public float getChannelAuxSend(final int aux, final int channel) {
		return this.channelAuxSends[aux][channel];
	}

	public void setChannelAuxSend(final int aux, final int channel, final float level) {
		this.channelAuxSends[aux][channel] = level;
	}

	public String getChannelName(final int channel) {
		return this.channelNames[channel];
	}

	public void setChannelName(final int channel, final String name) {
		this.channelNames[channel] = name;
	}

	public float getGeqBandLevels(final int geq, final boolean rightChannel, final int band) {
		return this.geqBandLevels[geq][rightChannel ? 1 : 0][band];
	}

	public void setGeqBandLevels(final int geq, final boolean rightChannel, final int band, final float level) {
		this.geqBandLevels[geq][rightChannel ? 1 : 0][band] = level;
	}

	public float getMasterLevel() {
		return this.masterLevel;
	}

	public void setMasterLevel(final float masterLevel) {
		this.masterLevel = masterLevel;
	}

	public float getAuxMasters(final int aux) {
		return this.auxMasters[aux];
	}

	public void setAuxMasters(final int aux, final float level) {
		this.auxMasters[aux] = level;
	}

	public int getNumberOfChannels() {
		return this.numberOfChannels;
	}

	public int getNumberOfAux() {
		return this.numberOfAux;
	}

	public int getNumberOfBus() {
		return this.numberOfBus;
	}

	public int getNumberOfOutputs() {
		return this.numberOfOutputs;
	}

	public int getNumberOfGeqs() {
		return this.numberOfGeqs;
	}

	public float getAuxDelay(final int aux) {
		return this.auxDelays[aux];
	}

	public void setAuxDelay(final int aux, final float value) {
		this.auxDelays[aux] = value;
	}

	public float getBusDelay(final int bus) {
		return this.busDelays[bus];
	}

	public void setBusDelay(final int bus, final float value) {
		this.busDelays[bus] = value;
	}

	public float getOutputDelay(final int output) {
		return this.outputDelays[output];
	}

	public void setOutputDelay(final int output, final float value) {
		this.outputDelays[output] = value;
	}

	public void setBusMasters(final int number, final float value) {
		this.busMaster[number] = value;
	}

	public float getBusMasters(final int number) {
		return this.busMaster[number];
	}
}
