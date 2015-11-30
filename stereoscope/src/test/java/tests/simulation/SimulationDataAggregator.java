package tests.simulation;

import java.util.HashMap;

/**
 * This class aggregates a unspecified number of messages so
 * that later messages overwrite earlier messages. So this
 * class represents the state of a mixer after a certain number
 * of messages have been received.
 * 
 * @author Tobias Heide &lt;tobi@s-hei.de&gt;
 *
 */
public class SimulationDataAggregator {

	HashMap<Integer,SimulationMessage> lastMessages;
	
	public SimulationDataAggregator() {
		this.lastMessages = new HashMap<Integer, SimulationMessage>();
	}
	
	public synchronized void addMessage(final SimulationMessage msg) {
		if (this.lastMessages.containsKey(msg.getMessageType())) {
			this.lastMessages.remove(msg.getMessageType());
			this.lastMessages.put(msg.getMessageType(), msg);
		} else {
			this.lastMessages.put(msg.getMessageType(), msg);
		}
	}
	
	public SimulationMessage getLastMessageOfType(final int type) {
		return this.lastMessages.get(type);
	}
	

}
