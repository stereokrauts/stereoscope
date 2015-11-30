package com.stereokrauts.stereoscope.wizards.newmixer;

public final class NewMixerWizardModel {
	private Object mixerPluginMemento;

	public boolean isValid() {
		return false;
	}

	public Object getMixerPluginMemento() {
		return this.mixerPluginMemento;
	}

	public void setMixerPluginMemento(final Object mixerPluginMemento) {
		this.mixerPluginMemento = mixerPluginMemento;
	}

}
