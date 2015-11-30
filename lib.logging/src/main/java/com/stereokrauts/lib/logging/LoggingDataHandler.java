package com.stereokrauts.lib.logging;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;

/**
 * This is registered at the java logging api to receive
 * all log messages of the application and to handle them
 * correctly. It notifies the views of this model when
 * new messages are received.
 * @author Tobias Heide &lt;tobi@s-hei.de&gt;
 *
 */
final class LoggingDataHandler
extends AppenderSkeleton
implements LoggingDataHandlerViewProvider {
	private static final int MESSAGE_LIMIT = 11000;
	private static final int MESSAGE_DELETE_OVER_LIMIT = 1000;
	/**
	 * Contains all currently registered views for this model.
	 */
	private List<IObserveLoggingDataHandler> views;
	/**
	 * Contains a list of all messages that were being reported
	 * since application startup.
	 */
	private List<LoggingEvent> messages;
	
	/**
	 * Creates a new LoggingDataHandler.
	 */
	public LoggingDataHandler() {
		this.views = new ArrayList<IObserveLoggingDataHandler>();
		this.messages = new ArrayList<LoggingEvent>();
	}

	/**
	 * This is an internal function that is called every time
	 * a log message has been added to the internal array,
	 * to notify the observers of this object.
	 * @param record The new log record.
	 */
	private void fireChanged(final LoggingEvent record) {
		for (final IObserveLoggingDataHandler v : this.views) {
			v.logmessageAdded(new Log4jEventAdapter(record).toLogEvent());
		}
	}

	/**
	 * This method releases all resources that were obtained
	 * in the constructor of this object. Would as well have
	 * been done by the garbage collector, but maybe some time
	 * later log messages should be written to disk or something
	 * else, so this function is provided for future use.
	 */
	@Override
	public synchronized void close() {
		this.views = null;
		this.messages = null;
	}

	@Override
	public synchronized void registerView(final IObserveLoggingDataHandler v) {
		this.views.add(v);
	}

	@Override
	public synchronized LoggingEvent getMessageAt(final int rowIndex) {
		return this.messages.get(rowIndex);
	}

	@Override
	public synchronized int getMessageCount() {
		return this.messages.size();
	}


	@Override
	public void writeLog(final BufferedWriter writer) throws IOException {
		LoggingEvent[] localCopy;
		synchronized (this) {
			localCopy = this.messages.toArray(new LoggingEvent[0]);
		}

		for (final LoggingEvent r : localCopy) {
			final LocationInfo loc = r.getLocationInformation();
			 writer.write(r.timeStamp + ";" + r.getLoggerName()
				+ ";" + r.getLevel() + ";" + r.getThreadName()
				+ ";" + r.getFQNOfLoggerClass()
				+ ";" + r.getMessage()
				+ ";" + loc.fullInfo
				+ ";" + r.getThrowableInformation() 
				+ ";" + r.getThrowableStrRep() + ";"
				+ r.getClass() + "\n");
		}
	}

	@Override
	public boolean requiresLayout() {
		return false;
	}

	@Override
	protected void append(final LoggingEvent record) {
		if (this.messages.size() > MESSAGE_LIMIT) {
			this.messages.subList(0, MESSAGE_DELETE_OVER_LIMIT).clear();
		}
		this.messages.add(record);
		this.fireChanged(record);
	}

}
