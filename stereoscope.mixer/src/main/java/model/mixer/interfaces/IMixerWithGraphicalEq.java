package model.mixer.interfaces;

/**
 * This interface is implemented by all mixers that have graphical
 * equalizers and defines functions for getting and setting these
 * values.
 * 
 * @author Tobias Heide &lt;tobi@s-hei.de&gt;
 */
public interface IMixerWithGraphicalEq {
	/**
	 * This function requests the level of a band of a graphical EQ.
	 * It will be returned asynchronously by the mixer at a later time
	 * using the respective function of the IObserveMixer interface.
	 * @param eqNumber The channel that shall be returned.
	 * @param rightChannel false if the left channel is requested,
	 * 		true if the right channel is requested.
	 * @param band The band of the EQ to request.
	 */
	void getGeqBandLevel(int eqNumber, boolean rightChannel, int band);
	
	/**
	 * @return The maximum number of graphical eqs the mixer has.
	 */
	int getGeqCount();

	/**
	 * This function requests the levels of all bands of a graphical EQ.
	 * It will be returned asynchronously by the mixer at a later time
	 * using the respective function of the IObserveMixer interface.
	 * @param eqNumber The channel that shall be returned.
	 */
	void getAllGeqLevels(byte eqNumber);
	
	/**
	 * Requests from a mixer whether an EQ is a flex EQ 15, in which case
	 * the bottom GEQ should be displayed.
	 * @param eqNumber The eq number to check 
	 */
	void isFlexEQ(short eqNumber);
}
