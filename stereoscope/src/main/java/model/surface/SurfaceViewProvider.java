package model.surface;

/**
 * This class provides the interface a view may use on the Surface model
 * class.
 * @author Tobias Heide &lt;tobi@s-hei.de&gt;
 */
public interface SurfaceViewProvider {
	/**
	 * @return The ip address of the surface.
	 */
	String getIpAddress();
	/**
	 * @return The incoming port of the surface.
	 */
	int getIncomingPort();
	/**
	 * @return The outgoing port of the surface.
	 */
	int getOutgoingPort();
	/**
	 * @return true if this surface is sticked to a specific aux send.
	 */
	boolean isStickyAux();
	/**
	 * @return The aux the user currently selected on the surface.
	 */
	byte getCurrentAux();
	/**
	 * @return true if the user activated the snap fader function on the
	 * surface.
	 */
	boolean isSnapFaders();
}
