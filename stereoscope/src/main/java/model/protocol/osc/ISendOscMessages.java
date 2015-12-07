package model.protocol.osc;


/**
 * This interface is implemented by all classes that send
 * OSC messages to a surface.
 * @author theide
 *
 */
public interface ISendOscMessages {
	void sendMessage(IOscMessage m);
}
