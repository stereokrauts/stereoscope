package com.stereokrauts.lib.midi.macos.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.stereokrauts.lib.midi.api.IMidiSubsystem;

public final class MidiClient implements ApplicationContextAware {
	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) {
		MidiClient.applicationContext = applicationContext;
	}

	public static IMidiSubsystem getMidiSubsystem() {
		while (internalGetMidiSubsystem() == null) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				throw new IllegalStateException(e);
			}
		}
		return internalGetMidiSubsystem();
	}

	private static IMidiSubsystem internalGetMidiSubsystem() {
		try {
			return (IMidiSubsystem) applicationContext
					.getBean("stereoscope.plugin.midi");
		} catch (NullPointerException e) {
			return null;
		}
	}

}
