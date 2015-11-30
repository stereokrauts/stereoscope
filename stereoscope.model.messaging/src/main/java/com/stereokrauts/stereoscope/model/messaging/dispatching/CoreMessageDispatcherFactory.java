package com.stereokrauts.stereoscope.model.messaging.dispatching;


/**
 * This class returns the appropriate CoreMessageDispatcher
 * for this instance of stereoscope.
 * @author theide
 *
 */
public final class CoreMessageDispatcherFactory {
	private CoreMessageDispatcherFactory() { }
	
	public static CoreMessageDispatcher getInstance(final String dispatcherName) {
		CoreMessageDispatcher manager;
		
		if (dispatcherName.equals("demo-dispatcher")) {
			manager = new CoreMessageDispatcherDemoversion();
		} else if (dispatcherName.equals("standard-dispatcher")) {
			manager = new CoreMessageDispatcher();
		} else {
			throw new RuntimeException("Illegal dispatcher type: " + dispatcherName);
		}
		final Thread dispatchThread = new Thread(manager, "Message dispatch thread");
		dispatchThread.setPriority(Thread.NORM_PRIORITY - 2);
		dispatchThread.start();
		
		return manager;
	}
}
