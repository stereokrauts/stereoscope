package com.stereokrauts.lib.logging;

import org.apache.log4j.spi.LoggingEvent;

final class Log4jEventAdapter {
	private final LoggingEvent record;

	public Log4jEventAdapter(final LoggingEvent record) {
		this.record = record;
	}

	public LogEvent toLogEvent() {
		final LogEvent event = new LogEvent();
		event.setLevel(new Log4jLevelAdapter(this.record.getLevel()).getLevel());
		event.setLocation(this.record.getLocationInformation().fullInfo);
		event.setMessage(this.record.getMessage().toString());
		event.setThreadName(this.record.getThreadName());
		if (this.record.getThrowableInformation() != null) {
			event.setThrowable(this.record.getThrowableInformation().getThrowable());
		}
		event.setTimeStamp(this.record.getTimeStamp());
		return event;
	}

}
