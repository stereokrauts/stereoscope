package com.stereokrauts.lib.midi.macos;

import com.stereokrauts.lib.midi.macos.jna.MIDIEntityRef;

public final class MacMidiEntity {
	private final MacMidiService macMidiService;
	private final MIDIEntityRef entityRef;

	public MacMidiEntity(final MacMidiService macMidiService, final MacMidiDevice device, final MIDIEntityRef entityRef) {
		this.macMidiService = macMidiService;
		this.entityRef = entityRef;
	}

	MIDIEntityRef getReference() {
		return entityRef;
	}

	public String getDisplayName() {
		return getName();
	}

	private String getName() {
		return macMidiService.getName(entityRef);
	}

	public boolean isOutputPort() {
		return getOutputCount() > 0;
	}

	public boolean isInputPort() {
		return getInputCount() > 0;
	}

	private int getInputCount() {
		return macMidiService.getService().MIDIEntityGetNumberOfSources(entityRef);
	}

	private int getOutputCount() {
		return macMidiService.getService().MIDIEntityGetNumberOfDestinations(entityRef);
	}

	@Override
	public String toString() {
		return String.format("MacMidiEntity[name=%s,inputs=%d,outputs=%d]", getDisplayName(), getInputCount(), getOutputCount());
	}

}
