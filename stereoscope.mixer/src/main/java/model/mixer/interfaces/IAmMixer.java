package model.mixer.interfaces;

import com.stereokrauts.stereoscope.model.messaging.api.IMessageReceiver;
import com.stereokrauts.stereoscope.model.messaging.api.IObservableMessageSender;



/**
 * This abstract class has to be extended by every mixer plugin and
 * is the central communication point between mixer and surface.
 * @author Tobias Heide &lt;tobi@s-hei.de&gt;
 *
 */
public abstract class IAmMixer implements IMessageReceiver, IObservableMessageSender {

	public abstract void getAuxMaster(int aux);
	public abstract void getOutputMaster();
	public abstract void getBusMaster(int bus);
	public abstract void getChannelLevel(int channel);
	public abstract void getChannelAux(int channel, int aux);
	public abstract void getChannelOnButton(int channel);
	public abstract void getAuxChannelOnButton(int channel, int aux);

	public abstract void getAllAuxLevels(int aux);
	public abstract void getAllAuxChannelOn(int aux);
	public abstract void getAllChannelLevels();
	public abstract void getAllInputValues(int inputChn);
	public abstract void getAllChannelOnButtons();
	public abstract void getAllGroupsStatus();
	public abstract void getAllDelayTimes();
	public abstract void getAllBusStatus();

	/**
	 * @return Number of channels that the mixer supports.
	 */
	public abstract int getChannelCount();

	/**
	 * @return The number of auxiliary sends the mixer has.
	 */
	public abstract int getAuxCount();

	/**
	 * @return The number of busses the mixer has.
	 */
	public abstract int getBusCount();

	/**
	 * @return The number of configurable outputs the mixer has.
	 */
	public abstract int getOutputCount();

	/**
	 * @return The number of configurable matrix busses the mixer has.
	 */
	public abstract int getMatrixCount();

	/**
	 * @return The number of configurable geqs the mixer has.
	 */
	public abstract int getGeqCount();
	
	/**
	 * @return The plugin name as defined in PluginDescriptor
	 */
	public abstract String getPluginName();

}
