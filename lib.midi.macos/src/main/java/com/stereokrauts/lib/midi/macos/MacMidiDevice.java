package com.stereokrauts.lib.midi.macos;

import com.stereokrauts.lib.midi.macos.jna.MIDIDeviceRef;

public final class MacMidiDevice {
	private final MacMidiService macMidiService;
	private final MIDIDeviceRef deviceRef;

	public MacMidiDevice(final MacMidiService macMidiService, final MIDIDeviceRef deviceRef) {
		this.macMidiService = macMidiService;
		this.deviceRef = deviceRef;
	}

	MIDIDeviceRef getReference() {
		return deviceRef;
	}

	public String getName() {
		return macMidiService.getName(deviceRef);
	}

	public boolean isOnline() {
		return !macMidiService.isOffline(deviceRef);
	}
}
