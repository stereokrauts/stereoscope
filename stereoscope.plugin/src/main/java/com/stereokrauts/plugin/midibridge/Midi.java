package com.stereokrauts.plugin.midibridge;

import java.util.concurrent.TimeUnit;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.stereokrauts.lib.midi.api.IMidiSubsystem;

public final class Midi implements ApplicationContextAware {
	private static ApplicationContext applicationContext;
	private static long TIMEOUT = TimeUnit.SECONDS.toMillis(10);

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) {
		Midi.applicationContext = applicationContext;
	}

	public static IMidiSubsystem getMidiSubsystem() {
		IMidiSubsystem midiSubsystem = null;
		final long startTime = System.currentTimeMillis();
		boolean timeoutReached = false;
		while (midiSubsystem == null && ! timeoutReached) {
			if (applicationContext != null) {
				midiSubsystem = (IMidiSubsystem) applicationContext.getBean("stereoscope.plugin.midi");
			}
			if (System.currentTimeMillis() - startTime > TIMEOUT) {
				timeoutReached = true;
			}
		}
		if (midiSubsystem == null) {
			failMidiSubsystemNotReady();
		}
		return midiSubsystem;
	}

	private static void failMidiSubsystemNotReady() throws IllegalStateException {
		throw new IllegalStateException("The MIDI subsystem is not yet ready, please wait a few seconds before trying again.");
	}
}
