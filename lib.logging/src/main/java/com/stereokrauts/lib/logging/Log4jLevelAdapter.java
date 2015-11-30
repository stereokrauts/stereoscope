package com.stereokrauts.lib.logging;

import org.apache.log4j.Level;
import org.apache.log4j.Priority;

final class Log4jLevelAdapter {

	private final Level level;

	public Log4jLevelAdapter(final Level level) {
		this.level = level;
	}

	public LogLevel getLevel() {
		switch (this.level.toInt()) {
		case Level.TRACE_INT:
			return LogLevel.TRACE;
		case Priority.DEBUG_INT:
			return LogLevel.DEBUG;
		case Priority.INFO_INT:
			return LogLevel.INFO;
		case Priority.WARN_INT:
			return LogLevel.WARN;
		case Priority.ERROR_INT:
			return LogLevel.ERROR;
		case Priority.FATAL_INT:
			return LogLevel.FATAL;
		}
		return LogLevel.ERROR;
	}

}
