package model;

import model.surface.OscSurface;
import model.surface.SurfaceViewProvider;

/**
 * This interface is implemented by objects that are interested
 * in changes of the SurfaceManager.
 * @author Tobias Heide &lt;tobi@s-hei.de&gt;
 *
 */
public interface IObserveSurfaceManager {
	/**
	 * This method is called when a surface is added to the manager.
	 * @param srfView the associated view
	 * @param surface the surface
	 */
	void surfaceAdded(SurfaceViewProvider srfView, OscSurface surface);
	/**
	 * This method is called when a surface is removed from the manager.
	 * @param srf the surface
	 */
	void surfaceRemoved(SurfaceViewProvider srf);
}
