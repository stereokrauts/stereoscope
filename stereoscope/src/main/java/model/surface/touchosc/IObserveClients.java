package model.surface.touchosc;

/**
 * This class must be implemented by every class that wishes to
 * be notified if a new TouchOSC instance has been detected in the
 * network.
 * @author theide
 *
 */
public interface IObserveClients {
	void surfaceDetected(BonjourAdvertisedTouchOscInstance inst);
}
