package com.stereokrauts.lib.logging;

import java.util.logging.Level;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

/**
 * This is a Adapter class to the Backend logging system.
 * @author theide
 *
 */
public final class SLogger {
	private final Logger delegate;

	public SLogger(final Logger l) {
		this.delegate = l;
	}

	public void info(final String string) {
		this.delegate.log(SLogger.class.getCanonicalName(), org.apache.log4j.Level.INFO, string, null);
	}

	public void warning(final String string) {
		this.delegate.log(SLogger.class.getCanonicalName(), org.apache.log4j.Level.WARN, string, null);
	}

	public void error(final String string) {
		this.delegate.log(SLogger.class.getCanonicalName(), org.apache.log4j.Level.ERROR, string, null);
	}

	public void severe(final String string) {
		this.delegate.log(SLogger.class.getCanonicalName(), org.apache.log4j.Level.FATAL, string, null);
	}

	public void entering(final String string, final String string2) {
		this.delegate.log(SLogger.class.getCanonicalName(), org.apache.log4j.Level.DEBUG, "Entering " + string + ": " + string2, null);
	}

	public void fatal(final String string, final Exception e1) {
		this.delegate.log(SLogger.class.getCanonicalName(), org.apache.log4j.Level.FATAL, string, e1);
	}

	public void log(final Level lev, final String string) {
		this.delegate.log(SLogger.class.getCanonicalName(), levelToPrio(lev), string, null);
	}

	public void log(final Level lev, final String string, final Throwable ex) {
		this.delegate.log(SLogger.class.getCanonicalName(), levelToPrio(lev), string, ex);
	}

	public void finest(final String string) {
		this.delegate.log(SLogger.class.getCanonicalName(), org.apache.log4j.Level.TRACE, string, null);
	}

	public void fine(final String string) {
		this.delegate.log(SLogger.class.getCanonicalName(), org.apache.log4j.Level.TRACE, string, null);
	}

	private Priority levelToPrio(final Level l) {
		if (l == Level.SEVERE) {
			return org.apache.log4j.Level.FATAL;
		} else if (l == Level.WARNING) {
			return org.apache.log4j.Level.WARN;
		} else if (l == Level.INFO) {
			return org.apache.log4j.Level.INFO;
		} else if (l == Level.FINE) {
			return org.apache.log4j.Level.DEBUG;
		} else if (l == Level.FINER) {
			return org.apache.log4j.Level.DEBUG;
		} else if (l == Level.FINEST) {
			return org.apache.log4j.Level.TRACE;
		} else {
			return org.apache.log4j.Level.ERROR;
		}
	}

}
