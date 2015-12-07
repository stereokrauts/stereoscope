package tests.simulation;

public class SimulationMessage {
	public static final int MESSAGE_TYPES_COUNT = 9; /* <- this field contains the number of messages defined below */

	public static final int MESSAGE_TYPE_CHLEVEL = 0;
	public static final int MESSAGE_TYPE_CHAUXLEVEL = 1;
	public static final int MESSAGE_TYPE_MASTER = 2;
	public static final int MESSAGE_TYPE_AUXMASTER = 3;
	public static final int MESSAGE_TYPE_CHONBUTTON = 4;
	public static final int MESSAGE_TYPE_GEQBAND = 5;
	public static final int MESSAGE_TYPE_GEQBANDRESET = 6;
	public static final int MESSAGE_TYPE_GEQFULLRESET = 7;	

	public static final int MESSAGE_TYPE_CHNAME = 8;

	public static final int MESSAGE_TYPE_BUSMASTER = 9;

	public static final int MESSAGE_TYPE_OUTDELAY = 10;



	private int messageType;

	/* the following is a bit messy: we define all attributes that any of the
	 * above messages can have. It is up to the user of this class to determine
	 * which attributes are really set for a specific message type and not
	 * to rely on the data in the other fields.
	 */
	private int geqNumber;
	private int bandNumber;
	private boolean rightGeq;

	private int channelNumber;

	private int auxNumber;

	private boolean channelOn;
	private float level;

	public SimulationMessage() {

	}

	public SimulationMessage(final int messageType) {
		this.messageType = messageType;
	}


	public int getMessageType() {
		return this.messageType;
	}
	public void setMessageType(final int messageType) {
		this.messageType = messageType;
	}
	public int getGeqNumber() {
		return this.geqNumber;
	}
	public void setGeqNumber(final int geqNumber) {
		this.geqNumber = geqNumber;
	}
	public int getBandNumber() {
		return this.bandNumber;
	}
	public void setBandNumber(final int bandNumber) {
		this.bandNumber = bandNumber;
	}
	public boolean isRightGeq() {
		return this.rightGeq;
	}
	public void setRightGeq(final boolean rightGeq) {
		this.rightGeq = rightGeq;
	}
	public int getChannelNumber() {
		return this.channelNumber;
	}
	public void setChannelNumber(final int channelNumber) {
		this.channelNumber = channelNumber;
	}
	public int getAuxNumber() {
		return this.auxNumber;
	}
	public void setAuxNumber(final int auxNumber) {
		this.auxNumber = auxNumber;
	}
	public boolean isChannelOn() {
		return this.channelOn;
	}
	public void setChannelOn(final boolean channelOn) {
		this.channelOn = channelOn;
	}
	public float getLevel() {
		return this.level;
	}
	public void setLevel(final float level) {
		this.level = level;
	}


	public String convertToOscAddress() {
		switch(this.messageType) {
		case SimulationMessage.MESSAGE_TYPE_CHLEVEL:
			return "/stereoscope/input/" + (this.getChannelNumber() + 1) + "/level";
		case SimulationMessage.MESSAGE_TYPE_CHAUXLEVEL:
			return "/stereoscope/stateful/aux/level/fromChannel/" + (this.getChannelNumber() + 1);
		case SimulationMessage.MESSAGE_TYPE_MASTER:
			return "/stereoscope/output/master/level";
		case SimulationMessage.MESSAGE_TYPE_AUXMASTER:
			return "/stereoscope/stateful/aux/level";
		case SimulationMessage.MESSAGE_TYPE_CHONBUTTON:
			return "/stereoscope/input/" + (this.getChannelNumber() + 1) + "/channelOn";
		case SimulationMessage.MESSAGE_TYPE_GEQBAND:
			return "/stereoscope/stateful/dsp/geq/band/" + (this.getBandNumber() + 1) + "/" + (this.isRightGeq() ? "right" : "left") + "/level" ;
		case SimulationMessage.MESSAGE_TYPE_GEQBANDRESET:
			return "/stereoscope/stateful/dsp/geq/band/" + (this.getBandNumber() + 1) + "/" + (this.isRightGeq() ? "right" : "left") + "/reset";
		case SimulationMessage.MESSAGE_TYPE_GEQFULLRESET:
			return "/stereoscope/stateful/dsp/geq/reset";
		default:
			throw new RuntimeException("Oops, there is an error in the definition of message types.");
		}
	}
}
