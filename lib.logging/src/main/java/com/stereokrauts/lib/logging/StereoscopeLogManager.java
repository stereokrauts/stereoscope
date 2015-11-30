package com.stereokrauts.lib.logging;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

/**
 * This class connects the java logging api and the stereoscope
 * logging subsystem.
 * @author Tobias Heide &lt;tobi@s-hei.de&gt;
 *
 */
public final class StereoscopeLogManager  {
	private final String logFilename;
	private static final long MAXIMUM_LOG_SIZE = 100 * 1024;
	private static final int MAXIMUM_BACKUPS = 10;
	private static final Layout DEFAULT_LAYOUT = new PatternLayout("%d [%t] %-5p %c - %m%n");

	/**
	 * The object that manages log messages.
	 */
	private final LoggingDataHandler handler;
	
	/** All loggers that are created sometime are registered in this
	 * Map.
	 */
	private final Map<String, Logger> loggers;
	
	/**
	 * Class initialisation.
	 */
	private StereoscopeLogManager(final String logFilename) {
		this.logFilename = logFilename;
		this.handler = new LoggingDataHandler();
		this.loggers = new HashMap<String, Logger>();
		
		this.initLog4J();
	}
	
	private void initLog4J() {
		this.activateConsoleLogging();
		this.activateFileLogging();
	}

	private void activateConsoleLogging() {
		final ConsoleAppender console = new ConsoleAppender(DEFAULT_LAYOUT);
		Logger.getRootLogger().addAppender(console);
	}
	

	private void activateFileLogging() {
		try {
			final FileAppender file = this.createRollingFileAppender();
			Logger.getRootLogger().addAppender(file);
		} catch (final IOException e) {
			this.handleFileLoggingException(e);
		}
	}

	private RollingFileAppender createRollingFileAppender()
			throws IOException {
		final RollingFileAppender rfa = new RollingFileAppender(DEFAULT_LAYOUT, this.logFilename);
		rfa.setMaximumFileSize(MAXIMUM_LOG_SIZE);
		rfa.setMaxBackupIndex(MAXIMUM_BACKUPS);
		return rfa;
	}
	

	private void handleFileLoggingException(final IOException e) {
		System.err.println("Could not initializes log file: " + e);
		Logger.getRootLogger().error("Could not initialize file logger", e);
	}

	/**
	 * @return the current stereoscope log handler as it should
	 * be accessed by the view.
	 */
	public LoggingDataHandlerViewProvider getViewProvider() {
		return this.handler;
	}
	
	/**
	 * This class creates a new logger for a subsystem of stereoscope.
	 * @param subsystem The name of the subsystem
	 * @return a new or existing logger to be used by this subsystem.
	 */
	protected synchronized Logger internalGetLogger(final String subsystem) {
		if (this.loggers.containsKey(subsystem)) {
			return this.loggers.get(subsystem);
		}
		
		final Logger logger = Logger.getLogger(subsystem);

		logger.addAppender(this.handler);
		this.loggers.put(subsystem, logger);
		return logger;
	}
	
	/**
	 * This class creates a new logger for a subsystem of stereoscope.
	 * @param subsystem The name of the subsystem
	 * @return a new or existing logger to be used by this subsystem.
	 */
	protected synchronized Logger internalGetLogger(final Class<?> subsystem) {
		final String className = subsystem.getName();
		if (this.loggers.containsKey(className)) {
			return this.loggers.get(className);
		}
		
		final Logger logger = Logger.getLogger(subsystem);

		logger.addAppender(this.handler);
		this.loggers.put(className, logger);
		return logger;
	}
	
	public static synchronized SLogger getLogger(final String subsystem) {
		return new SLogger(getInstance().internalGetLogger(subsystem));
	}
	
	public static synchronized SLogger getLogger(final Class<?> subsystem) {
		return new SLogger(getInstance().internalGetLogger(subsystem));
	}
	
	/**
	 * Singleton instance.
	 */
	private static StereoscopeLogManager instance = null;
	private static String instance_logFileName;
	
	public static synchronized void setLogFileName(final String logFileName) {
		if (instance_logFileName != null) {
			throw new IllegalStateException("You must call setLogFileName before the first call to getInstance and only once!");
		}
		instance_logFileName = logFileName;
	}
	
	/**
	 * @return the singleton instance of this object.
	 */
	public static synchronized StereoscopeLogManager getInstance() {
		if (instance_logFileName == null) {
			try {
				instance_logFileName = File.createTempFile("stereoscope-log", ".txt").getAbsolutePath();
			} catch (final IOException e) {
				throw new RuntimeException("You must call setLogFileName before getting a logging instance");

			}
		}
		if (instance == null) {
			instance = new StereoscopeLogManager(instance_logFileName);
		}
		return instance;
	}
}
