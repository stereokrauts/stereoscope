package model.mixer.interfaces;

/**
 * Mixers implementing this interface will provide channel
 * names to the surface.
 * @author Tobias Heide &lt;tobi@s-hei.de&gt;
 *
 */
public interface IProvideChannelNames {
	/**
	 * Requests the channel names from the mixer asynchronously.
	 */
	void getChannelNames();
}
