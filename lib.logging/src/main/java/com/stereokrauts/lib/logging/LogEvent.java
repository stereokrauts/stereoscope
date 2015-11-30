package com.stereokrauts.lib.logging;

public final class LogEvent {
	private LogLevel level;
	private String location;
	private String message;
	private String threadName;
	private Throwable throwable;
	private long timeStamp;

	public void setLevel(final LogLevel level) {
		this.level = level;		
	}

	public void setLocation(final String fullInfo) {
		this.location = fullInfo;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

	public void setThreadName(final String threadName) {
		this.threadName = threadName;
	}

	public void setThrowable(final Throwable throwable) {
		this.throwable = throwable;
	}

	public void setTimeStamp(final long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public LogLevel getLevel() {
		return this.level;
	}

	public String getLocation() {
		return this.location;
	}

	public String getMessage() {
		return this.message;
	}

	public String getThreadName() {
		return this.threadName;
	}

	public Throwable getThrowable() {
		return this.throwable;
	}

	public long getTimeStamp() {
		return this.timeStamp;
	}

}
