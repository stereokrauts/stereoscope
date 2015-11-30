package com.stereokrauts.stereoscope.webgui;

/**
 * This class maintains some state information for the webgui server. It is
 * derived from the class OscSurfaceState because of these reasons:
 * - To avoid cyclic reference errors when using OscSurfaceState.
 * - To keep the client as stupid as possible (hence stateless)
 * - To asure compatibility to TouchOSC layouts.
 * It also stores informations about the mixer.
 * 
 * @author Tobias Heide &lt;tobi@s-hei.de&gt;
 * @author jansen
 * 
 */
public final class WebclientState {
	/**
	 * Name of the current mixer plugin.
	 */
	private String name = "";
	/**
	 * Number of inputs of the current mixer.
	 */
	private int inputCount = 0;
	/**
	 * Number of auxiliaries of the current mixer.
	 */
	private int auxCount = 0;
	/**
	 * Number of GEQs of the current mixer.
	 */
	private int geqCount = 0;
	/**
	 * Number of busses of the current mixer.
	 */
	private int busCount = 0;
	/**
	 * Number of matrix busses of the current mixer.
	 */
	private int matrixCount = 0;
	/**
	 * Number of outputs of the current mixer.
	 */
	private int outputCount = 0;
	/**
	 * Number of bands of the current GEQ
	 */
	private int geqBandCount = 0;
	/**
	 * The mixer does have GEQs or not
	 */
	private boolean mixerWithGraphicalEQ = false;
	/**
	 * The currently selected GEQ can be a FlexEQ or not
	 */
	private boolean flexEq = false;
	/**
	 * The currently selected aux send.
	 */
	private int currentAux = 0;
	/**
	 * The currently selected graphical equalizer number.
	 */
	private byte currentGEQ = 0;

	/**
	 * The currently selected input channel.
	 */
	private int currentInput = 0;

	/**
	 * Has the user sticked the surface to a specific aux send?
	 */
	private boolean stickedToAux = false;

	/**
	 * Is the snap fader function active?
	 */
	private boolean snapFaders = true;

	/**
	 * @return The currently selected aux send.
	 */
	public int getCurrentAux() {
		return this.currentAux;
	}

	/**
	 * Sets the current aux send.
	 * 
	 * @param newCurrentAux
	 *            the number of the aux send
	 */
	public void setCurrentAux(final byte newCurrentAux) {
		if (!this.stickedToAux) {
			this.currentAux = newCurrentAux;
		}
	}

	/**
	 * Restricts the surface to a aux send.
	 * 
	 * @param aux
	 *            the aux number to restrict the surface to.
	 */
	public void setStickyAux(final int aux) {
		this.stickedToAux = true;
		this.currentAux = aux;
	}

	/**
	 * Release the restriction of the surface to a aux.
	 */
	public void releaseStickyAux() {
		this.stickedToAux = false;
	}

	/**
	 * @return true if the operator has restricted the surface to an aux.
	 */
	public boolean isStickedToAux() {
		return this.stickedToAux;
	}

	/**
	 * Activate or Deactivate the snap fader function.
	 * 
	 * @param newSnapFaders
	 *            true to activate.
	 */
	public void setSnapFaders(final boolean newSnapFaders) {
		this.snapFaders = newSnapFaders;
	}

	/**
	 * @return true, if the snap fader function has previously been activated.
	 */
	public boolean isSnapFaders() {
		return this.snapFaders;
	}

	/**
	 * Set the currently displayed and changeable graphical eq.
	 * 
	 * @param newCurrentGEQ
	 *            The number of the GEQ.
	 */
	public void setCurrentGEQ(final byte newCurrentGEQ) {
		this.currentGEQ = newCurrentGEQ;
	}

	/**
	 * @return The GEQ number that is currently displayed on the surface.
	 */
	public byte getCurrentGEQ() {
		return this.currentGEQ;
	}

	/**
	 * Set the currently displayed and changeable input.
	 * 
	 * @param newCurrentInput
	 *            ;
	 */
	public void setCurrentInput(final byte newCurrentInput) {
		this.currentInput = newCurrentInput;
	}

	/**
	 * @return The input number that is currently displayed on the surface
	 */
	public int getCurrentInput() {
		return this.currentInput;
	}

	public int getInputCount() {
		return inputCount;
	}

	public void setInputCount(int inputCount) {
		this.inputCount = inputCount;
	}

	public int getAuxCount() {
		return auxCount;
	}

	public void setAuxCount(int auxCount) {
		this.auxCount = auxCount;
	}

	public int getGeqCount() {
		return geqCount;
	}

	public void setGeqCount(int geqCount) {
		this.geqCount = geqCount;
	}

	public boolean isMixerWithGraphicalEQ() {
		return mixerWithGraphicalEQ;
	}

	public void setMixerWithGraphicalEQ(boolean mixerWithGraphicalEQ) {
		this.mixerWithGraphicalEQ = mixerWithGraphicalEQ;
	}

	public boolean isFlexEq() {
		return flexEq;
	}

	public void setFlexEq(boolean flexEq) {
		this.flexEq = flexEq;
	}

	public int getGeqBandCount() {
		return geqBandCount;
	}

	public void setGeqBandCount(int geqBandCount) {
		this.geqBandCount = geqBandCount;
	}

	public int getBusCount() {
		return busCount;
	}

	public void setBusCount(int busCount) {
		this.busCount = busCount;
	}

	public int getMatrixCount() {
		return matrixCount;
	}

	public void setMatrixCount(int matrixCount) {
		this.matrixCount = matrixCount;
	}

	public int getOutputCount() {
		return outputCount;
	}

	public void setOutputCount(int outputCount) {
		this.outputCount = outputCount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
