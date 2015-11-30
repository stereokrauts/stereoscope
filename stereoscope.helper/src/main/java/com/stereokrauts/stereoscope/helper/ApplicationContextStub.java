package com.stereokrauts.stereoscope.helper;

import java.io.InputStream;

import model.mixer.interfaces.IAmMixer;
import model.mixer.interfaces.IUnderstandMixer;

import com.stereokrauts.stereoscope.plugin.interfaces.AbstractApplicationContext;

public class ApplicationContextStub extends AbstractApplicationContext {

	public void registerMixerPlugin(final String displayname,
			final Class<? extends IAmMixer> mixerClass,
			final Class<? extends IUnderstandMixer> controllerClass) {
	}

	public InputStream getResourceAsStream(final String pluginIdentifier,
			final String resourcename) {
		return null;
	}

	@Override
	public Object getViewContext() {
		return null;
	}
}

