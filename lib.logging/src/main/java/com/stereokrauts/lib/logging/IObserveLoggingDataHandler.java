package com.stereokrauts.lib.logging;


/**
 * This interface is to be implemented by all classes that
 * wish to be notified on arrival of new log messages.
 * @author Tobias Heide &lt;tobi@s-hei.de&gt;
 *
 */
public interface IObserveLoggingDataHandler {
	/**
	 * This method is called when a new message has been
	 * logged.
	 * @param m The log message
	 */
	void logmessageAdded(LogEvent m);
}
