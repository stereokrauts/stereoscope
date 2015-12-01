package com.stereokrauts.stereoscope.gui.log;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.stereokrauts.lib.logging.LogEvent;

final class StereoscopeLogAdapter {
	private final LogEvent event;

	public StereoscopeLogAdapter(final LogEvent event) {
		this.event = event;
	}

	public IStatus getStatus() {
		int level = 0;
		switch (this.event.getLevel()) {
		case TRACE: level = IStatus.OK; break;
		case DEBUG: level = IStatus.OK; break;
		case INFO:  level = IStatus.INFO; break;
		case WARN:  level = IStatus.WARNING; break;
		case ERROR: level = IStatus.ERROR; break;
		case FATAL: level = IStatus.ERROR; break;
		}
		final Status status = new Status(level, this.event.getLocation(), this.event.getMessage(), this.event.getThrowable());
		return status;
	}

}
