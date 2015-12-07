package model.protocol.osc;

/**
 * Represents an OSC message.
 * @author th
 *
 */
public interface IOscMessage {
	OscObject get(int i);
	void add(OscObject o);
	OscAddress address();
	void addAll(IOscMessage m);
}
