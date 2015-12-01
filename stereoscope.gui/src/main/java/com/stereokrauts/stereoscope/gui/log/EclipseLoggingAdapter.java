package com.stereokrauts.stereoscope.gui.log;

import org.eclipse.core.runtime.IStatus;

import com.stereokrauts.lib.logging.IObserveLoggingDataHandler;
import com.stereokrauts.lib.logging.LogEvent;
import com.stereokrauts.lib.logging.LogLevel;
import com.stereokrauts.stereoscope.gui.Activator;

public final class EclipseLoggingAdapter implements IObserveLoggingDataHandler {
	@Override
	public void logmessageAdded(final LogEvent m) {
		if (m.getLevel() == LogLevel.TRACE) {
			return;
		}
		final IStatus status = new StereoscopeLogAdapter(m).getStatus();
		Activator.getDefault().getLog().log(status);
	}

}
