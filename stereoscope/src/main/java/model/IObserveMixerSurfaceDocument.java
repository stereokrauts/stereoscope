package model;

import model.surface.OscSurface;
import model.surface.SurfaceViewProvider;

/**
 * This interface is implemented by all objects that
 * want to be notified of changes in the MixerSurfaceDocument.
 * @author Tobias Heide &lt;tobi@s-hei.de&gt;
 */
public interface IObserveMixerSurfaceDocument {
	/**
	 * This method is invoked whenever a surface has been added
	 * to the MixerSurfaceDocument.
	 * @param srfView an associated view for this surface
	 * @param surface the new surface.
	 */
	void surfaceAdded(SurfaceViewProvider srfView, OscSurface surface);
}
