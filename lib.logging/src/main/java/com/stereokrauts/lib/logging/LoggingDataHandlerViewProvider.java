package com.stereokrauts.lib.logging;

import java.io.BufferedWriter;
import java.io.IOException;

import org.apache.log4j.spi.LoggingEvent;

/**
 * This class hides all the public functions of
 * the LoggingDataHandler that are not neccessary
 * for the view.
 * 
 * @author Tobias Heide &lt;tobi@s-hei.de&gt;
 *
 */
public interface LoggingDataHandlerViewProvider {
	/**
	 * Register a view as observer of this object.
	 * @param v The view that wants to be informed
	 * 		of updates.
	 */
	void registerView(IObserveLoggingDataHandler v);
	/**
	 * Retrieves a specific message.
	 * @param rowIndex The index of the message since
	 * application start.
	 * @return The log message at this index.
	 */
	LoggingEvent getMessageAt(int rowIndex);
	/**
	 * @return the number of messages that were received
	 * since application start.
	 */
	int getMessageCount();
	void writeLog(BufferedWriter writer) throws IOException;
}
