package model.protocol.osc;

/**
 * This interface has to be implemented by classes that
 * want to receive raw OSC messages.
 * @author th
 *
 */
public interface IOscEventListener {
	void oscEvent(final IOscMessage msg);
}
