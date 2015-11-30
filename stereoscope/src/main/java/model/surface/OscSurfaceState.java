package model.surface;

/**
 * This class maintains some state information for the TouchOsc surface, as
 * TouchOsc is in some regards "stateless". You could also say, it is brainless
 * ;-)
 * 
 * @author Tobias Heide &lt;tobi@s-hei.de&gt;
 * 
 */
public final class OscSurfaceState {
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

}
